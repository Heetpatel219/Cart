package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;

public class Controller {
    @FXML
    private ComboBox<Item> itemsComboBox;

    @FXML
    private Slider quantitySlider;

    @FXML
    private Label unitLabel;

    @FXML
    private Label pricePerUnitLabel;

    @FXML
    private TableView<Item> cartTableView;

    @FXML
    private TableColumn<Item, String> productColumn;

    @FXML
    private TableColumn<Item, Double> purchasedUnitColumn;

    @FXML
    private TableColumn<Item, Double> totalPriceColumn;

    @FXML
    private Label totalPriceLabel;

    private ObservableList<Item> items = FXCollections.observableArrayList(
        new Item("Milk", "gallon", 1.00),
        new Item("Eggs", "dozen", 2.50),
        new Item("Bread", "loaf", 3.25),
        new Item("Butter", "ounce", 2.00),
        new Item("Cereal", "ounce", 4.00)
    );

    private ObservableList<Item> cartItems = FXCollections.observableArrayList();

    public void initialize() {
        itemsComboBox.setItems(items);

        quantitySlider.setMin(0);
        quantitySlider.setMax(10);
        quantitySlider.setValue(0);
        quantitySlider.setMajorTickUnit(1);
        quantitySlider.setMinorTickCount(0);
        quantitySlider.setShowTickLabels(true);
        quantitySlider.setShowTickMarks(true);
        quantitySlider.setSnapToTicks(true);

        itemsComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                unitLabel.setText("Unit: " + newVal.getUnit());
                pricePerUnitLabel.setText("Price per Unit: $" + newVal.getPricePerUnit());
            } else {
                unitLabel.setText("Unit: N/A");
                pricePerUnitLabel.setText("Price per Unit: $0.00");
            }
        });

        productColumn.setCellValueFactory(cellData -> cellData.getValue().productProperty());
        purchasedUnitColumn.setCellValueFactory(cellData -> cellData.getValue().purchasedQuantityProperty().asObject());
        totalPriceColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() ->
            cellData.getValue().getTotalPrice(), cellData.getValue().purchasedQuantityProperty()
        ));

        cartTableView.setItems(cartItems);

        NumberBinding totalBinding = Bindings.createDoubleBinding(() ->
            cartItems.stream().mapToDouble(Item::getTotalPrice).sum(), cartItems
        );
        totalPriceLabel.textProperty().bind(Bindings.format("Total Price: $%.2f", totalBinding));
    }

    @FXML
    private void handleAddToCart() {
        Item selectedItem = itemsComboBox.getValue();
        if (selectedItem != null) {
            double quantity = quantitySlider.getValue();
            if (quantity > 0) {
                Item cartItem = new Item(selectedItem.getProduct(), selectedItem.getUnit(), selectedItem.getPricePerUnit());
                cartItem.setPurchasedQuantity(quantity);
                cartItems.add(cartItem);
            }
        }
    }

    @FXML
    private void handleRemoveFromCart() {
        Item selectedCartItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedCartItem != null) {
            cartItems.remove(selectedCartItem);
        }
    }

    @FXML
    private void handleSaveCart() {
        // Convert ObservableList to ArrayList for serialization
        List<Item> cartItemsList = new ArrayList<>(cartTableView.getItems());

        // Calculate the total price of the cart
        double totalPrice = cartItemsList.stream().mapToDouble(Item::getTotalPrice).sum();

        // Create a Cart object with the current items and total price
        CartData cart = new CartData(cartItemsList, totalPrice);

        // Save the cart to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_cart.ser"))) {
            oos.writeObject(cart); // Serialize and save the Cart object
            System.out.println("Cart saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save cart.");
        }
    }

    @FXML
    private void handleLoadCart() {
        // Load the cart from the file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_cart.ser"))) {
            CartData savedCart = (CartData) ois.readObject();

            // Clear the current cart and add the saved items
            cartItems.clear();
            cartItems.addAll(savedCart.getItems());

            // Update the total price label
            totalPriceLabel.setText(String.format("Total Price: $%.2f", savedCart.getTotalPrice()));
            System.out.println("Cart loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load cart.");
        }
    }

    // Inner class for serialization
    public static class CartData implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<Item> items;
        private double totalPrice;

        public CartData(List<Item> items, double totalPrice) {
            this.items = new ArrayList<>(items);
            this.totalPrice = totalPrice;
        }

        public List<Item> getItems() {
            return items;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}