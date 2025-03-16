package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private SimpleStringProperty product;
    private SimpleStringProperty unit;
    private SimpleDoubleProperty pricePerUnit;
    private SimpleDoubleProperty purchasedQuantity;

    public Item(String product, String unit, double pricePerUnit) {
        this.product = new SimpleStringProperty(product);
        this.unit = new SimpleStringProperty(unit);
        this.pricePerUnit = new SimpleDoubleProperty(pricePerUnit);
        this.purchasedQuantity = new SimpleDoubleProperty(0);
    }

    public String getProduct() { return product.get(); }
    public String getUnit() { return unit.get(); }
    public double getPricePerUnit() { return pricePerUnit.get(); }
    public double getPurchasedQuantity() { return purchasedQuantity.get(); }

    public void setPurchasedQuantity(double purchasedQuantity) {
        this.purchasedQuantity.set(purchasedQuantity);
    }

    public SimpleStringProperty productProperty() { return product; }
    public SimpleStringProperty unitProperty() { return unit; }
    public SimpleDoubleProperty pricePerUnitProperty() { return pricePerUnit; }
    public SimpleDoubleProperty purchasedQuantityProperty() { return purchasedQuantity; }

    public double getTotalPrice() {
        return getPricePerUnit() * getPurchasedQuantity();
    }

    @Override
    public String toString() {
        return getProduct();
    }
}