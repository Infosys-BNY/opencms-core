package org.opencms.user.service;

import org.opencms.user.entity.User;
import org.opencms.user.entity.UserData;
import org.opencms.user.exception.AuthenticationException;
import org.opencms.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private TwoFactorService twoFactorService;
    
    private final Map<String, InvalidLoginTracker> invalidLogins = new ConcurrentHashMap<>();
    private static final int MAX_INVALID_LOGINS = 5;
    private static final long LOCK_DURATION_MS = 15 * 60 * 1000;
    
    @Transactional
    public User authenticateUser(String username, String password, String secondFactorCode) {
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password is required");
        }
        
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> {
                trackInvalidLogin(username);
                return new AuthenticationException("Invalid username or password");
            });
        
        if (!passwordService.checkPassword(password, user.getUserPassword())) {
            trackInvalidLogin(username);
            throw new AuthenticationException("Invalid username or password");
        }
        
        if (user.getUserFlags() == null || (user.getUserFlags() & 1) == 0) {
            throw new AuthenticationException("User account is disabled");
        }
        
        if (twoFactorService.hasSecondFactor(user)) {
            String secret = twoFactorService.getSecret(user);
            if (secondFactorCode == null || !twoFactorService.verifyCode(secret, secondFactorCode)) {
                trackInvalidLogin(username);
                throw new AuthenticationException("Invalid verification code");
            }
        }
        
        checkInvalidLogins(username);
        clearInvalidLogins(username);
        
        user.setUserLastlogin(System.currentTimeMillis());
        
        if (passwordService.needsPasswordUpgrade(password, user.getUserPassword())) {
            String newEncrypted = passwordService.encryptPassword(password);
            user.setUserPassword(newEncrypted);
        }
        
        UserData lastPasswordChange = user.getAdditionalInfo().stream()
            .filter(data -> "LAST_PASSWORD_CHANGE".equals(data.getDataKey()))
            .findFirst()
            .orElse(null);
        
        if (lastPasswordChange == null) {
            UserData newData = new UserData();
            newData.setUserId(user.getUserId());
            newData.setDataKey("LAST_PASSWORD_CHANGE");
            newData.setDataValue(String.valueOf(System.currentTimeMillis()));
            newData.setDataType("java.lang.String");
            user.getAdditionalInfo().add(newData);
        }
        
        return userRepository.save(user);
    }
    
    private void trackInvalidLogin(String username) {
        InvalidLoginTracker tracker = invalidLogins.computeIfAbsent(username, k -> new InvalidLoginTracker());
        tracker.incrementAttempts();
    }
    
    private void checkInvalidLogins(String username) {
        InvalidLoginTracker tracker = invalidLogins.get(username);
        if (tracker != null && tracker.isLocked()) {
            throw new AuthenticationException("Account temporarily locked due to too many failed login attempts");
        }
    }
    
    private void clearInvalidLogins(String username) {
        invalidLogins.remove(username);
    }
    
    private static class InvalidLoginTracker {
        private int attempts = 0;
        private long lockTime = 0;
        
        public void incrementAttempts() {
            attempts++;
            if (attempts >= MAX_INVALID_LOGINS) {
                lockTime = System.currentTimeMillis();
            }
        }
        
        public boolean isLocked() {
            if (lockTime == 0) {
                return false;
            }
            if (System.currentTimeMillis() - lockTime > LOCK_DURATION_MS) {
                attempts = 0;
                lockTime = 0;
                return false;
            }
            return true;
        }
    }
}
