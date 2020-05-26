package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ServiceCategory;

public class ServiceSubcategoryEvent {

    private boolean success;
    private ServiceCategory services;

    public ServiceSubcategoryEvent(boolean success, ServiceCategory serviceCategory) {
        this.success = success;
        this.services = serviceCategory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ServiceCategory getServices() {
        return services;
    }

    public void setServices(ServiceCategory serviceCategory) {
        this.services = serviceCategory;
    }
}
