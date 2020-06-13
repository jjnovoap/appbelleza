package com.example.appbella.Interface;

import android.view.View;

public interface IFoodDetailOrCartClickListener {
    void onFoodItemClickListener(View view, int position, boolean isDetail, boolean isDelete, boolean isAdd);
}
