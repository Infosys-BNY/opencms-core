package org.opencms.config.service;

import org.opencms.config.dto.LocaleSettingsDto;
import org.opencms.config.entity.LocaleSettings;
import org.opencms.config.repository.LocaleSettingsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocaleSettingsService {
    
    private final LocaleSettingsRepository repository;
    
    public LocaleSettingsService(LocaleSettingsRepository repository) {
        this.repository = repository;
    }
    
    public List<LocaleSettingsDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public LocaleSettingsDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Locale settings not found"));
    }
    
    public LocaleSettingsDto save(LocaleSettingsDto dto) {
        LocaleSettings entity = toEntity(dto);
        LocaleSettings saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private LocaleSettingsDto toDto(LocaleSettings entity) {
        LocaleSettingsDto dto = new LocaleSettingsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private LocaleSettings toEntity(LocaleSettingsDto dto) {
        LocaleSettings entity = new LocaleSettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
