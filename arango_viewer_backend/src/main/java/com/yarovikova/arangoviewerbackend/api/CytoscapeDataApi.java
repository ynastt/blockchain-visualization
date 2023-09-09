package com.yarovikova.arangoviewerbackend.api;

import java.util.List;

public interface CytoscapeDataApi {
    List<CytoscapeDataElementDto> getElementsRelatedWithNodeById(String startNodeId);
}
