package com.example.appbella.Interface;

import com.example.appbella.Model.Category;

import java.util.List;

public interface ISpecificCategoryLoadListener {
    void onCategoryLoadSuccess(List<Category> categoryList);
    void onCategoryLoadFailed(String message);
}
