package com.example.appbella.Interface;

import com.example.appbella.Model.Order;

import java.util.List;

public interface IOrderLoadListener {
    void onOrderLoadSuccess(List<Order> orders);
    void onOrderLoadFailed(String message);
}
