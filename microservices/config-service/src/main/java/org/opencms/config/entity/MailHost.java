package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mail_hosts")
@Data
public class MailHost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer port;
    
    @Column(nullable = false)
    private String protocol;
    
    @Column
    private String username;
    
    @Column
    private String password;
    
    @Column
    private Integer orderIndex;
    
    @Column
    private String mailFrom;
}
