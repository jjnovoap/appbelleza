package com.example.appbella.Model;

import java.util.List;

public class FoodModel {
    private boolean success;
    private String message;
    private List<Product_and_Service> result;

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

    public List<Product_and_Service> getResult() {
        return result;
    }

    public void setResult(List<Product_and_Service> result) {
        this.result = result;
    }
}
