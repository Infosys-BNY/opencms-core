package org.opencms.publishing.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "CMS_PUBLISH_JOBS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishJobEntity {
    
    @Id
    @Column(name = "HISTORY_ID")
    private String historyId;
    
    @Column(name = "PROJECT_ID", nullable = false)
    private String projectId;
    
    @Column(name = "PROJECT_NAME", nullable = false)
    private String projectName;
    
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    
    @Column(name = "PUBLISH_LOCALE", nullable = false)
    private String publishLocale;
    
    @Column(name = "PUBLISH_FLAGS", nullable = false)
    private Integer publishFlags;
    
    @Column(name = "RESOURCE_COUNT", nullable = false)
    private Integer resourceCount;
    
    @Column(name = "ENQUEUE_TIME", nullable = false)
    private Long enqueueTime;
    
    @Column(name = "START_TIME")
    private Long startTime;
    
    @Column(name = "FINISH_TIME")
    private Long finishTime;
    
    @Lob
    @Column(name = "PUBLISH_LIST")
    private byte[] publishList;
    
    @Lob
    @Column(name = "PUBLISH_REPORT")
    private byte[] publishReport;
}
