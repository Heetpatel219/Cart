package com.example;

import java.io.Serializable;
import java.util.List;

public class CartData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // Name of the cart
    private List<Item> items;
    private double totalPrice;

    public CartData(String name, List<Item> items, double totalPrice) {
        this.name = name;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return name + " - Total Price: $" + String.format("%.2f", totalPrice);
    }
}