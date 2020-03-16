package com.example.appbella.Interface;

import com.example.appbella.Model.Favorite;

import java.util.List;

public interface IFavoriteLoadListener {

    void onFavoriteLoadSuccess(List<Favorite> favoriteList);
    void onFavoriteLoadFailed(String message);
}