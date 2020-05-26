package com.example.appbella.Interface;

import com.example.appbella.Model.ProductCategory;
import com.example.appbella.Model.ServiceCategory;

import java.util.List;

public interface IProductCategoryLoadListener {
    void onProductCategoriesLoadSuccess(List<ProductCategory> productCategoryList);
    void onProductCategoriesLoadFailed(String message);
}
