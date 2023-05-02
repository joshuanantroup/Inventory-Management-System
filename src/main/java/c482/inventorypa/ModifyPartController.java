package c482.inventorypa;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

/**
 *  Controller Class for ModifyProduct Screen.
 *
 * @author Joshua Nantroup
 */

public class ModifyPartController implements Initializable {
    Part selectedPart;
    @FXML private RadioButton inHouse;
    @FXML private RadioButton outsourced;
    @FXML private TextField partIDText;
    @FXML private Label partIdNameLabel;
    @FXML private TextField partIdNameText;
    @FXML private TextField partInventoryText;
    @FXML private TextField partMaxText;
    @FXML private TextField partMinText;
    @FXML private TextField partNameText;
    @FXML private TextField partPriceText;

    @FXML // sets label to "Machine ID" if InHouse selected
    void inHouseSelected(ActionEvent event) {
        partIdNameLabel.setText("Machine ID");
    }
    @FXML // sets label to "Company Name" if outsourced selected
    void outsourcedSelected(ActionEvent event) {
        partIdNameLabel.setText("Company Name");
    }
    @FXML // Cancels modify part and goes back to main screen.
    void onCancelPartButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            returnToMainScreen(event);
        }
    }
    @FXML // saves modified part
    void onSavePartButton(ActionEvent event) throws IOException {

        try {
            int id = selectedPart.getId();
            String name = partNameText.getText();
            Double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

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

                if (inHouse.isSelected()) {
                    try {
                        machineId = Integer.parseInt(partIdNameText.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                        Inventory.addPart(newInHousePart);
                        partAddSuccessful = true;
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid value for Machine ID");
                        alert.setContentText("Machine ID may only contain numbers.");
                        alert.showAndWait();
                    }
                }

                if (outsourced.isSelected()) {
                    companyName = partIdNameText.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max,
                            companyName);
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccessful = true;
                }

                if (partAddSuccessful) {
                    Inventory.deletePart(selectedPart);
                    returnToMainScreen(event);
                }
            }
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields or invalid values.");
            alert.showAndWait();
        }
    }


    // Loads MainScreen.
    private void returnToMainScreen(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Initializes controller and loads selected part
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedPart = MainScreenController.getPartToModify();

        if (selectedPart instanceof InHouse) {
            inHouse.setSelected(true);
            partIdNameLabel.setText("Machine ID");
            partIdNameText.setText(String.valueOf(((InHouse)selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced){
            outsourced.setSelected(true);
            partIdNameLabel.setText("Company Name");
            partIdNameText.setText(((Outsourced)selectedPart).getCompanyName());
        }

        partIDText.setText(String.valueOf(selectedPart.getId()));
        partNameText.setText(selectedPart.getName());
        partInventoryText.setText(String.valueOf(selectedPart.getStock()));
        partPriceText.setText(String.valueOf(selectedPart.getPrice()));
        partMaxText.setText(String.valueOf(selectedPart.getMax()));
        partMinText.setText(String.valueOf(selectedPart.getMin()));
    }
}
