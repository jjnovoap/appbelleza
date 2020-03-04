package com.example.appbella.Interface;


import com.example.appbella.Model.Product_and_Service;

import java.util.List;

public interface IServicesOrProductLoadListener {
    void onServicesOProductLoadSuccess(List<Product_and_Service> servicesOrProductList);
    void onServicesOProductLoadFailed(String message);
}
