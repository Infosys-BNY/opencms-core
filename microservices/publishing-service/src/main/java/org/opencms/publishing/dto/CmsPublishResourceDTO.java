package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishResourceDTO {
    private String id;
    private String name;
    private String title;
    private String resourceType;
    private String state;
    private long dateLastModified;
    private String dateLastModifiedStr;
    private String userLastModified;
    private boolean removable;
    private List<CmsPublishResourceDTO> related;
}
