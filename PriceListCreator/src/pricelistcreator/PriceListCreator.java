/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Gonzalo
 */
public class PriceListCreator extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/pricelistcreator/GUI/MainFXML.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Price List Creator (Mediterranea Carghas)");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tag.png")));
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
