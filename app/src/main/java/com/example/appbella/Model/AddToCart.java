package com.example.appbella.Model;

public class AddToCart {

    private String id;
    private String categoryId;
    private String key;
    private String name;
    private String image;
    private Long price;
    private Long productQuantity;
    private Long productExtraPrice;
    private boolean isAddon;
    private Long discount;
    private String status;

    public AddToCart() {
    }

    public AddToCart(String id, String categoryId, String key, String name, String image, Long price, Long productQuantity, Long productExtraPrice, boolean isAddon, Long discount, String status) {
        this.id = id;
        this.categoryId = categoryId;
        this.key = key;
        this.name = name;
        this.image = image;
        this.price = price;
        this.productQuantity = productQuantity;
        this.productExtraPrice = productExtraPrice;
        this.isAddon = isAddon;
        this.discount = discount;
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public boolean isAddon() {
        return isAddon;
    }

    public void setAddon(boolean addon) {
        isAddon = addon;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getProductExtraPrice() {
        return productExtraPrice;
    }

    public void setProductExtraPrice(Long productExtraPrice) {
        this.productExtraPrice = productExtraPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
