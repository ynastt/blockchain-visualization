package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.ParentBlock;

import java.util.Optional;

public interface ParentBlockRepository extends ArangoRepository<ParentBlock, String> {
    Optional<ParentBlock> findBy_from__id(String txId);
}