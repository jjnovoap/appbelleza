package com.example.appbella.Model;

import java.util.List;

public class RestaurantModel {

    private boolean success;
    private String message;
    private List<CategoryProductOrServices> result;

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

    public List<CategoryProductOrServices> getResult() {
        return result;
    }

    public void setResult(List<CategoryProductOrServices> result) {
        this.result = result;
    }
}
