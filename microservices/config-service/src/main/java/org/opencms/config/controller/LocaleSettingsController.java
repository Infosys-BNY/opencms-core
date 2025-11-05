package org.opencms.config.controller;

import org.opencms.config.dto.LocaleSettingsDto;
import org.opencms.config.service.LocaleSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/locale")
public class LocaleSettingsController {
    
    private final LocaleSettingsService service;
    
    public LocaleSettingsController(LocaleSettingsService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<LocaleSettingsDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LocaleSettingsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<LocaleSettingsDto> create(@Validated @RequestBody LocaleSettingsDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LocaleSettingsDto> update(
            @PathVariable Long id,
            @Validated @RequestBody LocaleSettingsDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
