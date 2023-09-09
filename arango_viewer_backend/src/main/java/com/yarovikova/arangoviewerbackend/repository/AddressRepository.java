package com.yarovikova.arangoviewerbackend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.yarovikova.arangoviewerbackend.entity.Address;

public interface AddressRepository extends ArangoRepository<Address, String> {
}