package com.example.appbella.Model;

public class Order {
    private String orderId;
    private int orderStatus;
    private int restaurantId;
    private String orderPhone;
    private String orderName;
    private String orderAddress;
    private String transactionId;
    private String orderDate;
    private boolean cod;
    private Double totalPrice;
    private int numOfItem;

    public Order() {
    }

    public Order(String orderId, int orderStatus, int restaurantId, String orderPhone, String orderName, String orderAddress, String transactionId, String orderDate, boolean cod, Double totalPrice, int numOfItem) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.restaurantId = restaurantId;
        this.orderPhone = orderPhone;
        this.orderName = orderName;
        this.orderAddress = orderAddress;
        this.transactionId = transactionId;
        this.orderDate = orderDate;
        this.cod = cod;
        this.totalPrice = totalPrice;
        this.numOfItem = numOfItem;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumOfItem() {
        return numOfItem;
    }

    public void setNumOfItem(int numOfItem) {
        this.numOfItem = numOfItem;
    }
}
