package com.example.appbella.Model;

import java.util.List;

public class MenuModel {

    private boolean success;
    private String message;
    private List<Subcategory> result;

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

    public List<Subcategory> getResult() {
        return result;
    }

    public void setResult(List<Subcategory> result) {
        this.result = result;
    }
}
