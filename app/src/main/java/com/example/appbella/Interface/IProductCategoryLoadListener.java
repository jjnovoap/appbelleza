package com.example.appbella.Interface;

import com.example.appbella.Model.Subcategory;

import java.util.List;

public interface IProductCategoryLoadListener {
    void onProductCategoryLoadSuccess(List<Subcategory> subcategoryList);
    void onProductCategoryLoadFailed(String message);
}
