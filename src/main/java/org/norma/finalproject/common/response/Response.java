package org.norma.finalproject.common.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public class Response<T>{

    public Response(boolean isSuccessful){
        this.isSuccess=isSuccessful;
    }
    public Response(List<T> elements){
        this.isSuccess=true;
        this.elements=elements;
    }

    private String requestEndpoint;
    private List<String> messages;
    private List<T> elements;
    private long size;
    private String dataType;
    private HttpStatus status;
    private boolean isSuccess;


}