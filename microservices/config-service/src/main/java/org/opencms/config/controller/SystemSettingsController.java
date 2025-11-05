package org.opencms.config.controller;

import org.opencms.config.dto.SystemSettingsDto;
import org.opencms.config.service.SystemSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/system")
public class SystemSettingsController {
    
    private final SystemSettingsService service;
    
    public SystemSettingsController(SystemSettingsService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<SystemSettingsDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SystemSettingsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<SystemSettingsDto> create(@Validated @RequestBody SystemSettingsDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SystemSettingsDto> update(
            @PathVariable Long id,
            @Validated @RequestBody SystemSettingsDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
