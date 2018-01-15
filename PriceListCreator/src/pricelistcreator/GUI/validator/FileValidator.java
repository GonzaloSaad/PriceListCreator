/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.GUI.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pricelistcreator.GUI.validator.exceptions.InvalidBaseFileException;
import pricelistcreator.GUI.validator.exceptions.InvalidPriceFileException;
import pricelistcreator.common.ServiceType;

/**
 *
 * @author Gonzalo
 */
public class FileValidator {

    private final int OK_COL = 20;
    private final int OK_ROW = 0;
    private final int OK_SHEET = 0;

    private final int DOM_BASE_CODE = 111;
    private final int IMPOEXPO_BASE_CODE = 112;

    private final int DOM_PRICE_CODE = 221;
    private final int IMPO_PRICE_CODE = 222;
    private final int EXPO_PRICE_CODE = 223;

    public FileValidator() {

    }

    public void validate(File read, File price, ServiceType type) throws InvalidBaseFileException, InvalidPriceFileException {

        int cmpValue;

        if (type == ServiceType.DOM) {
            cmpValue = DOM_BASE_CODE;
        } else {
            cmpValue = IMPOEXPO_BASE_CODE;
        }

        if (read == null) {
            throw new InvalidBaseFileException("No se selecciono un archivo base.");
        }

        if (price == null) {
            throw new InvalidPriceFileException("No se selecciono un archivo de tarifa actual.");
        }

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(read));
            XSSFSheet sheet = workbook.getSheetAt(OK_SHEET);

            XSSFRow okrow = sheet.getRow(OK_ROW);

            if (okrow == null) {
                throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no es una base.");
            }

            XSSFCell okcell = okrow.getCell(OK_COL);

            if (okcell == null) {
                throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no es una base.");
            }

            int cellvalue = Integer.parseInt(okcell.getRawValue());
            
            if (cellvalue >200){
                throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no es una base.");
            }

            if (cellvalue != cmpValue) {
                throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no es de " + ServiceType.getName(type) + ".");
            }

            workbook.close();

        } catch (IOException ex) {

            Logger.getLogger(FileValidator.class.getName()).log(Level.SEVERE, null, ex);
            throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no se pudo leer.");

        } catch (NumberFormatException ex) {
            throw new InvalidBaseFileException("El archivo '" + read.getName() + "' no es una tarifa.");
        }

        if (type == ServiceType.DOM) {
            cmpValue = DOM_PRICE_CODE;
        } else if (type == ServiceType.IMPO) {
            cmpValue = IMPO_PRICE_CODE;
        } else {
            cmpValue = EXPO_PRICE_CODE;
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(price));
            XSSFSheet sheet = workbook.getSheetAt(OK_SHEET);

            XSSFRow okrow = sheet.getRow(OK_ROW);

            if (okrow == null) {
                throw new InvalidBaseFileException("El archivo '" + price.getName() + "' no es una tarifa.");
            }

            XSSFCell okcell = okrow.getCell(OK_COL);

            if (okcell == null) {
                throw new InvalidPriceFileException("El archivo '" + price.getName() + "' no es una tarifa.");
            }

            int cellvalue = Integer.parseInt(okcell.getRawValue());
            
            if (cellvalue<200){
                throw new InvalidPriceFileException("El archivo '" + price.getName() + "' no es una tarifa.");
            }

            if (cellvalue != cmpValue) {
                throw new InvalidPriceFileException("El archivo '" + price.getName() + "' no es de " + ServiceType.getName(type) + ".");
            }

            workbook.close();
        } catch (IOException ex) {

            Logger.getLogger(FileValidator.class.getName()).log(Level.SEVERE, null, ex);
            throw new InvalidPriceFileException("El archivo '" + price.getName() + "' no se pudo leer.");

        } catch (NumberFormatException ex) {
            throw new InvalidPriceFileException("El archivo '" + price.getName() + "' no es una tarifa.");
        }
    }
}
