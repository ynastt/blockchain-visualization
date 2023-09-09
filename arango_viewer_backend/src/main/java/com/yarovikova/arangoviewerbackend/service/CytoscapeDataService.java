package com.yarovikova.arangoviewerbackend.service;

import com.yarovikova.arangoviewerbackend.api.CytoscapeDataApi;
import com.yarovikova.arangoviewerbackend.api.CytoscapeDataElementDto;
import com.yarovikova.arangoviewerbackend.api.CytoscapeEdgeDto;
import com.yarovikova.arangoviewerbackend.api.CytoscapeNodeDto;
import com.yarovikova.arangoviewerbackend.entity.*;
import com.yarovikova.arangoviewerbackend.repository.*;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CytoscapeDataService implements CytoscapeDataApi {
    private final InRepository inRepository;
    private final NextRepository nextRepository;
    private final OutRepository outRepository;
    private final ParentBlockRepository parentBlockRepository;
    private final TxRepository txRepository;

    public CytoscapeDataService(InRepository inRepository,
                                NextRepository nextRepository,
                                OutRepository outRepository,
                                ParentBlockRepository parentBlockRepository,
                                TxRepository txRepository) {
        this.inRepository = inRepository;
        this.nextRepository = nextRepository;
        this.outRepository = outRepository;
        this.parentBlockRepository = parentBlockRepository;
        this.txRepository = txRepository;
    }

    @Override
    public List<CytoscapeDataElementDto> getElementsRelatedWithNodeById(String startNodeId) {
        Optional<Tx> optionalStartTx = txRepository.findById(startNodeId);
        if (optionalStartTx.isPresent()) {
            Tx tx = optionalStartTx.get();
            return getElementsRelatedWithTx(tx);
        }
        return Collections.emptyList();
    }

    private List<CytoscapeDataElementDto> getElementsRelatedWithTx(Tx startTx) {
        List<CytoscapeDataElementDto> cytoscapeDataElementDtos = new ArrayList<>();
        cytoscapeDataElementDtos.add(CytoscapeNodeDto.builder()
                .id(startTx.get_id())
                .type(startTx.getClass().getSimpleName())
                .description(startTx)
                .build());
        try {
            Optional<ParentBlock> optionalParentBlock = parentBlockRepository.findBy_from__id(startTx.get_id());
            if (optionalParentBlock.isPresent()) {
                ParentBlock parentBlock = optionalParentBlock.get();
                Optional<Block> optionalBlock = Optional.ofNullable(parentBlock.get_to());
                if (optionalBlock.isPresent()) {
                    Block block = optionalBlock.get();
                    cytoscapeDataElementDtos.add(CytoscapeEdgeDto.builder()
                            .source(startTx.get_id())
                            .target(block.get_id())
                            .type(parentBlock.getClass().getSimpleName())
                            .description(parentBlock)
                            .build());
                    cytoscapeDataElementDtos.add(CytoscapeNodeDto.builder()
                            .id(block.get_id())
                            .type(block.getClass().getSimpleName())
                            .description(block)
                            .build());

                }
            }
        } catch (MappingException ignored) {
        }
        try {
            Optional<In> optionalIn = inRepository.findBy_to__id(startTx.get_id());
            if (optionalIn.isPresent()) {
                In in = optionalIn.get();
                Optional<Address> optionalAddress = Optional.ofNullable(in.get_from());
                if (optionalAddress.isPresent()) {
                    Address address = optionalAddress.get();
                    cytoscapeDataElementDtos.add(CytoscapeEdgeDto.builder()
                            .source(startTx.get_id())
                            .target(address.get_id())
                            .type(in.getClass().getSimpleName())
                            .description(in)
                            .build());
                    cytoscapeDataElementDtos.add(CytoscapeNodeDto.builder()
                            .id(address.get_id())
                            .type(address.getClass().getSimpleName())
                            .description(address)
                            .build());

                }
            }
        } catch (MappingException ignored) {
        }
        try {
            Optional<Out> optionalOut = outRepository.findBy_from__id(startTx.get_id());
            if (optionalOut.isPresent()) {
                Out out = optionalOut.get();
                Optional<Address> optionalAddress = Optional.ofNullable(out.get_to());
                if (optionalAddress.isPresent()) {
                    Address address = optionalAddress.get();
                    cytoscapeDataElementDtos.add(CytoscapeEdgeDto.builder()
                            .source(startTx.get_id())
                            .target(address.get_id())
                            .type(out.getClass().getSimpleName())
                            .description(out)
                            .build());
                    cytoscapeDataElementDtos.add(CytoscapeNodeDto.builder()
                            .id(address.get_id())
                            .type(address.getClass().getSimpleName())
                            .description(address)
                            .build());

                }
            }
        } catch (MappingException ignored) {
        }
        try {
            Optional<Next> optionalNext = nextRepository.findBy_from__id(startTx.get_id());
            if (optionalNext.isPresent()) {
                Next next = optionalNext.get();
                Optional<Tx> optionalTx = Optional.ofNullable(next.get_to());
                if (optionalTx.isPresent()) {
                    Tx tx = optionalTx.get();
                    cytoscapeDataElementDtos.add(CytoscapeEdgeDto.builder()
                            .source(startTx.get_id())
                            .target(tx.get_id())
                            .type(next.getClass().getSimpleName())
                            .description(next)
                            .build());
                    cytoscapeDataElementDtos.addAll(getElementsRelatedWithTx(tx));
                }
            }
        } catch (MappingException ignored) {
        }
        return cytoscapeDataElementDtos;
    }
}
