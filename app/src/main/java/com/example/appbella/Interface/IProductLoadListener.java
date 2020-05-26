package com.example.appbella.Interface;

import com.example.appbella.Model.Product;

import java.util.List;

public interface IProductLoadListener {
    void onProductLoadSuccess(List<Product> productList);
    void onProductLoadFailed(String message);
}
