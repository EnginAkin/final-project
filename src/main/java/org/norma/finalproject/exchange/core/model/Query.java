package org.norma.finalproject.exchange.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Query {

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;
}