package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishGroupListDTO {
    private List<CmsPublishGroupDTO> groups = new ArrayList<>();
    private String overrideWorkflowId;
    private CmsPublishListTokenDTO token;
    private String tooManyResourcesMessage = "";
}
