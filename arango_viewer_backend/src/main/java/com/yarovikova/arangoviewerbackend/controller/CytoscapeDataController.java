package com.yarovikova.arangoviewerbackend.controller;

import com.yarovikova.arangoviewerbackend.api.CytoscapeDataApi;
import com.yarovikova.arangoviewerbackend.api.CytoscapeDataElementDto;
import com.yarovikova.arangoviewerbackend.service.CytoscapeDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:1234", maxAge = 3600)
@RestController
@RequestMapping("/data")
public class CytoscapeDataController implements CytoscapeDataApi {
    private final CytoscapeDataService cytoscapeDataService;

    public CytoscapeDataController(CytoscapeDataService cytoscapeDataService) {
        this.cytoscapeDataService = cytoscapeDataService;
    }

    @GetMapping(path = "/{startNodeId}")
    @Override
    public List<CytoscapeDataElementDto> getElementsRelatedWithNodeById(@PathVariable String startNodeId) {
        return cytoscapeDataService.getElementsRelatedWithNodeById(startNodeId);
    }
}
