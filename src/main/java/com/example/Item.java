package com.example;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    // Underlying data fields (serializable)
    private String product;
    private String unit;
    private double pricePerUnit;
    private double purchasedQuantity;

    // Transient fields for JavaFX properties (not serializable)
    private transient StringProperty productProperty;
    private transient StringProperty unitProperty;
    private transient DoubleProperty pricePerUnitProperty;
    private transient DoubleProperty purchasedQuantityProperty;

    public Item(String product, String unit, double pricePerUnit) {
        this.product = product;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.purchasedQuantity = 0; // Default value
        initProperties(); // Initialize JavaFX properties
    }

    // Initialize JavaFX properties
    private void initProperties() {
        productProperty = new SimpleStringProperty(product);
        unitProperty = new SimpleStringProperty(unit);
        pricePerUnitProperty = new SimpleDoubleProperty(pricePerUnit);
        purchasedQuantityProperty = new SimpleDoubleProperty(purchasedQuantity);
    }

    // Getters for JavaFX properties
    public StringProperty productProperty() {
        return productProperty;
    }

    public StringProperty unitProperty() {
        return unitProperty;
    }

    public DoubleProperty pricePerUnitProperty() {
        return pricePerUnitProperty;
    }

    public DoubleProperty purchasedQuantityProperty() {
        return purchasedQuantityProperty;
    }

    // Regular getters and setters
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
        if (productProperty != null) {
            productProperty.set(product);
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        if (unitProperty != null) {
            unitProperty.set(unit);
        }
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        if (pricePerUnitProperty != null) {
            pricePerUnitProperty.set(pricePerUnit);
        }
    }

    public double getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(double purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
        if (purchasedQuantityProperty != null) {
            purchasedQuantityProperty.set(purchasedQuantity);
        }
    }

    // Calculate total price
    public double getTotalPrice() {
        return pricePerUnit * purchasedQuantity;
    }

    // Custom deserialization logic
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize non-transient fields
        initProperties(); // Reinitialize JavaFX properties
    }

    @Override
    public String toString() {
        return getProduct();
    }
}