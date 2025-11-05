package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locale_settings")
@Data
public class LocaleSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String localeHandlerClass;
    
    @ElementCollection
    @CollectionTable(name = "configured_locales", joinColumns = @JoinColumn(name = "locale_settings_id"))
    @Column(name = "locale")
    private List<String> configuredLocales = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "default_locales", joinColumns = @JoinColumn(name = "locale_settings_id"))
    @Column(name = "locale")
    private List<String> defaultLocales = new ArrayList<>();
}
