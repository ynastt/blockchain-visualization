package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.Tx;

public interface TxRepository extends ArangoRepository<Tx, String> {
}