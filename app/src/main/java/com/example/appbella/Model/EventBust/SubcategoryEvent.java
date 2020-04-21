package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Category;

public class SubcategoryEvent {

    private boolean success;
    private Category productOrServices;

    public SubcategoryEvent(boolean success, Category category) {
        this.success = success;
        this.productOrServices = category;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Category getProductOrServices() {
        return productOrServices;
    }

    public void setProductOrServices(Category productOrServices) {
        this.productOrServices = productOrServices;
    }
}
