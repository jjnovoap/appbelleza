package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ProductOrService;

public class FoodDetailEvent {
    private boolean success;
    private ProductOrService food;

    public FoodDetailEvent(boolean success, ProductOrService food) {
        this.success = success;
        this.food = food;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ProductOrService getFood() {
        return food;
    }

    public void setFood(ProductOrService food) {
        this.food = food;
    }
}
