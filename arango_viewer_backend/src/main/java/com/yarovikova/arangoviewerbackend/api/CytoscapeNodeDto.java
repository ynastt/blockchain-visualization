package com.yarovikova.arangoviewerbackend.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CytoscapeNodeDto implements CytoscapeDataElementDto {
    private String id;
    private Object description;
    private String type;
}
