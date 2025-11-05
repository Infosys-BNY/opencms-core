package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "login_settings")
@Data
public class LoginSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer disableMinutes;
    
    @Column(nullable = false)
    private Integer maxBadAttempts;
}
