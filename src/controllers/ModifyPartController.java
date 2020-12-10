package controllers;

import biz.Helper;
import classes.InHouse;
import classes.Inventory;
import classes.Outsourced;
import classes.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ModifyPartController {

    private Part part;
    private Scene scene;
    private final boolean inHouse;
    private final Inventory inventory;
    private final int partIndex;
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
    private TextField tfMIDCNAME;

    @FXML
    private RadioButton rbInHouse;

    @FXML
    private RadioButton rbOutsourced;

    @FXML
    private Label lblMIDCNAME;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblMinMaxError;

    /**
     * Modify Part constructor
     * @param part to modify
     * @param index of part to modify
     * @param inventory of parts/products
     */
    public ModifyPartController(Part part, int index, Inventory inventory){
        this.part = part;
        this.partIndex = index;
        this.inventory = inventory;

        // Check the part type.
        if(part instanceof InHouse){
            inHouse = true;
        }
        else {
            inHouse = false;
        }
    }

    /**
     * Called on controller initialization, sets initial data and binds events
     */
    public void initialize() {
        tfID.setText(String.valueOf(part.getId()));
        tfName.setText(part.getName());
        tfInv.setText(String.valueOf(part.getStock()));
        tfPriceCost.setText(String.valueOf(part.getPrice()));
        tfMax.setText(String.valueOf(part.getMax()));
        tfMin.setText(String.valueOf(part.getMin()));

        if(inHouse){
            rbInHouse.setSelected(true);
            lblMIDCNAME.setText("Machine ID");
            tfMIDCNAME.setText(String.valueOf(((InHouse) part).getMachineId()));
            tfMIDCNAME.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        }
        else{
            rbOutsourced.setSelected(true);
            lblMIDCNAME.setText("Company Name");
            tfMIDCNAME.setText(String.valueOf(((Outsourced) part).getCompanyName()));
            tfMIDCNAME.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.nonNumericFilter));
        }

        btnCancel.setOnAction((event -> {
            try {
                handleCancelButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Bind Save button action
        btnSave.setOnAction((event -> {
            try {
                handleSaveButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        rbInHouse.setOnAction((event -> {
            try {
                handleRadioButtonSelected(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        rbOutsourced.setOnAction((event -> {
            try {
                handleRadioButtonSelected(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

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
     * Sets the scene value to use
     * @param scene to set controller to reference
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    };

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
     * On radio button selected, determines which information to display based on Part instance type
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleRadioButtonSelected(ActionEvent event) throws Exception {
        if(rbInHouse.isSelected()){
            lblMIDCNAME.setText("Machine ID");
            tfMIDCNAME.clear();
            tfMIDCNAME.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.numericFilter));
        }
        else {
            lblMIDCNAME.setText("Company Name");
            tfMIDCNAME.clear();
            tfMIDCNAME.setTextFormatter(new TextFormatter<String>(Helper.TextHelper.nonNumericFilter));
        }
    }

    /**
     * On Save Button click, either displays error messages or saves the Part successfully.
     * @param event action event of button click
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleSaveButtonAction(ActionEvent event) throws Exception {
        try {
            StringBuilder errorList = new StringBuilder();
            if(tfName.getText().isEmpty() || tfName.getText() == null){
                errorList.append(" - Please enter a part name.\n");
            }

            if(tfInv.getText().isEmpty() || tfInv.getText() == null){
                errorList.append(" - Please enter part inventory.\n");
            }

            if(tfMin.getText().isEmpty() || tfMin.getText() == null){
                errorList.append(" - Please enter part minimum.\n");
            }

            if(tfMax.getText().isEmpty() || tfMax.getText() == null){
                errorList.append(" - Please enter part maximum.\n");
            }

            if(tfPriceCost.getText().isEmpty() || tfPriceCost.getText() == null){
                errorList.append(" - Please enter part price.\n");
            }

            if(tfMIDCNAME.getText().isEmpty() || tfMIDCNAME.getText() == null){
                {
                    if(rbInHouse.isSelected()) {
                        errorList.append(" - Please enter part machine Id.\n");
                    }
                    else {
                        errorList.append(" - Please enter part Company Name.\n");
                    }
                }
            }

            if(errorList.length() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Modify Part Warning");
                alert.setHeaderText("Please complete all fields.");
                alert.setContentText(errorList.toString());
                alert.showAndWait();
                return;
            }
            else {
                if(!invalidSaveState){
                    if(part instanceof InHouse && rbInHouse.isSelected()){
                        part.setName(tfName.getText());
                        part.setPrice(Double.parseDouble(tfPriceCost.getText()));
                        part.setMin(Integer.parseInt(tfMin.getText()));
                        part.setMax(Integer.parseInt(tfMax.getText()));
                        part.setStock(Integer.parseInt(tfInv.getText()));
                        ((InHouse)part).setMachineId(Integer.parseInt(tfMIDCNAME.getText()));
                    }
                    else if (part instanceof InHouse && rbOutsourced.isSelected()){

                        Outsourced newPart = new Outsourced(part.getId(),
                                tfName.getText(),
                                Double.parseDouble(tfPriceCost.getText()),
                                Integer.parseInt(tfInv.getText()),
                                Integer.parseInt(tfMin.getText()),
                                Integer.parseInt(tfMax.getText()),
                                tfMIDCNAME.getText());
                        inventory.updatePart(partIndex, newPart);
                    }
                    else if (part instanceof Outsourced && rbOutsourced.isSelected()){
                        part.setName(tfName.getText());
                        part.setPrice(Double.parseDouble(tfPriceCost.getText()));
                        part.setMin(Integer.parseInt(tfMin.getText()));
                        part.setMax(Integer.parseInt(tfMax.getText()));
                        part.setStock(Integer.parseInt(tfInv.getText()));
                        ((Outsourced)part).setCompanyName(tfMIDCNAME.getText());
                    }
                    else {
                        InHouse newPart = new InHouse(part.getId(),
                                tfName.getText(),
                                Double.parseDouble(tfPriceCost.getText()),
                                Integer.parseInt(tfInv.getText()),
                                Integer.parseInt(tfMin.getText()),
                                Integer.parseInt(tfMax.getText()),
                                Integer.parseInt(tfMIDCNAME.getText()));
                        inventory.updatePart(partIndex, newPart);
                    }
                    closeWindow();
                }
                else {
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
     * Gets the stage and closes the window.
     */
    private void closeWindow(){
        Stage stage = (Stage)scene.getWindow();
        stage.close();
    }
}
