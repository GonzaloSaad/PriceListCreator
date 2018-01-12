/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pricelistcreator.common.CommonString;
import pricelistcreator.common.ServiceType;
import pricelistcreator.creator.DirectoryCreator;
import pricelistcreator.creator.DirectoryNotCreatedException;
import pricelistcreator.creator.DomesticOutputCreator;

import pricelistcreator.creator.ImpoExpoOutputCreator;
import pricelistcreator.creator.OutputCreator;

/**
 *
 * @author Gonzalo
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    private GridPane gridPane;

    private File saveFile;
    private File readFile;
    private File priceFile;
    private final int AMOUNT_VALUES = 15;

    private final String[] TYPE_VALUES = new String[]{CommonString.DOM, CommonString.EXPO, CommonString.IMPO};
    private ServiceType SERVICE_TYPE = ServiceType.DOM;

    @FXML
    private ComboBox<Integer> cmb_amount;
    @FXML
    private ComboBox<String> cmb_type;
    @FXML
    private AnchorPane innerAnchorPane;
    @FXML
    private Button btn_generate;
    @FXML
    private TextField txb_readPath;
    @FXML
    private TextField txb_savePath;
    @FXML
    private TextField txb_pricePath;
    @FXML
    private Button btn_priceSearch;
    @FXML
    private Button btn_baseSearch;
    @FXML
    private Button btn_saveSearch;
    @FXML
    private CheckBox chb_individuals;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setComboBoxOptions();

    }

    @FXML
    private void buttonBaseSearch(ActionEvent event) {
        chooseBase();
    }

    @FXML
    private void buttonSaveSearch(ActionEvent event) {
        chooseSave();
    }

    @FXML
    private void buttonPriceSearch(ActionEvent event) {
        choosePrice();
    }

    @FXML
    private void buttonGenerateClick(ActionEvent event) {
        try {
            buildFiles();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void comboBoxAmountEntered(ActionEvent event) {

        gridPane = createGridPane(cmb_amount.getValue());

        innerAnchorPane.getChildren().clear();
        innerAnchorPane.getChildren().add(gridPane);

        anchorPane.setPrefHeight(innerAnchorPane.getPrefHeight() + 200);
    }

    @FXML
    private void comboBoxTypeEntered(ActionEvent event) {
        if (cmb_type.getValue().equals(CommonString.EXPO)) {
            SERVICE_TYPE = ServiceType.EXPO;
        } else if (cmb_type.getValue().equals(CommonString.IMPO)) {
            SERVICE_TYPE = ServiceType.IMPO;
        } else {
            SERVICE_TYPE = ServiceType.DOM;
        }
    }

    private void chooseBase() {
        File read = readFile();

        if (read == null) {
            return;
        }

        readFile = read;
        setReadPathText(read.getAbsolutePath());

    }

    private void choosePrice() {
        File price = readFile();

        if (price == null) {
            return;
        }

        priceFile = price;
        setPricePathText(price.getAbsolutePath());
    }

    private void chooseSave() {
        File save = readDirectory();

        if (save == null) {
            return;
        }

        saveFile = save;
        setSavePathText(save.getAbsolutePath());
    }

    private File readFile() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fc.getExtensionFilters().add(extFilter);

        File file = fc.showOpenDialog((Stage) anchorPane.getScene().getWindow());
        return file;

    }

    private File readDirectory() {
        DirectoryChooser dc = new DirectoryChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");

        File file = dc.showDialog((Stage) anchorPane.getScene().getWindow());
        return file;
    }

    private void setReadPathText(String path) {
        this.txb_readPath.setText(path);
    }

    private void setPricePathText(String path) {
        this.txb_pricePath.setText(path);
    }

    private void setSavePathText(String path) {
        this.txb_savePath.setText(path);
    }

    private void setComboBoxOptions() {

        ObservableList value;

        ArrayList<Integer> arrayValues = new ArrayList<>();
        for (int i = 1; i < AMOUNT_VALUES; i++) {
            arrayValues.add(i);
        }
        value = FXCollections.observableArrayList(arrayValues);
        cmb_amount.setItems(value);
        cmb_amount.getSelectionModel().selectFirst();

        value = FXCollections.observableArrayList(TYPE_VALUES);
        cmb_type.setItems(value);
        cmb_type.getSelectionModel().selectFirst();
    }

    private GridPane createGridPane(int elements) {
        GridPane gp = new GridPane();

        // Setting the headers.
        gp.add(new Label(""), 0, 0);

        Label baseLabel = new Label("Fijo");
        baseLabel.setAlignment(Pos.CENTER);
        gp.add(baseLabel, 1, 0);

        Label porcLabel = new Label("Porcentaje");
        porcLabel.setAlignment(Pos.CENTER);

        gp.add(porcLabel, 2, 0);

        // Creating the body.
        for (int i = 1; i < elements + 1; i++) {
            gp.add(new Label("Tarifa " + i + ":\t"), 0, i);

            TextField tx1 = new TextField();
            tx1.setPrefWidth(60);

            TextField tx2 = new TextField();
            tx2.setPrefWidth(60);

            gp.add(tx1, 1, i);
            gp.add(tx2, 2, i);
        }

        return gp;
    }

    private void buildFiles() throws IOException {

        int cant = cmb_amount.getValue();
        final int FIX_COL = 1;
        final int PER_COL = 2;
        final int COL_COUNT = 3;
        double[] fixes = new double[cant];
        double[] percentages = new double[cant];

        TextField tf;
        TextField tp;

        for (int i = 0; i < cant; i++) {

            tf = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + FIX_COL);
            fixes[i] = Double.parseDouble(tf.getText().length() == 0 ? "0" : tf.getText());
            tp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + PER_COL);
            percentages[i] = Double.parseDouble(tp.getText().length() == 0 ? "0" : tp.getText());

        }

        /**
         * Create directories.
         */
        
        DirectoryCreator dc = new DirectoryCreator(saveFile,priceFile,cant,chb_individuals.isSelected(),SERVICE_TYPE);
        
        
        
        
        
        OutputCreator oc;

        if (SERVICE_TYPE == ServiceType.EXPO || SERVICE_TYPE == ServiceType.IMPO) {
            oc = new ImpoExpoOutputCreator();
        } else {
            oc = new DomesticOutputCreator();
        }

        oc.setCreator(readFile, dc.getDestinyFile(), dc.getOuputDir(), fixes, percentages);
        oc.write(chb_individuals.isSelected());
    }

}
