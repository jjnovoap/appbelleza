package com.example.appbella.Interface;

import com.example.appbella.Model.Category;

import java.util.List;

public interface IProductCategoryLoadListener {
    void onProductCategoryLoadSuccess(List<Category> categoryList);
    void onProductCategoryLoadFailed(String message);
}
