package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsProjectBeanDTO {
    private String id;
    private String name;
    private String description;
    private int type;
    private int rank = 1000;
    private String defaultGroupName;
}
