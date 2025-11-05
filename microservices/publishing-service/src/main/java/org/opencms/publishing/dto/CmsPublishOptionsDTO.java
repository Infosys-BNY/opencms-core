package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishOptionsDTO {
    private boolean includeRelated = true;
    private boolean includeSiblings;
    private String projectId;
    private Map<String, String> parameters;
}
