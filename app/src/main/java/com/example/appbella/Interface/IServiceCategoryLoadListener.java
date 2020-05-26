package com.example.appbella.Interface;

import com.example.appbella.Model.ServiceCategory;

import java.util.List;

public interface IServiceCategoryLoadListener {
    void onCategoriesLoadSuccess(List<ServiceCategory> serviceCategoryList);
    void onCategoriesLoadFailed(String message);

}
