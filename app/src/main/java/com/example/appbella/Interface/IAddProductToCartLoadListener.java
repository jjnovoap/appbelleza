package com.example.appbella.Interface;

import com.example.appbella.Model.AddToCart;
import com.example.appbella.Model.Product;

import java.util.List;

public interface IAddProductToCartLoadListener {
    void onAddProductToCartLoadSuccess(List<AddToCart> addToCartList);
    void onAddProductToCartLoadFailed(String message);
}
