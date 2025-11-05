package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsWorkflowDTO {
    private String id;
    private String niceName;
    private List<CmsWorkflowActionDTO> actions;
}
