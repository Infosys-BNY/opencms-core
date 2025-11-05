package org.opencms.config.service;

import org.opencms.config.dto.CacheSettingsDto;
import org.opencms.config.entity.CacheSettings;
import org.opencms.config.repository.CacheSettingsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CacheSettingsService {
    
    private final CacheSettingsRepository repository;
    
    public CacheSettingsService(CacheSettingsRepository repository) {
        this.repository = repository;
    }
    
    public List<CacheSettingsDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public CacheSettingsDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Cache settings not found"));
    }
    
    public CacheSettingsDto save(CacheSettingsDto dto) {
        CacheSettings entity = toEntity(dto);
        CacheSettings saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private CacheSettingsDto toDto(CacheSettings entity) {
        CacheSettingsDto dto = new CacheSettingsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private CacheSettings toEntity(CacheSettingsDto dto) {
        CacheSettings entity = new CacheSettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
