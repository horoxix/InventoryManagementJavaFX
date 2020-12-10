package controllers;

import biz.Helper;
import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class ModifyProductController {

    private final Product product;
    private final Inventory inventory;
    private Scene scene;
    private boolean invalidSaveState;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfInv;

    @FXML
    private TextField tfPriceCost;

    @FXML
    private TextField tfMax;

    @FXML
    private TextField tfMin;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private TableView tvAvailableParts;

    @FXML
    private TableView tvAssignedParts;

    @FXML
    private TextField tfSearchPart;

    @FXML
    private Label lblMinMaxError;

    /**
     *
     * @param inventory inventory of parts/products
     * @param product to modify
     */
    public ModifyProductController(Inventory inventory, Product product)
    {
        this.inventory = inventory;
        this.product = product;
    }

    /**
     * Called on controller initialization, sets initial data and binds events
     */
    public void initialize() {
        // Initialize text data
        tfID.setText(String.valueOf(product.getId()));
        tfName.setText(product.getName());
        tfInv.setText(String.valueOf(product.getStock()));
        tfPriceCost.setText(String.valueOf(product.getPrice()));
        tfMax.setText(String.valueOf(product.getMax()));
        tfMin.setText(String.valueOf(product.getMin()));

        tvAvailableParts.setPlaceholder(Helper.TextHelper.getNoResultsLabel());
        tvAssignedParts.setPlaceholder(new Label("No Associated Parts."));

        // Get Associated parts
        ObservableList<Part> associatedParts = product.getAllAssociatedParts();

        // Initialize part tables
        if(associatedParts != null){
            updateAvailableParts();
        }
        else {
            tvAvailableParts.setItems(inventory.getAllParts());
        }

        tvAssignedParts.setItems(product.getAllAssociatedParts());

        // Add Part Action Binding
        btnAdd.setOnAction((event -> {
            try {
                handleAddButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Remove Part Action Binding
        btnRemove.setOnAction((event -> {
            try {
                handleRemoveButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Save Button Action Binding
        btnSave.setOnAction((event -> {
            try {
                handleSaveButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Cancel Button Action Binding
        btnCancel.setOnAction((event -> {
            try {
                handleCancelButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Part Search Binding
        tfSearchPart.setOnKeyReleased(keyEvent -> {
            try {
                handleSearchPartAction(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Min < Max Binding
        tfMin.setOnKeyReleased(keyEvent -> {
            try {
                handleMinMaxEndKey(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Max > Min Binding
        tfMax.setOnKeyReleased(keyEvent -> {
            try {
                handleMinMaxEndKey(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Inv Binding
        tfInv.setOnKeyReleased(keyEvent -> {
            try {
                handleInvEndKey(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Text Formatter Binding
        tfName.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.nonNumericFilter));
        tfID.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        tfInv.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        tfMin.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        tfMax.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        tfPriceCost.setTextFormatter(new TextFormatter<>(Helper.TextHelper.decimalFilter));
    }

    /**
     * On Search TF Key Up, updates the part table view with matching results
     * @param event action event of lifting up a key press in the text field
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleSearchPartAction(KeyEvent event) throws Exception {
        try {
            if(tfSearchPart.getText().isEmpty() || tfSearchPart.getText() == null){
                updateAvailableParts();
            }
            else{
                tvAvailableParts.setItems(inventory.getAllParts()
                        .stream()
                        .filter(part -> (part.getName().toLowerCase().contains(tfSearchPart.getText().toLowerCase())
                                || Integer.toString(part.getId()).contains(tfSearchPart.getText()))
                                && !product.getAllAssociatedParts().contains(part))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Min/Max TF Key Up, determines if an error message needs to display
     * @param event action event of lifting up a key press in the text field
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleMinMaxEndKey(KeyEvent event) throws Exception {
        try {
            if(!tfMin.getText().isEmpty() && !tfMax.getText().isEmpty()){
                if(Integer.parseInt(tfMin.getText()) > Integer.parseInt(tfMax.getText())){
                    invalidSaveState = true;
                    lblMinMaxError.setVisible(true);
                    lblMinMaxError.setTextFill(Color.web("red"));
                    lblMinMaxError.setText("Min must be less than Max.");
                }
                else {
                    if(!tfInv.getText().isEmpty()){
                        validateInventory();
                    }
                    else {
                        invalidSaveState = false;
                        lblMinMaxError.setVisible(false);
                        lblMinMaxError.setText("");
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Inv TF Key Up, determines if an error message needs to display
     * @param event action event of lifting up a key press in the text field
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleInvEndKey(KeyEvent event) throws Exception {
        try {
            validateInventory();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Validates if inventory is within min/max boundaries
     */
    private void validateInventory(){
        if(!tfInv.getText().isEmpty() && !tfMax.getText().isEmpty() && !tfMin.getText().isEmpty()){
            if(Integer.parseInt(tfInv.getText()) > Integer.parseInt(tfMax.getText())
                    || Integer.parseInt(tfInv.getText()) < Integer.parseInt(tfMin.getText())){
                invalidSaveState = true;
                lblMinMaxError.setVisible(true);
                lblMinMaxError.setTextFill(Color.web("red"));
                lblMinMaxError.setText("Inventory must be with the Min/Max boundaries.");
            }
            else {
                invalidSaveState = false;
                lblMinMaxError.setVisible(false);
                lblMinMaxError.setText("");
            }
        }
    }

    /**
     * On Add Button Click, adds the selected part to the associated part list and tableview
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleAddButtonAction(ActionEvent event) throws Exception {
        try {
            Part selectedPart = (Part)tvAvailableParts.getSelectionModel().getSelectedItem();
            if(selectedPart != null && product.getAllAssociatedParts().stream().noneMatch(part -> part.getId() == selectedPart.getId())){
                product.addAssociatedPart(selectedPart);
                tvAssignedParts.setItems(product.getAllAssociatedParts());
                updateAvailableParts();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a Part to add.");
                alert.show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Remove Button Click, confirms removal then removes from associated list
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleRemoveButtonAction(ActionEvent event) throws Exception {
        try {
            Part selectedPart = (Part)tvAssignedParts.getSelectionModel().getSelectedItem();
            if(selectedPart != null && product.getAllAssociatedParts().stream().anyMatch(part -> part.getId() == selectedPart.getId())){
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Remove " + selectedPart.getName() + "?",
                        ButtonType.YES,
                        ButtonType.NO,
                        ButtonType.CANCEL);

                confirmationAlert.showAndWait();
                if(confirmationAlert.getResult() == ButtonType.YES){
                    product.deleteAssociatedPart(selectedPart);
                    tvAssignedParts.setItems(product.getAllAssociatedParts());
                    updateAvailableParts();
                }
                else {
                    return;
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a Part to remove.");
                alert.show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Save Button Click, verifies all data is valid, then either displays error message or saves product
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleSaveButtonAction(ActionEvent event) throws Exception {
        try {
            StringBuilder errorList = new StringBuilder();
            if(tfName.getText().isEmpty() || tfName.getText() == null){
                errorList.append(" - Please enter a product name.\n");
            }

            if(tfInv.getText().isEmpty() || tfInv.getText() == null){
                errorList.append(" - Please enter product inventory.\n");
            }

            if(tfMin.getText().isEmpty() || tfMin.getText() == null){
                errorList.append(" - Please enter product minimum.\n");
            }

            if(tfMax.getText().isEmpty() || tfMax.getText() == null){
                errorList.append(" - Please enter product maximum.\n");
            }

            if(tfPriceCost.getText().isEmpty() || tfPriceCost.getText() == null){
                errorList.append(" - Please enter product price.\n");
            }

            if(errorList.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Modify Product Warning");
                alert.setHeaderText("Please complete all fields.");
                alert.setContentText(errorList.toString());
                alert.showAndWait();
                return;
            }
            else {
                if (!invalidSaveState) {
                    product.setName(tfName.getText());
                    product.setPrice(Double.parseDouble(tfPriceCost.getText()));
                    product.setMin(Integer.parseInt(tfMin.getText()));
                    product.setMax(Integer.parseInt(tfMax.getText()));
                    product.setStock(Integer.parseInt(tfInv.getText()));
                    closeWindow();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Modify Product Warning");
                    alert.setHeaderText("Please resolve any error messages.");
                    alert.setContentText(errorList.toString());
                    alert.showAndWait();
                    return;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On cancel button click, closes window
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleCancelButtonAction(ActionEvent event) throws Exception {
        closeWindow();
    }

    /**
     * Sets the scene value to use
     * @param scene to set controller to reference
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    };

    /**
     * Updates available part list based on associated parts
     */
    protected void updateAvailableParts(){
        tvAvailableParts.setItems(
                inventory.getAllParts()
                        .stream()
                        .filter(part -> !product.getAllAssociatedParts().contains(part))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        tvAvailableParts.refresh();
    }

    /**
     * Gets the stage and closes the window.
     */
    private void closeWindow(){
        Stage stage = (Stage)scene.getWindow();
        stage.close();
    }
}
