package com.natasha.foodsaver.common;

public class ResponseWrapper<T> {

    private boolean success; // Indikerar om anropet var framgångsrikt
    private String message; // Eventuellt meddelande (t.ex. fel eller notis)
    private T data; // Dataobjektet som returneras (kan vara valfritt)

    public ResponseWrapper(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new ResponseWrapper<>(true, message, data);
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(false, message, null);
    }
}
