package com.example.appbella.Interface;

import com.example.appbella.Model.Category;

import java.util.List;

public interface ICategoryLoadListener {
    void onCategoriesLoadSuccess(List<Category> categoryList);
    void onCategoriesLoadFailed(String message);

}
