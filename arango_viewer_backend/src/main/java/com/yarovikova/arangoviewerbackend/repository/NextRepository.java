package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.Next;

import java.util.Optional;

public interface NextRepository extends ArangoRepository<Next, String> {
    Optional<Next> findBy_from__id(String txId);
}