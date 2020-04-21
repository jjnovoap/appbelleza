package com.example.appbella.Interface;

import com.example.appbella.Model.Subcategory;

import java.util.List;

public interface ISubcategoryLoadListener {
    void onSubcategoryLoadSuccess(List<Subcategory> subcategoryList);
    void onSubcategoryLoadFailed(String message);

}
