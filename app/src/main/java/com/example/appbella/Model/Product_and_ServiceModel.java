package com.example.appbella.Model;

import java.util.List;

public class Product_and_ServiceModel {
    private boolean success;
    private String message;
    private List<ProductOrService> result;

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

    public List<ProductOrService> getResult() {
        return result;
    }

    public void setResult(List<ProductOrService> result) {
        this.result = result;
    }
}
