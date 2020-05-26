package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ProductCategory;

public class ProductSubcategoryEvent {
    private boolean success;
    private ProductCategory product;

    public ProductSubcategoryEvent(boolean success, ProductCategory productCategory) {
        this.success = success;
        this.product = productCategory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ProductCategory getProduct() {
        return product;
    }

    public void setProduct(ProductCategory product) {
        this.product = product;
    }
}
