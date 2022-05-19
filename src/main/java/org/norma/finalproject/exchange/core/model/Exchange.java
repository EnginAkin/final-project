package org.norma.finalproject.exchange.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Exchange {

    @JsonProperty("date")
    private String date;

    @JsonProperty("result")
    private double result;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("query")
    private Query query;

    @JsonProperty("historical")
    private String historical;

    @JsonProperty("info")
    private Info info;
}