package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Category;

import java.util.List;

public class RestaurantLoadEvent {

    private boolean success;
    private String message;
    private List<Category> mCategoryList;

    public RestaurantLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RestaurantLoadEvent(boolean success, List<Category> categoryList) {
        this.success = success;
        mCategoryList = categoryList;
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

    public List<Category> getRestaurantList() {
        return mCategoryList;
    }

    public void setRestaurantList(List<Category> categoryList) {
        mCategoryList = categoryList;
    }
}
