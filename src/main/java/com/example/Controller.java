package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
}