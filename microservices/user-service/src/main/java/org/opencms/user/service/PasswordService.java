package org.opencms.user.service;

import com.lambdaworks.crypto.SCryptUtil;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class PasswordService {
    
    private static final String DIGEST_TYPE_SCRYPT = "scrypt";
    private static final String DIGEST_TYPE_MD5 = "md5";
    
    public String encryptPassword(String plainPassword) {
        return SCryptUtil.scrypt(plainPassword, 16384, 8, 1);
    }
    
    public boolean checkPassword(String plainPassword, String encryptedPassword) {
        try {
            return SCryptUtil.check(plainPassword, encryptedPassword);
        } catch (IllegalArgumentException e) {
            return checkMD5(plainPassword, encryptedPassword);
        }
    }
    
    public boolean needsPasswordUpgrade(String plainPassword, String encryptedPassword) {
        try {
            SCryptUtil.check(plainPassword, encryptedPassword);
            return false;
        } catch (IllegalArgumentException e) {
            return checkMD5(plainPassword, encryptedPassword);
        }
    }
    
    private boolean checkMD5(String plainPassword, String encryptedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(plainPassword.getBytes("UTF-8"));
            String computed = Base64.getEncoder().encodeToString(hash);
            return computed.equals(encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
