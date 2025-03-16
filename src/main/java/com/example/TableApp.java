package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TableApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("table_view.fxml"));
        Scene scene = new Scene(loader.load());

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Grocery Store - Table View");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}