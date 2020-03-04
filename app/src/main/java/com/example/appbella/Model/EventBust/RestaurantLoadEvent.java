package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.CategoryProductOrServices;

import java.util.List;

public class RestaurantLoadEvent {

    private boolean success;
    private String message;
    private List<CategoryProductOrServices> mCategoryProductOrServicesList;

    public RestaurantLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RestaurantLoadEvent(boolean success, List<CategoryProductOrServices> categoryProductOrServicesList) {
        this.success = success;
        mCategoryProductOrServicesList = categoryProductOrServicesList;
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

    public List<CategoryProductOrServices> getRestaurantList() {
        return mCategoryProductOrServicesList;
    }

    public void setRestaurantList(List<CategoryProductOrServices> categoryProductOrServicesList) {
        mCategoryProductOrServicesList = categoryProductOrServicesList;
    }
}
