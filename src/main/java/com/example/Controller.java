package com.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

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
    private List<CartData> savedCarts = new ArrayList<>(); // List to store saved carts

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
        // Prompt the user to enter a name for the cart
        TextInputDialog dialog = new TextInputDialog("Cart Name");
        dialog.setTitle("Save Cart");
        dialog.setHeaderText("Enter a name for the cart:");
        dialog.setContentText("Name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                // Save the cart with the given name
                List<Item> cartItemsList = new ArrayList<>(cartTableView.getItems());
                double totalPrice = cartItemsList.stream().mapToDouble(Item::getTotalPrice).sum();
                CartData cart = new CartData(name, cartItemsList, totalPrice);
                savedCarts.add(cart);

                // Clear the current cart
                cartItems.clear();

                // Save the list of saved carts to a file
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_carts.ser"))) {
                    oos.writeObject(savedCarts);
                    System.out.println("Cart saved successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to save cart.");
                }
            }
        });
    }

    @FXML
    private void handleLoadCart() {
        // Load the list of saved carts from the file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_carts.ser"))) {
            savedCarts = (List<CartData>) ois.readObject();

            // Show a pop-up window with the list of saved carts
            showSavedCartsPopup();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load carts.");
        }
    }

    private void showSavedCartsPopup() {
        // Create a dialog to display the saved carts
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Saved Carts");
        dialog.setHeaderText("Select a cart to load:");

        // Create a ListView to display the saved carts
        ListView<String> listView = new ListView<>();
        for (CartData cart : savedCarts) {
            listView.getItems().add(cart.toString());
        }

        // Add the ListView to the dialog
        dialog.getDialogPane().setContent(listView);

        // Add a button to load the selected cart
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // Handle the OK button action
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String selectedCart = listView.getSelectionModel().getSelectedItem();
                if (selectedCart != null) {
                    // Find the selected cart and load its items
                    for (CartData cart : savedCarts) {
                        if (cart.toString().equals(selectedCart)) {
                            cartItems.clear();
                            cartItems.addAll(cart.getItems());
                            break;
                        }
                    }
                }
            }
            return null;
        });

        // Show the dialog
        dialog.showAndWait();
    }
}