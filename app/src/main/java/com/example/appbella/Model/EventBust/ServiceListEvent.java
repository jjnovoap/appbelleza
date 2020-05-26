package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.ServiceSubcategory;

public class ServiceListEvent {

    private boolean success;
    private ServiceSubcategory serviceSubcategory;

    public ServiceListEvent(boolean success, ServiceSubcategory serviceSubcategory) {
        this.success = success;
        this.serviceSubcategory = serviceSubcategory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ServiceSubcategory getServiceSubcategory() {
        return serviceSubcategory;
    }

    public void setServiceSubcategory(ServiceSubcategory serviceSubcategory) {
        this.serviceSubcategory = serviceSubcategory;
    }
}
