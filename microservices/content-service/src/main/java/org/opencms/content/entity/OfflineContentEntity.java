package org.opencms.content.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "CMS_OFFLINE_CONTENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineContentEntity {
    
    @Id
    @Column(name = "RESOURCE_ID", length = 36)
    private String resourceId;
    
    @Lob
    @Column(name = "FILE_CONTENT", nullable = false)
    private byte[] fileContent;
}
