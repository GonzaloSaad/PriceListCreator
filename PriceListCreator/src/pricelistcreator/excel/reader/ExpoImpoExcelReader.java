/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.excel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class ExpoImpoExcelReader {

    private final int DOC_ROW_START = 3;
    private final int NONDOC_ROW_START = 8;
    private final int ADDERS_ROW_START = 289;
    private final int COL_START = 1;

    private final int DOC_ROW_COUNT = 4;
    private final int NONDOC_ROW_COUNT = 280;
    private final int COL_COUNT = 6;

    private final int SHEET = 0;

    private final double[][] docBase;
    private final double[][] nondocBase;
    private final double[] adders;

    private XSSFWorkbook WORK_BOOK;

    public ExpoImpoExcelReader(File input) throws IOException {
        try {
            
           
            WORK_BOOK = new XSSFWorkbook(new FileInputStream(input));
           
        } catch (IOException ex) {
            
            Logger.getLogger(ExpoImpoExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        docBase = new double[DOC_ROW_COUNT][COL_COUNT];
        nondocBase = new double[NONDOC_ROW_COUNT][COL_COUNT];
        adders = new double[COL_COUNT];
        readAndClose();
    }

    private void readAndClose() throws IOException {
        readDocBase();
        readNondocBase();
        readAdders();
        try {
            
            close();
            
        } catch (IOException ex) {
           
            Logger.getLogger(ExpoImpoExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

    }

    private void readDocBase() {

        XSSFSheet sheet = WORK_BOOK.getSheetAt(SHEET);

        XSSFRow row;
        XSSFCell cell;

        for (int i = 0; i < DOC_ROW_COUNT; i++) {
            row = sheet.getRow(i + DOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + DOC_ROW_START);
            }

            for (int j = 0; j < COL_COUNT; j++) {
                cell = row.getCell(j + COL_START);
                if (cell == null) {
                    cell = row.createCell(j + COL_START);
                }
                docBase[i][j] = Double.parseDouble(cell.getRawValue());
            }
        }
    }

    private void readNondocBase() {
        XSSFSheet sheet = WORK_BOOK.getSheetAt(SHEET);

        XSSFRow row;
        XSSFCell cell;

        for (int i = 0; i < NONDOC_ROW_COUNT; i++) {
            row = sheet.getRow(i + NONDOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + NONDOC_ROW_START);
            }

            for (int j = 0; j < COL_COUNT; j++) {
                cell = row.getCell(j + COL_START);
                if (cell == null) {
                    cell = row.createCell(j + COL_START);
                }
                nondocBase[i][j] = Double.parseDouble(cell.getRawValue());
            }
        }
    }

    private void readAdders() {

        XSSFSheet sheet = WORK_BOOK.getSheetAt(SHEET);

        XSSFRow row;
        XSSFCell cell;

        row = sheet.getRow(ADDERS_ROW_START);
        if (row == null) {
            row = sheet.createRow(DOC_ROW_START);
        }

        for (int j = 0; j < COL_COUNT; j++) {
            cell = row.getCell(j + COL_START);
            if (cell == null) {
                cell = row.createCell(j + COL_START);
            }
            adders[j] = Double.parseDouble(cell.getRawValue());
        }

    }

    private void close() throws IOException {
        WORK_BOOK.close();
    }

    public double[][] getDocBase() {
        return docBase;
    }

    public double[][] getNondocBae() {
        return nondocBase;
    }

    public double[] getAdders() {
        return adders;
    }

}
