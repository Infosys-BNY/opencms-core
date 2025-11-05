package org.opencms.config.controller;

import org.opencms.config.dto.LoginSettingsDto;
import org.opencms.config.service.LoginSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/login")
public class LoginSettingsController {
    
    private final LoginSettingsService service;
    
    public LoginSettingsController(LoginSettingsService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<LoginSettingsDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LoginSettingsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<LoginSettingsDto> create(@Validated @RequestBody LoginSettingsDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LoginSettingsDto> update(
            @PathVariable Long id,
            @Validated @RequestBody LoginSettingsDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
