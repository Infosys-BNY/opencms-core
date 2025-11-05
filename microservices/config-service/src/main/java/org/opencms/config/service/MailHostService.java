package org.opencms.config.service;

import org.opencms.config.dto.MailHostDto;
import org.opencms.config.entity.MailHost;
import org.opencms.config.repository.MailHostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MailHostService {
    
    private final MailHostRepository repository;
    
    public MailHostService(MailHostRepository repository) {
        this.repository = repository;
    }
    
    public List<MailHostDto> findAll() {
        return repository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public MailHostDto findById(Long id) {
        return repository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Mail host not found"));
    }
    
    public MailHostDto save(MailHostDto dto) {
        MailHost entity = toEntity(dto);
        MailHost saved = repository.save(entity);
        return toDto(saved);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    private MailHostDto toDto(MailHost entity) {
        MailHostDto dto = new MailHostDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private MailHost toEntity(MailHostDto dto) {
        MailHost entity = new MailHost();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
