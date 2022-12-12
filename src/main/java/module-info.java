module com.example.customcontrols {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.customcontrols to javafx.fxml;
    exports com.example.customcontrols;
}