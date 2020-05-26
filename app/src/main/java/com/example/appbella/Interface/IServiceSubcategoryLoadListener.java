package com.example.appbella.Interface;

import com.example.appbella.Model.ServiceSubcategory;

import java.util.List;

public interface IServiceSubcategoryLoadListener {
    void onServiceSubcategoryLoadSuccess(List<ServiceSubcategory> serviceSubcategoryList);
    void onServiceSubcategoryLoadFailed(String message);

}
