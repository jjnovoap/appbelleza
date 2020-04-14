package com.example.appbella.Interface;

import com.example.appbella.Model.Category;

import java.util.List;

public interface IservicesCategoryLoadListener {
    void onServiceCategoryLoadSuccess(List<Category> categoryList);
    void onServiceCategoryLoadFailed(String message);

}
