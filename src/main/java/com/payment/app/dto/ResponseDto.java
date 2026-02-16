package com.payment.app.dto;

public class ResponseDto<T> {

    private T data;
    private String error;

    public ResponseDto() {
    }

    public ResponseDto(T data) {
        this.data = data;
    }

    public ResponseDto(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
