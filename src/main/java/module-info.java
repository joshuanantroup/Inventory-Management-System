module c482.inventorypa {
    requires javafx.controls;
    requires javafx.fxml;


    opens c482.inventorypa to javafx.fxml;
    exports c482.inventorypa;

    opens model to javafx.fxml;
    exports model;


}