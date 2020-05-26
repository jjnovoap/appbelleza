package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ServiceCategory;

import java.util.List;

public class RestaurantLoadEvent {

    private boolean success;
    private String message;
    private List<ServiceCategory> mServiceCategoryList;

    public RestaurantLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RestaurantLoadEvent(boolean success, List<ServiceCategory> serviceCategoryList) {
        this.success = success;
        mServiceCategoryList = serviceCategoryList;
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

    public List<ServiceCategory> getRestaurantList() {
        return mServiceCategoryList;
    }

    public void setRestaurantList(List<ServiceCategory> serviceCategoryList) {
        mServiceCategoryList = serviceCategoryList;
    }
}
