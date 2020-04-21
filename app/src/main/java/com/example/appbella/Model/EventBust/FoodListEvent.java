package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Subcategory;

public class FoodListEvent {

    private boolean success;
    private Subcategory subcategory;

    public FoodListEvent(boolean success, Subcategory subcategory) {
        this.success = success;
        this.subcategory = subcategory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
