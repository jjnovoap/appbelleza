package com.example.appbella.Interface;


import com.example.appbella.Model.ProductOrService;

import java.util.List;

public interface IServicesOrProductLoadListener {
    void onServicesOProductLoadSuccess(List<ProductOrService> servicesOrProductList);
    void onServicesOProductLoadFailed(String message);
}
