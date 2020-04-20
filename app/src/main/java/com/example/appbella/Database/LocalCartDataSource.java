package com.example.appbella.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {

    private CartDAO mCartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        mCartDAO = cartDAO;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String userPhone) {
        return mCartDAO.getAllCart(userPhone);
    }

    @Override
    public Single<Integer> countItemInCart(String userPhone) {
        return mCartDAO.countItemInCart(userPhone);
    }

    @Override
    public Single<Long> sumPrice(String userPhone) {
        return mCartDAO.sumPrice(userPhone);
    }

    @Override
    public Single<Long> sumQuantity(String userPhone) {
        return mCartDAO.sumQuantity(userPhone);
    }

    @Override
    public Flowable<CartItem> getItemInCart(String productId, String categoryId,String userPhone) {
        return mCartDAO.getItemInCart(productId,categoryId,userPhone);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return mCartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCart(CartItem cart) {
        return mCartDAO.updateCart(cart);
    }

    @Override
    public Single<Integer> deleteCart(CartItem cart) {
        return mCartDAO.deleteCart(cart);
    }

    @Override
    public Single<Integer> cleanCart(String userPhone) {
        return mCartDAO.cleanCart(userPhone);
    }
}
