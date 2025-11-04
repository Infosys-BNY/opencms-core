package org.opencms.user.controller;

import org.opencms.user.dto.SessionDto;
import org.opencms.user.service.SessionInfo;
import org.opencms.user.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    
    @Autowired
    private SessionService sessionService;
    
    @GetMapping("/current")
    public ResponseEntity<SessionDto> getCurrentSession(@RequestHeader("X-Session-Id") String sessionId) {
        SessionInfo sessionInfo = sessionService.getSession(sessionId);
        if (sessionInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(sessionInfo));
    }
    
    @GetMapping
    public ResponseEntity<List<SessionDto>> getAllSessions() {
        List<SessionInfo> sessions = sessionService.getAllSessions();
        List<SessionDto> sessionDtos = sessions.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sessionDtos);
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> invalidateSession(@PathVariable String sessionId) {
        sessionService.invalidateSession(sessionId);
        return ResponseEntity.ok().build();
    }
    
    private SessionDto toDto(SessionInfo sessionInfo) {
        SessionDto dto = new SessionDto();
        dto.setSessionId(sessionInfo.getSessionId());
        dto.setUserId(sessionInfo.getUserId());
        dto.setUsername(sessionInfo.getUsername());
        dto.setProjectId(sessionInfo.getProjectId());
        dto.setOrgUnit(sessionInfo.getOrgUnit());
        dto.setCreatedAt(sessionInfo.getCreatedAt());
        dto.setLastAccessedAt(sessionInfo.getLastAccessedAt());
        return dto;
    }
}
