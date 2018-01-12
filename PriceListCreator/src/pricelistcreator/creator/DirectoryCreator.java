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
import pricelistcreator.common.LogToSOUT;

/**
 *
 * @author Gonzalo
 */
public class DirectoryCreator {

    private File BASE_DIRECTORY;
    private File OUTPUT_DIRECTORY;
    private File DESTINY_FILE;

    public DirectoryCreator(File directory, File prices, int amount, boolean needsIndividuals, ServiceType servType) throws DirectoryNotCreatedException, IOException {
        LogToSOUT.log("DirectoryCreator");
        createBaseDirectory(directory, servType);
        createFilesInOutput(prices, needsIndividuals, amount, servType);
    }

   

    private void createBaseDirectory(File rootFolder, ServiceType st) throws DirectoryNotCreatedException {
        LogToSOUT.log("\tCreating Final Directory...");
        String path = rootFolder.getAbsolutePath() + "\\Tarifas " + CommonTime.getYear();
        LogToSOUT.log("\t\tPath[" + path + "]");

        int i = 0;
        String alt = "";
        do {

            if (i > 0) {
                alt = "-" + Integer.toString(i);
            }
            BASE_DIRECTORY = new File(path + alt);
            i++;
        } while (BASE_DIRECTORY.exists());

        OUTPUT_DIRECTORY = new File(BASE_DIRECTORY.getAbsolutePath() + ServiceType.getServicePath(st));

        try {
            BASE_DIRECTORY.mkdir();
            LogToSOUT.log("\t\tBase directory Created.");
            OUTPUT_DIRECTORY.mkdir();
            LogToSOUT.log("\t\tOutput Directory Created.");
            LogToSOUT.log("\tFinish");
        } catch (SecurityException se) {
            LogToSOUT.log("\t\t" + se.getMessage());
            throw new DirectoryNotCreatedException(se.getMessage());
        }

    }

    private void createFilesInOutput(File prices, boolean needsIndividuals, int amount, ServiceType st) throws IOException {

        LogToSOUT.log("\tCreating Files in Output...");
        
        DESTINY_FILE = new File(OUTPUT_DIRECTORY.getAbsolutePath() + ServiceType.getServicePath(st) + " Clientes " + CommonTime.getYear() + ".xlsx");
        try {
            LogToSOUT.log("\t\tCopy price file.");
            Files.copy(prices.toPath(), DESTINY_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            LogToSOUT.log("\t\t" + ex.getMessage());
            Logger.getLogger(DirectoryCreator.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        if (needsIndividuals) {
            LogToSOUT.log("\t\tCopy individuals files.");

            for (int i = 0; i < amount; i++) {
                LogToSOUT.log("\t\t\tFile " + (i + 1));
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

        LogToSOUT.log("\tFinish");

    }

    public File getBaseDir() {
        return BASE_DIRECTORY;
    }

    public File getOuputDir() {
        return OUTPUT_DIRECTORY;
    }

    public File getDestinyFile() {
        return DESTINY_FILE;
    }

}
