package com.yarovikova.arangoviewerbackend.entity;

import com.arangodb.springframework.annotation.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
@Edge("btcOut")
public class Out {
    @Id
    private String _key;
    @ArangoId
    private String _id;
    @Rev
    private String _rev;
    @From
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Tx _from;
    @To
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Address _to;
    private Long outIndex;
    private Long spentBtc;
    private Long time;
}
