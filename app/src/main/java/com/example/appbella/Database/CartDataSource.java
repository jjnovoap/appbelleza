package com.example.appbella.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {

    Flowable<List<CartItem>> getAllCart(String userPhone);

    Single<Integer> countItemInCart(String userPhone);

    Single<Long> sumPrice(String userPhone);

    Single<Long> sumQuantity(String userPhone);

    Flowable<CartItem> getItemInCart(String productId,String categoryId,String userPhone);

    Completable insertOrReplaceAll(CartItem... cartItems);

    Single<Integer> updateCart(CartItem cart);

    Single<Integer> deleteCart(CartItem cart);

    Single<Integer> cleanCart(String userPhone);

}
