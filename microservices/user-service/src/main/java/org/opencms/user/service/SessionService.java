package org.opencms.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final String USER_SESSIONS_KEY_PREFIX = "user_sessions:";
    private static final long SESSION_TIMEOUT = 30;
    
    public String createSession(String userId, String username, String projectId, String orgUnit) {
        String sessionId = UUID.randomUUID().toString();
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSessionId(sessionId);
        sessionInfo.setUserId(userId);
        sessionInfo.setUsername(username);
        sessionInfo.setProjectId(projectId);
        sessionInfo.setOrgUnit(orgUnit);
        sessionInfo.setCreatedAt(System.currentTimeMillis());
        sessionInfo.setLastAccessedAt(System.currentTimeMillis());
        
        redisTemplate.opsForValue().set(
            SESSION_KEY_PREFIX + sessionId, 
            sessionInfo, 
            SESSION_TIMEOUT, 
            TimeUnit.MINUTES
        );
        
        redisTemplate.opsForSet().add(USER_SESSIONS_KEY_PREFIX + userId, sessionId);
        redisTemplate.expire(USER_SESSIONS_KEY_PREFIX + userId, SESSION_TIMEOUT, TimeUnit.MINUTES);
        
        return sessionId;
    }
    
    public SessionInfo getSession(String sessionId) {
        Object obj = redisTemplate.opsForValue().get(SESSION_KEY_PREFIX + sessionId);
        if (obj instanceof SessionInfo) {
            SessionInfo sessionInfo = (SessionInfo) obj;
            sessionInfo.setLastAccessedAt(System.currentTimeMillis());
            redisTemplate.opsForValue().set(
                SESSION_KEY_PREFIX + sessionId, 
                sessionInfo, 
                SESSION_TIMEOUT, 
                TimeUnit.MINUTES
            );
            return sessionInfo;
        }
        return null;
    }
    
    public void invalidateSession(String sessionId) {
        SessionInfo sessionInfo = getSession(sessionId);
        if (sessionInfo != null) {
            redisTemplate.delete(SESSION_KEY_PREFIX + sessionId);
            redisTemplate.opsForSet().remove(USER_SESSIONS_KEY_PREFIX + sessionInfo.getUserId(), sessionId);
        }
    }
    
    public List<SessionInfo> getAllSessions() {
        Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");
        List<SessionInfo> sessions = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                Object obj = redisTemplate.opsForValue().get(key);
                if (obj instanceof SessionInfo) {
                    sessions.add((SessionInfo) obj);
                }
            }
        }
        return sessions;
    }
    
    public List<SessionInfo> getUserSessions(String userId) {
        Set<Object> sessionIds = redisTemplate.opsForSet().members(USER_SESSIONS_KEY_PREFIX + userId);
        List<SessionInfo> sessions = new ArrayList<>();
        if (sessionIds != null) {
            for (Object sessionId : sessionIds) {
                SessionInfo sessionInfo = getSession(sessionId.toString());
                if (sessionInfo != null) {
                    sessions.add(sessionInfo);
                }
            }
        }
        return sessions;
    }
}
