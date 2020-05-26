package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ProductSubcategory;

public class ProductsListEvent {

    private boolean success;
    private ProductSubcategory productSubcategory;

    public ProductsListEvent(boolean success, ProductSubcategory productSubcategory) {
        this.success = success;
        this.productSubcategory = productSubcategory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ProductSubcategory getProductSubcategory() {
        return productSubcategory;
    }

    public void setProductSubcategory(ProductSubcategory productSubcategory) {
        this.productSubcategory = productSubcategory;
    }
}
