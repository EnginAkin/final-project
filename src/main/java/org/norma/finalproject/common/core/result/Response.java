package org.norma.finalproject.common.core.result;

import org.springframework.http.HttpStatus;

import java.util.List;

public class Response<T> {

    private String requestEndpoint;
    private List<String> messages;
    private List<T> elements;
    private long size;
    private String dataType;
    private HttpStatus status;
    private boolean isSuccess;
    public Response(boolean isSuccessful) {
        this.isSuccess = isSuccessful;
    }
    public Response(List<T> elements) {
        this.isSuccess = true;
        this.elements = elements;
    }


}