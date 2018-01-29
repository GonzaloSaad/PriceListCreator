/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;

import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pricelistcreator.common.CommonTime;
import pricelistcreator.common.ServiceType;

/**
 *
 * @author Gonzalo
 */
public class DirectoryCreator {

    private File OUTPUT_DIRECTORY;
    private File DESTINY_FILE;

    public DirectoryCreator(File directory, File prices, int amount, boolean needsIndividuals, ServiceType servType) throws DirectoryNotCreatedException, IOException {

        createBaseDirectory(directory, servType);
        createFilesInOutput(prices, needsIndividuals, amount, servType);
    }

    private void createBaseDirectory(File rootFolder, ServiceType st) throws DirectoryNotCreatedException {

        String path = rootFolder.getAbsolutePath() + ServiceType.getServicePath(st) + CommonTime.getYear();

        int i = 0;
        String alt = "";
        do {

            if (i > 0) {
                alt = "-" + Integer.toString(i);
            }
            OUTPUT_DIRECTORY = new File(path + alt);
            i++;
        } while (OUTPUT_DIRECTORY.exists());

        try {
            OUTPUT_DIRECTORY.mkdir();

            OUTPUT_DIRECTORY.mkdir();

        } catch (SecurityException se) {

            throw new DirectoryNotCreatedException(se.getMessage());
        }

    }

    private void createFilesInOutput(File prices, boolean needsIndividuals, int amount, ServiceType st) throws IOException {

        DESTINY_FILE = new File(OUTPUT_DIRECTORY.getAbsolutePath() + ServiceType.getServicePath(st) + "Clientes " + CommonTime.getYear() + ".xlsx");
        try {
            /**
             * Create copy of actual price file.
             */
            Files.copy(prices.toPath(), DESTINY_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);

            /**
             * Create copy of parameters.
             */
            InputStream parametersInput;
            if (st == ServiceType.EXPO || st == ServiceType.IMPO) {
                parametersInput = getClass().getResourceAsStream("/resources/EXPO_IMPO_PARAMETERS_OUTPUT.xlsx");
            } else {
                parametersInput = getClass().getResourceAsStream("/resources/DOM_PARAMETERS_OUTPUT.xlsx");
            }
            File parametersOutput = new File(OUTPUT_DIRECTORY + "\\Parametros de Tarifa.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(parametersInput);

            FileOutputStream fileOut = new FileOutputStream(parametersOutput);
            wb.write(fileOut);
            fileOut.flush();

        } catch (IOException ex) {

            Logger.getLogger(DirectoryCreator.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        if (needsIndividuals) {

            for (int i = 0; i < amount; i++) {

                File output = new File(OUTPUT_DIRECTORY + "\\" + (i + 1) + ".xlsx");

                InputStream source;

                if (st == ServiceType.EXPO || st == ServiceType.IMPO) {

                    source = getClass().getResourceAsStream("/resources/EXPO_IMPO_CLIENT_SIDE_FORMAT_OUTPUT.xlsx");

                    //path = "resources/EXPO_IMPO_CLIENT_SIDE_FORMAT_OUTPUT.xlsx";
                } else {
                    source = getClass().getResourceAsStream("/resources/DOM_CLIENT_SIDE_FORMAT_OUTPUT.xlsx");
                    //path = "resources/DOM_CLIENT_SIDE_FORMAT_OUTPUT.xlsx";
                }

                XSSFWorkbook wb = new XSSFWorkbook(source);

                FileOutputStream fileOut = new FileOutputStream(output);
                wb.write(fileOut);
                fileOut.flush();

            }

        }

    }

    public File getOuputDir() {
        return OUTPUT_DIRECTORY;
    }

    public File getDestinyFile() {
        return DESTINY_FILE;
    }

}
