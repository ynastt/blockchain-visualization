package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.Block;

public interface BlockRepository extends ArangoRepository<Block, String> {
}