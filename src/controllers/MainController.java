package controllers;

import biz.Helper;
import classes.Inventory;
import classes.Part;
import classes.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class MainController {

    private final Inventory inventory;

    @FXML
    private TextField tfSearchPart;

    @FXML
    private TextField tfSearchProduct;

    @FXML
    private TableView tvPart;

    @FXML
    private TableView tvProduct;

    @FXML
    private Button btnModify;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModifyProduct;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Button btnDeleteProduct;

    @FXML
    private Button btnExit;

    @FXML
    private TableColumn tcPartId;

    @FXML
    private TableColumn tcProductId;

    /**
     * Main controller constructor
     * @param inventory root object
     */
    public MainController(Inventory inventory)
    {
        this.inventory = inventory;
    }

    /**
     * On controller load, bind events and set intitial values
     */
    public void initialize() {
        // Initialize Table Views
        tvPart.setItems(inventory.getAllParts());
        tvProduct.setItems(inventory.getAllProducts());
        tvPart.setPlaceholder(Helper.TextHelper.getNoResultsLabel());
        tvProduct.setPlaceholder(Helper.TextHelper.getNoResultsLabel());

        // Add Part Button Action Binding
        btnAdd.setOnAction((event -> {
            try {
                handleAddPartButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Modify Part Button Action Binding
        btnModify.setOnAction(event -> {
            try {
                handleModifyPartButtonAction(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Delete Part Button Action Binding
        btnDelete.setOnAction(event -> {
            try {
                handleDeletePartButtonAction(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add Product Button Action Binding
        btnAddProduct.setOnAction((event -> {
            try {
                handleAddProductButtonAction(event);
            } catch (Exception e){
                e.printStackTrace();
            }
        }));

        // Modify Product Button Action Binding
        btnModifyProduct.setOnAction(event -> {
            try {
                handleModifyProductButtonAction(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Delete Product Button Action Binding
        btnDeleteProduct.setOnAction(event -> {
            try {
                handleDeleteProductButtonAction(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Exit Button Action Binding
        btnExit.setOnAction(event -> {
            try {
                handleExitButtonAction(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Part Search Binding
        tfSearchPart.setOnKeyReleased(keyEvent -> {
            try {
                handleSearchPartAction(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Product Search Binding
        tfSearchProduct.setOnKeyReleased(keyEvent -> {
            try {
                handleSearchProductAction(keyEvent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
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
                tvPart.setItems(inventory.getAllParts());
            }
            else{
                tvPart.setItems(inventory.getAllParts()
                        .stream()
                        .filter(part -> part.getName().toLowerCase().contains(tfSearchPart.getText().toLowerCase())
                                || Integer.toString(part.getId()).contains(tfSearchPart.getText()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Search TF Key Up, updates the product table view with matching results
     * <b> This is a good area that we could improve functionality in a future release. We could add more
     * search options such as inventory amount and price, as well as have a backend SQL database
     * to actually store the product/part information between sessions rather than have it revert each time.</b>
     * @param event action event of lifting up a key press in the text field
     * @throws Exception possible Exception thrown
     */
    @FXML
    protected void handleSearchProductAction(KeyEvent event) throws Exception {
        try {
            if(tfSearchProduct.getText().isEmpty() || tfSearchProduct.getText() == null){
                tvProduct.setItems(inventory.getAllProducts());
            }
            else{
                tvProduct.setItems(inventory.getAllProducts()
                        .stream()
                        .filter(product -> product.getName().toLowerCase().contains(tfSearchProduct.getText().toLowerCase())
                                || Integer.toString(product.getId()).contains(tfSearchProduct.getText()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Add button click, opens a new Add Part Form Window
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleAddPartButtonAction(ActionEvent event) throws Exception {
        try {
            // Load AddPartForm
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddPartForm.fxml"));
            AddPartController controller = new AddPartController(inventory);
            loader.setController(controller);

            // Set new window stage
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            controller.setScene(scene);
            stage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Modify button click, opens a new Modify Part Form Window, or displays an error message
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleModifyPartButtonAction(ActionEvent event) throws Exception {
        try {
            // Get selected part
            Part selectedPart = (Part)tvPart.getSelectionModel().getSelectedItem();
            if(selectedPart != null){
                // Load ModifyPartForm
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ModifyPartForm.fxml"));
                int index = tvPart.getItems().indexOf(selectedPart);
                ModifyPartController controller = new ModifyPartController(selectedPart, index, inventory);
                loader.setController(controller);

                // Set new window stage
                Stage stage = new Stage();
                stage.setTitle("Modify Part");
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                controller.setScene(scene);
                stage.show();

                // Bind window closing event
                stage.setOnHiding(windowEvent -> {
                    tvPart.refresh();
                });
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select a Part to modify.");
                alert.show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Delete button click, confirms deletion then deletes part or displays error message
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleDeletePartButtonAction(ActionEvent event) throws Exception {
        // Get selected part
        Part selectedPart = (Part)tvPart.getSelectionModel().getSelectedItem();

        // Delete selected part
        if(selectedPart != null){
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Delete " + selectedPart.getName() + "?",
                        ButtonType.YES,
                        ButtonType.NO,
                        ButtonType.CANCEL);

                confirmationAlert.showAndWait();
                if(confirmationAlert.getResult() == ButtonType.YES){
                    inventory.deletePart(selectedPart);
                }
                else {
                    return;
                }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a Part to delete.");
            alert.show();
        }

    }

    /**
     * On Add button click, opens a new Add Product Form Window
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleAddProductButtonAction(ActionEvent event) throws Exception {
        try {
            // Load AddPartForm
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddProductForm.fxml"));
            AddProductController controller = new AddProductController(inventory);
            loader.setController(controller);

            // Set new window stage
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            controller.setScene(scene);
            stage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Modify button click, opens a new Modify Product Form Window, or displays an error message
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleModifyProductButtonAction(ActionEvent event) throws Exception {
        try {
            // Get selected part
            Product selectedProduct = (Product)tvProduct.getSelectionModel().getSelectedItem();

            if(selectedProduct != null){
                // Load ModifyPartForm
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ModifyProductForm.fxml"));
                ModifyProductController controller = new ModifyProductController(inventory, selectedProduct);
                loader.setController(controller);

                // Set new window stage
                Stage stage = new Stage();
                stage.setTitle("Modify Product");
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                controller.setScene(scene);
                stage.show();

                // Bind window closing event
                stage.setOnHiding(windowEvent -> {
                    tvProduct.refresh();
                });
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select a Product to modify.");
                alert.show();
            }
        }
            catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * On Delete button click, confirms deletion then deletes product or displays error message
     * @param event action event when button is clicked
     * @throws Exception if unable to load the scene
     */
    @FXML
    protected void handleDeleteProductButtonAction(ActionEvent event) throws Exception {
        // Get selected part
        Product selectedProduct = (Product)tvProduct.getSelectionModel().getSelectedItem();

        // Delete selected part
        if(selectedProduct != null)
        {
            if(selectedProduct.getAllAssociatedParts().size() > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You must first remove associated parts from product before deleting it.");
                alert.show();
            }
            else {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Delete " + selectedProduct.getName() + "?",
                        ButtonType.YES,
                        ButtonType.NO,
                        ButtonType.CANCEL);

                confirmationAlert.showAndWait();
                if(confirmationAlert.getResult() == ButtonType.YES){
                    inventory.deleteProduct(selectedProduct);
                }
                else {
                    return;
                }
            }
        }
            else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a Product to delete.");
            alert.show();
        }
    }

    /**
     * Closes application
     * @param event Exit Button Click Event
     * @throws Exception throws if cannot perform exit function
     */
    @FXML
    protected void handleExitButtonAction(ActionEvent event) throws Exception {
        Platform.exit();
    }
}
