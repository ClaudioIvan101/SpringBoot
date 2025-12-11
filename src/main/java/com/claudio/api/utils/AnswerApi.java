package com.claudio.api.utils;

import com.claudio.api.model.Product;

import java.util.List;

public class AnswerApi {
    public String message;
    public List<Product> data;

    public AnswerApi(String message, List<Product> data) {
        this.message = message;
        this.data = data;
    }
}
