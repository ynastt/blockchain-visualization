package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.In;

import java.util.Optional;

public interface InRepository extends ArangoRepository<In, String> {
    Optional<In> findBy_to__id(String txId);
}