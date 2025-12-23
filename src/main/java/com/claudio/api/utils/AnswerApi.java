package com.claudio.api.utils;

import com.claudio.api.model.Product;
import lombok.Data;

import java.util.List;
import java.util.Optional;

public class AnswerApi {
    public String message;
    public List<Product> data;

    public AnswerApi() {
    }

    public AnswerApi(String message, List<Product> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
