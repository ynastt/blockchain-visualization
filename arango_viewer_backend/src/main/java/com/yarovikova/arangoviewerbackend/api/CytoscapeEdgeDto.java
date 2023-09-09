package com.yarovikova.arangoviewerbackend.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CytoscapeEdgeDto implements CytoscapeDataElementDto {
    private String source;
    private String target;
    private Object description;
    private String type;
 }
