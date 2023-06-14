//03
package com.thomas.tulisaja;

import com.google.gson.annotations.SerializedName;


public class ValueData<T> {
    @SerializedName("success")
    private int success;
    @SerializedName("message")
    private String message;

    private T data;

    public int getSuccessNah() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
