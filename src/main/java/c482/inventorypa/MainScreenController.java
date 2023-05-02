package c482.inventorypa;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

/**
 * Controller class that provides logic for main screen of the application.
 *
 * @author Joshua Nantroup
 *
 */


public class MainScreenController implements Initializable {

    //part object selected in partTableView
    private static Part partToModify;

    // product object selected in productTableView.
    private static Product productToModify;

    // Part Table Properties
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, Double> partCost;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TextField partSearchText;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableView<Part> partTableView;

    // Product Table Properties
    @FXML private TableColumn<Product, Integer> productId;
    @FXML private TableColumn<Product, Double> productCost;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TextField productSearchText;
    @FXML private TableColumn<Product, Integer> productStock;
    @FXML private TableView<Product> productTableView;

    // Gets the part from the part partTableView
    public static Part getPartToModify() {
        return partToModify;
    }
    // Gets the product from product partTableView
    public static Product getProductToModify() {
        return productToModify;
    }


    // Add Part
    @FXML
    void partAddButtonClicked(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("AddPartScreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    // Add Product
    @FXML
    void productAddButtonClicked(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    // Deletes Selected Part
    @FXML
    void partDeleteButtonClicked(ActionEvent event) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText("Part not selected");
            alertError.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }
    }

    // Deletes Selected Product
    @FXML
    void productDeleteButtonClicked(ActionEvent event) {

        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText("Product not selected");
            alertError.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();

                if (assocParts.size() >= 1) {

                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setTitle("Error");
                    alertError.setHeaderText("Parts Associated");
                    alertError.setContentText("All parts must be removed from product before deletion.");
                    alertError.showAndWait();

                } else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }

    }

    // Exits the Program
    @FXML
    void programExitButtonClicked(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Modify the selected part by loading the ModifyPartController.
     *
     * Runtime error would occur if user clicked modify part button without selecting part
     * in the form of null exception error. This is mitigated with an if statement checking
     * for null status and throwing an alert to the user.
     *
     * @param event Modify Part button action.
     */
    @FXML
    void partModifyButtonClicked(ActionEvent event) throws IOException {

        partToModify = partTableView.getSelectionModel().getSelectedItem();
        if (partToModify == null) {

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText("Part not selected");
            alertError.showAndWait();

        } else {

            Parent parent = FXMLLoader.load(getClass().getResource("ModifyPartScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Modify selected product by loading the ModifyProductController.
     *
     * Runtime error would occur if user clicked modify part button without selecting product
     * in the form of null exception error. This is mitigated with an if statement checking
     * for null status and throwing an alert to the user.
     *
     * @param event Modify Product Button Action.
     */
    @FXML
    void productModifyButtonClicked(ActionEvent event) throws IOException {

        productToModify = productTableView.getSelectionModel().getSelectedItem();
        if (productToModify == null) {

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText("Product not selected");
            alertError.showAndWait();

        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("ModifyProductScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


    }

    // Search for part
    @FXML
    void onPartSearchKeyPressed(KeyEvent event)  {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchText.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partTableView.setItems(partsFound);

        if (partsFound.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Part not found");
            alert.showAndWait();

        }

    }

    // Search for product.
    @FXML
    void onProductSearchKeyPressed(KeyEvent event) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchText.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }

        productTableView.setItems(productsFound);

        if (productsFound.size() == 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Product not found");
            alert.showAndWait();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Populate parts partTableView view
        partTableView.setItems(Inventory.getAllParts());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populate products partTableView view
        productTableView.setItems(Inventory.getAllProducts());
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
