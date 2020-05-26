package com.example.appbella.Model.EventBust;

import com.example.appbella.Model.Service;

public class ServiceDetailEvent {
    private boolean success;
    private Service service;

    public ServiceDetailEvent(boolean success, Service service) {
        this.success = success;
        this.service = service;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
