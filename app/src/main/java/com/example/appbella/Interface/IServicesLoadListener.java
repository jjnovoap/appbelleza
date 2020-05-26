package com.example.appbella.Interface;


import com.example.appbella.Model.Service;

import java.util.List;

public interface IServicesLoadListener {
    void onServicesLoadSuccess(List<Service> servicesList);
    void onServicesLoadFailed(String message);
}
