package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.Out;

import java.util.Optional;

public interface OutRepository extends ArangoRepository<Out, String> {
    Optional<Out> findBy_from__id(String txId);
}