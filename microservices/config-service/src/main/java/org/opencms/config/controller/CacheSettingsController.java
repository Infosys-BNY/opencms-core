package org.opencms.config.controller;

import org.opencms.config.dto.CacheSettingsDto;
import org.opencms.config.service.CacheSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/cache")
public class CacheSettingsController {
    
    private final CacheSettingsService service;
    
    public CacheSettingsController(CacheSettingsService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<CacheSettingsDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CacheSettingsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<CacheSettingsDto> create(@Validated @RequestBody CacheSettingsDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CacheSettingsDto> update(
            @PathVariable Long id,
            @Validated @RequestBody CacheSettingsDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
