module com.example.demoforfx {
    requires javafx.controls; // Includes javafx.graphics
    requires javafx.fxml;

    requires transitive javafx.graphics;

    opens com.example to javafx.fxml; // Allow FXML to access the com.example package
    exports com.example; // Export the com.example package to other modules
}