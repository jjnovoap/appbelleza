package com.example.appbella.Model.EventBust;

public class CalculatePriceEvent {
    private long totalPrice;

    public CalculatePriceEvent(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
