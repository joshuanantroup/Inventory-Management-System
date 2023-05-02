package c482.inventorypa;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

/**
 * Controller class for ModifyProductScreen.
 *
 * @author Joshua Nantroup
 */

public class ModifyProductController implements Initializable {

    Product selectedProduct;
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    // Associated parts table view elements.
    @FXML private TableColumn<Part, Integer> assocPartIdColumn;
    @FXML private TableColumn<Part, Integer> assocPartInventoryLevel;
    @FXML private TableColumn<Part, String> assocPartNameColumn;
    @FXML private TableColumn<Part, Double> assocPartPriceColumn;
    @FXML private TableView<Part> assocPartTableView;

    // Parts table view elements.
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TextField partSearchText;
    @FXML private TableView<Part> partTableView;

    // Input fields for product.
    @FXML private TextField productIDText;
    @FXML private TextField productInventoryText;
    @FXML private TextField productMaxText;
    @FXML private TextField productMinText;
    @FXML private TextField productNameText;
    @FXML private TextField productPriceText;

    // Adds part to associated parts table.
    @FXML
    void addButtonClicked(ActionEvent event) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part not selected");
            alert.showAndWait();

        } else {
            assocParts.add(selectedPart);
            assocPartTableView.setItems(assocParts);
        }

    }

    // Cancel modify product and goes back to mains screen.
    @FXML
    void onCancelProductButtonClicked(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(event);
        }

    }

    // Saves the modified part.
    @FXML
    void onSaveProductButtonClicked(ActionEvent event) {

        try {
            int id = selectedProduct.getId();
            String name = productNameText.getText();
            Double price = Double.parseDouble(productPriceText.getText());
            int stock = Integer.parseInt(productInventoryText.getText());
            int min = Integer.parseInt(productMinText.getText());
            int max = Integer.parseInt(productMaxText.getText());

            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Name Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
            } else if (min <= 0 || min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max.");
                alert.showAndWait();
                // Validates the inventory level is within Min and Max range.
            } else if (stock < min || stock > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Inventory");
                alert.setContentText("Inventory must be a number equal to or between Min and Max.");
                alert.showAndWait();
            } else {

                    Product newProduct = new Product(id, name, price, stock, min, max);

                    for (Part part : assocParts) {
                        newProduct.addAssociatedPart(part);
                    }

                    Inventory.addProduct(newProduct);
                    Inventory.deleteProduct(selectedProduct);
                    returnToMainScreen(event);
                }
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
        }
    }

    // Searches for part.
    @FXML
    void partSearchKeyPressed(KeyEvent event) {

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

    // Removes part from associated parts
    @FXML
    void removeButtonClicked(ActionEvent event) {

        Part selectedPart = assocPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part not selected");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTableView.setItems(assocParts);
            }
        }

    }

    // Returns to Main screen.
    private void returnToMainScreen(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Initializes controller and populates text fields.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedProduct = MainScreenController.getProductToModify();
        assocParts = selectedProduct.getAllAssociatedParts();

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartTableView.setItems(assocParts);

        productIDText.setText(String.valueOf(selectedProduct.getId()));
        productNameText.setText(selectedProduct.getName());
        productInventoryText.setText(String.valueOf(selectedProduct.getStock()));
        productPriceText.setText(String.valueOf(selectedProduct.getPrice()));
        productMaxText.setText(String.valueOf(selectedProduct.getMax()));
        productMinText.setText(String.valueOf(selectedProduct.getMin()));

    }
}
