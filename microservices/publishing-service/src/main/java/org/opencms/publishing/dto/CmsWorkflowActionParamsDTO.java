package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsWorkflowActionParamsDTO {
    private List<String> publishIds;
    private List<String> removeIds;
    private CmsPublishListTokenDTO token;
}
