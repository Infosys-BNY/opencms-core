package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishGroupDTO {
    private String name;
    private List<CmsPublishResourceDTO> resources;
    private boolean autoSelectable = true;
}
