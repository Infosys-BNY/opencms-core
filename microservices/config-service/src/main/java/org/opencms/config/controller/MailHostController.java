package org.opencms.config.controller;

import org.opencms.config.dto.MailHostDto;
import org.opencms.config.service.MailHostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/mailhost")
public class MailHostController {
    
    private final MailHostService service;
    
    public MailHostController(MailHostService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<MailHostDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MailHostDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<MailHostDto> create(@Validated @RequestBody MailHostDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MailHostDto> update(
            @PathVariable Long id,
            @Validated @RequestBody MailHostDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
