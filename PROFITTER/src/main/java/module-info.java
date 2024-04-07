module com.example.profitter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.profitter to javafx.fxml;
    exports com.example.profitter;
}