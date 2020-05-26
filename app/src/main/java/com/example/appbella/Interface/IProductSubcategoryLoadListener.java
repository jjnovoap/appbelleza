package com.example.appbella.Interface;

import com.example.appbella.Model.ProductSubcategory;

import java.util.List;

public interface IProductSubcategoryLoadListener {
    void onProductSubcategoryLoadSuccess(List<ProductSubcategory> productSubcategoryList);
    void onProductSubcategoryLoadFailed(String message);
}
