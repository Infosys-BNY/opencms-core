package org.opencms.config.service;

import org.opencms.config.dto.SystemSettingsDto;
import org.opencms.config.entity.SystemSettings;
import org.opencms.config.repository.SystemSettingsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SystemSettingsService {
    
    private final SystemSettingsRepository repository;
    
    public SystemSettingsService(SystemSettingsRepository repository) {
        this.repository = repository;
    }
    
    public List<SystemSettingsDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public SystemSettingsDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("System settings not found"));
    }
    
    public SystemSettingsDto save(SystemSettingsDto dto) {
        SystemSettings entity = toEntity(dto);
        SystemSettings saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private SystemSettingsDto toDto(SystemSettings entity) {
        SystemSettingsDto dto = new SystemSettingsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private SystemSettings toEntity(SystemSettingsDto dto) {
        SystemSettings entity = new SystemSettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
