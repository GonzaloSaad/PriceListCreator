/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.GUI;

import java.awt.Desktop;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pricelistcreator.GUI.validator.FileValidator;
import pricelistcreator.GUI.validator.exceptions.InvalidBaseFileException;
import pricelistcreator.GUI.validator.exceptions.InvalidPriceFileException;
import pricelistcreator.common.CommonString;
import pricelistcreator.common.ServiceType;
import pricelistcreator.creator.DirectoryCreator;
import pricelistcreator.creator.DomesticOutputCreator;

import pricelistcreator.creator.ImpoExpoOutputCreator;

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
    private File lastFile;
    private final int AMOUNT_DOM = 4;
    private final int AMOUNT_IMPOEXPO = 12;

    private final String[] TYPE_VALUES = new String[]{CommonString.DOM, CommonString.EXPO, CommonString.IMPO};
    private ServiceType SERVICE_TYPE = ServiceType.DOM;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private File lastValidDirectory;

    @FXML
    private ComboBox<Integer> cmb_amount;
    @FXML
    private ComboBox<String> cmb_type;
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
    @FXML
    private AnchorPane innerAnchorPaneGrid;
    @FXML
    private AnchorPane innerAnchorPaneTitle;


    /*
    Events
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTypeComboBox();
        setAmountComboBox();
        createGridPane(cmb_amount.getValue());

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

        if (!alertConfirmation()) {
            return;
        }

        if (inputOk()) {

            try {

                buildFiles();
                if (alertFinish()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(lastFile);
                }
            } catch (Exception ex) {
                alertUnknownError(ex.getMessage());
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void comboBoxAmountEntered(ActionEvent event) {
        int cant = 0;
        try {
            cant = cmb_amount.getSelectionModel().getSelectedItem();
        } catch (NullPointerException ex) {
            // No idea why throws exception buy stills works! ¿?¿?¿?
        }

        createGridPane(cant);

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

        setAmountComboBox();
        createGridPane(cmb_amount.getValue());

    }

    /*
    Directory and File management.
     */
    private void chooseBase() {
        File read = readFile();

        if (read == null) {
            return;
        }

        readFile = read;
        lastValidDirectory = new File(read.getParent());
        setReadPathText(read.getAbsolutePath());

    }

    private void choosePrice() {
        File price = readFile();

        if (price == null) {
            return;
        }

        priceFile = price;
        lastValidDirectory = new File(price.getParent());
        setPricePathText(price.getAbsolutePath());
    }

    private void chooseSave() {
        File save = readDirectory();

        if (save == null) {
            return;
        }

        saveFile = save;
        lastValidDirectory = save;
        setSavePathText(save.getAbsolutePath());
    }

    private File readFile() {
        FileChooser fc = getFileChooser();
        File file = fc.showOpenDialog((Stage) anchorPane.getScene().getWindow());
        return file;

    }

    private File readDirectory() {
        DirectoryChooser dc = getDirectoryChooser();
        File file = dc.showDialog((Stage) anchorPane.getScene().getWindow());
        return file;
    }

    /*
    Controller's setters.
     */
    private void setReadPathText(String path) {
        this.txb_readPath.setText(path);
    }

    private void setPricePathText(String path) {
        this.txb_pricePath.setText(path);
    }

    private void setSavePathText(String path) {
        this.txb_savePath.setText(path);
    }

    private void setAmountComboBox() {

        ObservableList value;
        int amount;

        if (SERVICE_TYPE == ServiceType.DOM) {
            amount = AMOUNT_DOM;
        } else {
            amount = AMOUNT_IMPOEXPO;
        }

        ArrayList<Integer> arrayValues = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            arrayValues.add(i);
        }
        value = FXCollections.observableArrayList(arrayValues);
        cmb_amount.setItems(value);
        cmb_amount.getSelectionModel().selectFirst();

    }

    private void setTypeComboBox() {
        ObservableList value = FXCollections.observableArrayList(TYPE_VALUES);
        cmb_type.setItems(value);
        cmb_type.getSelectionModel().selectFirst();
    }

    /*
    Controllers creators.
     */
    private void createGridPane(int elements) {

        Label titleLabel = new Label();

        if (SERVICE_TYPE == ServiceType.DOM) {
            gridPane = createDomesticGridPane(elements);
            titleLabel.setText("Grilla de Detalles ("+CommonString.DOM+")");
        } else {
            gridPane = createImpoExpoGridPane(elements);
            if (SERVICE_TYPE == ServiceType.IMPO) {
                titleLabel.setText("Grilla de Detalles ("+CommonString.IMPO+")");
            } else {
                titleLabel.setText("Grilla de Detalles ("+CommonString.EXPO+")");
            }

        }
        titleLabel.setFont(new Font(14));

        innerAnchorPaneGrid.getChildren().clear();
        innerAnchorPaneTitle.getChildren().clear();

        innerAnchorPaneTitle.getChildren().add(titleLabel);
        innerAnchorPaneGrid.getChildren().add(gridPane);

    }

    private GridPane createDomesticGridPane(int elements) {
        GridPane gp = new GridPane();
        int initalColumnWidth = 60;
        int columnWidth = 80;
        gp.getColumnConstraints().add(new ColumnConstraints(initalColumnWidth));
        gp.getColumnConstraints().add(new ColumnConstraints(columnWidth));

        // Setting the headers.
        gp.add(new Label(""), 0, 0);

        Label porcLabel = new Label("     (%)");
        porcLabel.setAlignment(Pos.CENTER);
        gp.add(porcLabel, 1, 0);

        // Creating the body.
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#0.00", decimalSymbols);
        for (int i = 1; i <= elements; i++) {

            Label pLabel = new Label("Tarifa " + i + ":\t");
            gp.add(pLabel, 0, i);

            TextField tx = new TextField();
            tx.setTextFormatter(new TextFormatter<>(c
                    -> {
                if (c.getControlNewText().isEmpty()) {
                    return c;
                }

                ParsePosition parsePosition = new ParsePosition(0);
                Object object = format.parse(c.getControlNewText(), parsePosition);

                if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                } else {
                    return c;
                }
            }));
            tx.setAlignment(Pos.CENTER_RIGHT);
            gp.add(tx, 1, i);

        }
        gp.hgapProperty().set(1.2);
        gp.vgapProperty().set(1.2);
        return gp;
    }

    private GridPane createImpoExpoGridPane(int elements) {
        GridPane gp = new GridPane();

        int initalColumnWidth = 60;
        int columnWidth = 80;
        gp.getColumnConstraints().add(new ColumnConstraints(initalColumnWidth));
        gp.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        gp.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        gp.getColumnConstraints().add(new ColumnConstraints(columnWidth));

        // Setting the headers.
        gp.add(new Label(""), 0, 0);

        Label docPorc = new Label("Doc (%)");
        docPorc.setAlignment(Pos.CENTER);
        gp.add(docPorc, 1, 0);

        Label baseLabel = new Label("Fijo NonDoc");
        baseLabel.setAlignment(Pos.CENTER);
        gp.add(baseLabel, 2, 0);

        Label nondocPorc = new Label("NonDoc (%)");
        nondocPorc.setAlignment(Pos.CENTER);
        gp.add(nondocPorc, 3, 0);

        // Creating the body.
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#0.00", decimalSymbols);
        for (int i = 1; i <= elements; i++) {

            Label pLabel = new Label("Tarifa " + i + ":\t");
            gp.add(pLabel, 0, i);

            for (int j = 1; j <= 3; j++) {
                TextField tx = new TextField();
                tx.setTextFormatter(new TextFormatter<>(c
                        -> {
                    if (c.getControlNewText().isEmpty()) {
                        return c;
                    }

                    ParsePosition parsePosition = new ParsePosition(0);
                    Object object = format.parse(c.getControlNewText(), parsePosition);

                    if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                        return null;
                    } else {
                        return c;
                    }
                }));

                tx.setAlignment(Pos.CENTER_RIGHT);
                gp.add(tx, j, i);
            }

        }

        gp.hgapProperty().set(1.2);
        gp.vgapProperty().set(1.2);

        return gp;
    }

    private FileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
        }
        fileChooser.setInitialDirectory(lastValidDirectory);
        return fileChooser;
    }

    private DirectoryChooser getDirectoryChooser() {
        if (directoryChooser == null) {
            directoryChooser = new DirectoryChooser();
        }
        directoryChooser.setInitialDirectory(lastValidDirectory);
        return directoryChooser;
    }

    /*
    Files builders.
     */
    private void buildFiles() throws IOException {
        if (SERVICE_TYPE == ServiceType.DOM) {
            buildDomesticFiles();
        } else {
            buildExpoImpoFiles();
        }
    }

    private void buildDomesticFiles() throws IOException {

        int cant = cmb_amount.getValue();
        final int PER_COL = 1;
        final int COL_COUNT = 2;

        double[] percentages = new double[cant];

        TextField tp;

        for (int i = 0; i < cant; i++) {

            tp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + PER_COL);
            percentages[i] = Double.parseDouble(tp.getText().length() == 0 ? "0" : tp.getText()) / 100;

        }

        /**
         * Create directories.
         */
        DirectoryCreator dc = new DirectoryCreator(saveFile, priceFile, cant, chb_individuals.isSelected(), SERVICE_TYPE);
        lastFile = dc.getOuputDir();

        /**
         * Create files.
         */
        DomesticOutputCreator domC = new DomesticOutputCreator();
        domC.setCreator(readFile, dc.getDestinyFile(), dc.getOuputDir(), percentages);
        domC.write(chb_individuals.isSelected());

    }

    private void buildExpoImpoFiles() throws IOException {

        int cant = cmb_amount.getValue();
        final int DPER = 1;
        final int NDFIX_COL = 2;
        final int NDPER_COL = 3;
        final int COL_COUNT = 4;
        double[] dpercentages = new double[cant];
        double[] ndfixes = new double[cant];
        double[] ndpercentages = new double[cant];

        TextField dp;
        TextField ndf;
        TextField ndp;

        for (int i = 0; i < cant; i++) {

            dp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + DPER);
            dpercentages[i] = Double.parseDouble(dp.getText().length() == 0 ? "0" : dp.getText()) / 100;

            ndf = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + NDFIX_COL);
            ndfixes[i] = Double.parseDouble(ndf.getText().length() == 0 ? "0" : ndf.getText());

            ndp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + NDPER_COL);
            ndpercentages[i] = Double.parseDouble(ndp.getText().length() == 0 ? "0" : ndp.getText()) / 100;

        }

        /**
         * Create directories.
         */
        DirectoryCreator dc = new DirectoryCreator(saveFile, priceFile, cant, chb_individuals.isSelected(), SERVICE_TYPE);
        lastFile = dc.getOuputDir();

        /**
         * Create files.
         */
        ImpoExpoOutputCreator oc = new ImpoExpoOutputCreator();
        oc.setCreator(readFile, dc.getDestinyFile(), dc.getOuputDir(), dpercentages, ndfixes, ndpercentages);
        oc.write(chb_individuals.isSelected());

    }

    /*
    Alerts
     */
    private void alertError(String msg, String header) {
        Alert a = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }

    private void alertGridError() {
        alertError("Debe completar la Grilla de Detalles.", "Error en la grilla.");
    }

    private void alertBaseError(String msg) {
        alertError(msg, "Hubo un problema con el arhivo seleccionado como 'Base'.");
    }

    private void alertPriceError(String msg) {
        alertError(msg, "Hubo un problema con el archivo seleccionado como 'Tarifa Actual'.");
    }

    private void alertDirError() {
        alertError("No se selecciono una carpeta donde guardar.", "Seleccione una carpeta en el campo 'Guardar en:'");
    }

    private void alertUnknownError(String msg) {
        alertError("Ha ocurrido un error desconocido.\n" + msg, "Error desconocido.");
    }

    private boolean alertFinish() {

        Alert a = new Alert(Alert.AlertType.INFORMATION,
                "Las tarifas fueron creadas en la carpeta '" + lastFile.getName() + "'.\n¿Desea abrir la carpeta donde fueron creadas?",
                ButtonType.YES,
                ButtonType.NO);

        a.setHeaderText("Se han generado las tarifas con exito!");

        Optional<ButtonType> result = a.showAndWait();
        return result.get() == ButtonType.YES;
    }

    private boolean alertConfirmation() {
        Alert a = new Alert(AlertType.CONFIRMATION, "¿Esta seguro que quiere generar las tarifas?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText("Confirmacion de Guardado");
        Optional<ButtonType> result = a.showAndWait();
        return result.get() == ButtonType.YES;
    }

    /*
    Validations
     */
    private boolean inputOk() {

        if (filesSelected()) {

            /**
             * Check if grid is complete.
             */
            if (!validateGrid()) {
                alertGridError();
                return false;
            }

            /**
             * Check if files are valid.
             */
            try {
                FileValidator fv = new FileValidator();
                fv.validate(readFile, priceFile, SERVICE_TYPE);
            } catch (InvalidBaseFileException ex) {
                alertBaseError(ex.getMessage());
                return false;

            } catch (InvalidPriceFileException ex) {
                alertPriceError(ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean filesSelected() {
        if (readFile == null) {
            alertBaseError("No se selecciono un archivo base.");
            return false;
        } else if (priceFile == null) {
            alertPriceError("No se seleccino un archivo tarifa.");
            return false;
        } else if (saveFile == null) {
            alertDirError();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateGrid() {
        if (SERVICE_TYPE == ServiceType.DOM) {
            return validateDomesticGrid();
        } else {
            return validateExpoImpoGrid();
        }
    }

    private boolean validateDomesticGrid() {
        int cant = cmb_amount.getValue();
        final int PER_COL = 1;
        final int COL_COUNT = 2;

        TextField tp;

        for (int i = 0; i < cant; i++) {

            tp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + PER_COL);
            if (tp.getText().trim().isEmpty()) {
                return false;
            }

        }
        return true;
    }

    private boolean validateExpoImpoGrid() {
        int cant = cmb_amount.getValue();
        final int DPER = 1;
        final int NDFIX_COL = 2;
        final int NDPER_COL = 3;
        final int COL_COUNT = 4;

        for (int i = 0; i < cant; i++) {

            TextField dp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + DPER);
            if (dp.getText().trim().isEmpty()) {
                return false;
            }

            TextField ndf = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + NDFIX_COL);
            if (ndf.getText().trim().isEmpty()) {
                return false;
            }

            TextField ndp = (TextField) gridPane.getChildren().get((i + 1) * COL_COUNT + NDPER_COL);
            if (ndp.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
