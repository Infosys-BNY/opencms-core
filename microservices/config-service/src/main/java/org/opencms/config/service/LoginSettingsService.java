package org.opencms.config.service;

import org.opencms.config.dto.LoginSettingsDto;
import org.opencms.config.entity.LoginSettings;
import org.opencms.config.repository.LoginSettingsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoginSettingsService {
    
    private final LoginSettingsRepository repository;
    
    public LoginSettingsService(LoginSettingsRepository repository) {
        this.repository = repository;
    }
    
    public List<LoginSettingsDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public LoginSettingsDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Login settings not found"));
    }
    
    public LoginSettingsDto save(LoginSettingsDto dto) {
        LoginSettings entity = toEntity(dto);
        LoginSettings saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private LoginSettingsDto toDto(LoginSettings entity) {
        LoginSettingsDto dto = new LoginSettingsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private LoginSettings toEntity(LoginSettingsDto dto) {
        LoginSettings entity = new LoginSettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
