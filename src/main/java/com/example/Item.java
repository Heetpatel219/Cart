package com.example;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    // Use JavaFX properties for binding
    private final StringProperty product = new SimpleStringProperty();
    private final StringProperty unit = new SimpleStringProperty();
    private final DoubleProperty pricePerUnit = new SimpleDoubleProperty();
    private final DoubleProperty purchasedQuantity = new SimpleDoubleProperty();

    public Item(String product, String unit, double pricePerUnit) {
        this.product.set(product);
        this.unit.set(unit);
        this.pricePerUnit.set(pricePerUnit);
    }

    // Getters for properties
    public StringProperty productProperty() {
        return product;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public DoubleProperty pricePerUnitProperty() {
        return pricePerUnit;
    }

    public DoubleProperty purchasedQuantityProperty() {
        return purchasedQuantity;
    }

    // Regular getters and setters
    public String getProduct() {
        return product.get();
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public double getPricePerUnit() {
        return pricePerUnit.get();
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit.set(pricePerUnit);
    }

    public double getPurchasedQuantity() {
        return purchasedQuantity.get();
    }

    public void setPurchasedQuantity(double purchasedQuantity) {
        this.purchasedQuantity.set(purchasedQuantity);
    }

    // Calculate total price
    public double getTotalPrice() {
        return pricePerUnit.get() * purchasedQuantity.get();
    }

    @Override
    public String toString() {
        return getProduct();
    }
}