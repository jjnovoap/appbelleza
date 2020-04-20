package com.example.appbella.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {
    // We only load Cart by Restaurant Id
    // Because each restaurant id will have order receipt different
    // Because each restaurant have different link payment, so we can't make 1 cart for all
    @Query("SELECT * FROM Cart WHERE userPhone=:userPhone")
    Flowable<List<CartItem>> getAllCart(String userPhone);

    @Query("SELECT COUNT(*) FROM Cart WHERE userPhone=:userPhone")
    Single<Integer> countItemInCart(String userPhone);

    @Query("SELECT SUM(productPrice*productQuantity) FROM Cart WHERE userPhone=:userPhone")
    Single<Long> sumPrice(String userPhone);

    @Query("SELECT SUM(productQuantity) FROM Cart WHERE userPhone=:userPhone")
    Single<Long> sumQuantity(String userPhone);

    @Query("SELECT * FROM Cart WHERE  productId=:productId AND categoryId=:categoryId AND userPhone=:userPhone")
    Flowable<CartItem> getItemInCart(String productId, String categoryId,String userPhone);

    // If conflict foodId, we will update information
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem... cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCart(CartItem cart);

    @Delete
    Single<Integer> deleteCart(CartItem cart);

    @Query("DELETE FROM Cart WHERE userPhone=:userPhone")
    Single<Integer> cleanCart(String userPhone);
}
