/**
 *
 * @author Holden Johnson
 */

package ui;

import classes.InHouse;
import classes.Inventory;
import classes.Product;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     *
     * @param primaryStage main stage to load application
     * @throws Exception if unable to load application primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Initialize inventory
        Inventory inv = new Inventory();
        inv.addPart(new InHouse(1,"Charger", 10.99, 100, 1, 1000, 1));
        inv.addPart(new InHouse(2,"Cable", 4.99, 500, 0, 999, 2));
        inv.addProduct(new Product(1,"Router", 199.99, 4, 1, 10));
        inv.addProduct(new Product(2,"Computer", 599.99, 15, 1, 15));

        // Initialize main controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        MainController controller = new MainController(inv);
        loader.setController(controller);

        // Set up scene to display
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
