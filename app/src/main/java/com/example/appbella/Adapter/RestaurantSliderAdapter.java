package com.example.appbella.Adapter;

import com.example.appbella.Model.CategoryProductOrServices;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class RestaurantSliderAdapter extends SliderAdapter {

    private List<CategoryProductOrServices> mCategoryProductOrServicesList;

    public RestaurantSliderAdapter(List<CategoryProductOrServices> categoryProductOrServicesList) {
        this.mCategoryProductOrServicesList = categoryProductOrServicesList;
    }

    @Override
    public int getItemCount() {
        return mCategoryProductOrServicesList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(mCategoryProductOrServicesList.get(position).getImage());
    }
}
