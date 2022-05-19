package org.norma.finalproject.exchange.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Info {

    @JsonProperty("rate")
    private double rate;

    @JsonProperty("timestamp")
    private int timestamp;
}