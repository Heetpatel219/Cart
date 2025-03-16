package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Model {
    private ObservableList<Item> items;

    public Model() {
        items = FXCollections.observableArrayList();
        loadData();
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    private void loadData() {
        String csvFilePath = "com/example/ItemsMaster.csv";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFilePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.out.println("CSV file not found at: " + csvFilePath); // Debug statement
                return;
            }
    
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String product = data[0].trim();
                String unit = data[1].trim();
                double quantity = Double.parseDouble(data[2].trim());
                double pricePerUnit = Double.parseDouble(data[3].trim());
                items.add(new Item(product, unit, quantity));
                System.out.println("Loaded item: " + product + ", " + unit + ", " + quantity + ", " + pricePerUnit); // Debug statement
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage()); // Debug statement
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("CSV file not found or path is incorrect!"); // Debug statement
            e.printStackTrace();
        }
    }
}