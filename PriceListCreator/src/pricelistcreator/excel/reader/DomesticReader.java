/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.excel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pricelistcreator.common.CommonTime;

/**
 *
 * @author Gonzalo
 */
public class DomesticReader {

    private final int PRICE_ROW_START = 3;
    private final int ADDERS_ROW_START = 64;
    private final int COL_START = 1;

    private final int PRICE_ROW_COUNT = 60;
    private final int ADDERS_ROW_COUNT = 3;
    private final int COL_COUNT = 4;

    private final int SHEET = 0;

    private final double[][] base;
    private final double[][] adders;

    private XSSFWorkbook WORK_BOOK;

    public DomesticReader(File input) throws IOException {
        try {
            
            WORK_BOOK = new XSSFWorkbook(new FileInputStream(input));
            
        } catch (IOException ex) {
           
            Logger.getLogger(ExpoImpoExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        base = new double[PRICE_ROW_COUNT][COL_COUNT];
        adders = new double[ADDERS_ROW_COUNT][COL_COUNT];

        readAndClose();
    }

    private void readAndClose() throws IOException {
        readBase();
        readAdders();
        try {
            
            close();
            
        } catch (IOException ex) {
            
            Logger.getLogger(ExpoImpoExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

    }

    private void readBase() {

        XSSFSheet sheet = WORK_BOOK.getSheetAt(SHEET);

        XSSFRow row;
        XSSFCell cell;

        for (int i = 0; i < PRICE_ROW_COUNT; i++) {
            row = sheet.getRow(i + PRICE_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + PRICE_ROW_START);
            }

            for (int j = 0; j < COL_COUNT; j++) {
                cell = row.getCell(j + COL_START);
                if (cell == null) {
                    cell = row.createCell(j + COL_START);
                }
                base[i][j] = Double.parseDouble(cell.getRawValue());
                
            }
        }
    }

    private void readAdders() {

        XSSFSheet sheet = WORK_BOOK.getSheetAt(SHEET);

        XSSFRow row;
        XSSFCell cell;

        for (int i = 0; i < ADDERS_ROW_COUNT; i++) {
            row = sheet.getRow(i + ADDERS_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + ADDERS_ROW_START);
            }

            for (int j = 0; j < COL_COUNT; j++) {
                cell = row.getCell(j + COL_START);
                if (cell == null) {
                    cell = row.createCell(j + COL_START);
                }
                adders[i][j] = Double.parseDouble(cell.getRawValue());
            }
        }

    }

    private void close() throws IOException {
        WORK_BOOK.close();
    }

    public double[][] getDocBase() {
        return base;
    }

    public double[][] getAdders() {
        return adders;
    }

}
