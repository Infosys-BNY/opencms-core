package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "handler_configs")
@Data
public class HandlerConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HandlerType type;
    
    @Column(nullable = false)
    private String className;
    
    @Column
    private Integer orderIndex;
    
    @ElementCollection
    @CollectionTable(name = "handler_params", joinColumns = @JoinColumn(name = "handler_id"))
    @MapKeyColumn(name = "param_key")
    @Column(name = "param_value")
    private Map<String, String> parameters = new HashMap<>();
    
    public enum HandlerType {
        RESOURCE_INIT, REQUEST, PASSWORD, VALIDATION, AUTHORIZATION
    }
}
