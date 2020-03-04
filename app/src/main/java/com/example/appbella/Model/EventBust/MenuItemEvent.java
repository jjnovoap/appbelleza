package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.CategoryProductOrServices;

public class MenuItemEvent {

    private boolean success;
    private CategoryProductOrServices productOrServices;

    public MenuItemEvent(boolean success, CategoryProductOrServices categoryProductOrServices) {
        this.success = success;
        this.productOrServices = categoryProductOrServices;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CategoryProductOrServices getProductOrServices() {
        return productOrServices;
    }

    public void setProductOrServices(CategoryProductOrServices productOrServices) {
        this.productOrServices = productOrServices;
    }
}
