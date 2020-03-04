package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Product_and_Service;

public class FoodDetailEvent {
    private boolean success;
    private Product_and_Service food;

    public FoodDetailEvent(boolean success, Product_and_Service food) {
        this.success = success;
        this.food = food;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Product_and_Service getFood() {
        return food;
    }

    public void setFood(Product_and_Service food) {
        this.food = food;
    }
}
