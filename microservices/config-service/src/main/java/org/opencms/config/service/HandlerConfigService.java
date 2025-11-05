package org.opencms.config.service;

import org.opencms.config.dto.HandlerConfigDto;
import org.opencms.config.entity.HandlerConfig;
import org.opencms.config.repository.HandlerConfigRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HandlerConfigService {
    
    private final HandlerConfigRepository repository;
    
    public HandlerConfigService(HandlerConfigRepository repository) {
        this.repository = repository;
    }
    
    public List<HandlerConfigDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public HandlerConfigDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Handler config not found"));
    }
    
    public HandlerConfigDto save(HandlerConfigDto dto) {
        HandlerConfig entity = toEntity(dto);
        HandlerConfig saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private HandlerConfigDto toDto(HandlerConfig entity) {
        HandlerConfigDto dto = new HandlerConfigDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private HandlerConfig toEntity(HandlerConfigDto dto) {
        HandlerConfig entity = new HandlerConfig();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
