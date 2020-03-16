package com.example.appbella.Model;

public class Favorite {

    private String fbid;
    private String restaurantName;
    private String foodName;
    private String foodImage;
    private String status;
    private int foodId;
    private int restaurantId;
    private Double price;

    public Favorite() {
    }

    public Favorite(String fbid, String restaurantName, String foodName, String foodImage, String status, int foodId, int restaurantId, Double price) {
        this.fbid = fbid;
        this.restaurantName = restaurantName;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.status = status;
        this.foodId = foodId;
        this.restaurantId = restaurantId;
        this.price = price;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


