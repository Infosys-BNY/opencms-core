package org.opencms.config.controller;

import org.opencms.config.dto.HandlerConfigDto;
import org.opencms.config.service.HandlerConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/handler")
public class HandlerConfigController {
    
    private final HandlerConfigService service;
    
    public HandlerConfigController(HandlerConfigService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<HandlerConfigDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HandlerConfigDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<HandlerConfigDto> create(@Validated @RequestBody HandlerConfigDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HandlerConfigDto> update(
            @PathVariable Long id,
            @Validated @RequestBody HandlerConfigDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
