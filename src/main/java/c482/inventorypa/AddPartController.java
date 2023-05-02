package c482.inventorypa;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
import model.Inventory;
import model.InHouse;
import model.Outsourced;

/**
 * Controller class that provides control logic for the add part screen of the application.
 *
 * @author Joshua Nantroup
 */
public class AddPartController implements Initializable {

    @FXML private RadioButton inHouse;
    @FXML private RadioButton outsourced;
    @FXML private TextField partIDText;
    @FXML private Label machineIDCompanyLabel; // Label for Machine ID or Company Name depending on radio button selection.
    @FXML private TextField machineIdCompanyText; // Text for Machine ID or Company Name.
    @FXML private TextField partInventoryText;
    @FXML private TextField partMaxText;
    @FXML private TextField partMinText;
    @FXML private TextField partNameText;
    @FXML private TextField partPriceText;
    @FXML private ToggleGroup tgPartType; // Toggle Group for radio buttons.
    @FXML // Sets label to Machine ID.
    void inHouseSelected(ActionEvent event) {
        machineIDCompanyLabel.setText("Machine ID");
    }
    @FXML  // Sets label to Company Name.
    void outsourcedSelected(ActionEvent event)  {
        machineIDCompanyLabel.setText("Company Name");
    }
    @FXML // Displays confirmation of cancel and loads MainScreen.
    void onCancelPartButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Cancel changes and return to main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            returnToMainScreen(event);
        }
    }
    @FXML  //  Adds new part to inventory and returns to MainScreenController. Validates all fields entered by user.
    void onSavePartButton(ActionEvent event) {

        try {
            int id = 0;
            String name = partNameText.getText();
            Double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

            // Validates that name is filled in.
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Name");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                // Validates min > 0 and min < max.
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
                            machineId = Integer.parseInt(machineIdCompanyText.getText());
                            InHouse newInhousePart = new InHouse(id, name, price, stock, min, max, machineId);
                            newInhousePart.setId(Inventory.getNewPartId());
                            Inventory.addPart(newInhousePart);
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
                        companyName = machineIdCompanyText.getText();
                        Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                        newOutsourcedPart.setId(Inventory.getNewPartId());
                        Inventory.addPart(newOutsourcedPart);
                        partAddSuccessful = true;
                    }

                    if (partAddSuccessful) {
                        returnToMainScreen(event);
                    }
                }
            } catch(Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
        }
    }

    // Loads MainScreenController.
    private void returnToMainScreen(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Initializes controller and sets in-house radio button to true.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inHouse.setSelected(true);
    }
}
