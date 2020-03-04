package com.example.appbella.Interface;

import com.example.appbella.Model.CategoryProductOrServices;

import java.util.List;

public interface IGeneralCategoriesLoadListener {
    void onCategoriesLoadSuccess(List<CategoryProductOrServices> categoryProductOrServicesList);
    void onCategoriesLoadFailed(String message);

}
