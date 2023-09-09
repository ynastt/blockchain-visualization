package com.yarovikova.arangoviewerbackend.entity;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Rev;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
@Document("btcTx")
public class Tx {
    @Id
    private String _key;
    @ArangoId
    private String _id;
    @Rev
    private String _rev;
    private Long time;
}
