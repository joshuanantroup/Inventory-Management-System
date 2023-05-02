package c482.inventorypa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.util.Objects;

/**
 * The Inventory Management program lets the user manage inventory
 * of parts and products.
 *
 * @author Joshua Nantroup
 *
 */
public class Main extends Application {

    // The start method loads the MainScreen scene.
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // Entry point of the application and adds sample data to inventory.
    public static void main(String[] args) {

        // Add sample parts
        int partId = Inventory.getNewPartId();
        InHouse tvScreen = new InHouse(partId, "TV Screen", 300.00, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        InHouse tvShell = new InHouse(partId, "TV Shell", 100.00, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        InHouse powerCord = new InHouse(partId, "Power Cord", 2.99, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        Outsourced remote = new Outsourced(partId, "Remote Control", 29.99, 50, 30, 100, "Panasonic");
        Inventory.addPart(tvScreen);
        Inventory.addPart(tvShell);
        Inventory.addPart(powerCord);
        Inventory.addPart(remote);

        // Add sample product
        int productId = Inventory.getNewProductId();
        Product television = new Product(productId, "LCD Television", 499.99, 20, 20, 100);
        television.addAssociatedPart(tvScreen);
        television.addAssociatedPart(tvShell);
        television.addAssociatedPart(powerCord);
        television.addAssociatedPart(remote);
        Inventory.addProduct(television);

        launch(args);
    }
}
