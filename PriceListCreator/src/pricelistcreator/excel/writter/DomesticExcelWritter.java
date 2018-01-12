/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.excel.writter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pricelistcreator.common.CommonString;
import pricelistcreator.common.CommonTime;

/**
 *
 * @author Gonzalo
 */
public class DomesticExcelWritter {

    private final int PRICE_ROW_START = 12;
    private final int ADDERS_ROW_START = 73;
    private final int PRICE_COL_START = 2;
    private final int PROFIT_COL_START = 11;
    private final File OUTPUT_FILE;

    private final int TITLE_ROW = 7;
    private final int PRICE_COL = 1;
    private final int COURIER_COL = 4;

    private final XSSFWorkbook WORK_BOOK;

    public DomesticExcelWritter(File file) throws FileNotFoundException, IOException {

        OUTPUT_FILE = file;
        try {

            System.out.println("DomesticExcelWritter.");
            System.out.println("\t" + file.getAbsolutePath());
            System.out.println("\tOpening File...");
            WORK_BOOK = new XSSFWorkbook(new FileInputStream(OUTPUT_FILE));

            System.out.println("\tInit\t-\t[" + CommonTime.getTime() + "]");

        } catch (FileNotFoundException ex) {
            System.out.println("\tFileNotFoundException!!! " + ex.getMessage());
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
            System.out.println("\tIOException!!! " + ex.getMessage());
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void write(double[][] prices, double[][] priceProfit, double[][] adders, double[][] addersProfit, int worksheet) throws FileNotFoundException, IOException {

        XSSFSheet sheet = WORK_BOOK.getSheetAt(worksheet);

        /**
         * Part 0: row and cell declaration.
         */
        XSSFRow row;
        XSSFCell cellPrice;
        XSSFCell cellProfit;

        row = sheet.getRow(TITLE_ROW);
        cellPrice = row.getCell(PRICE_COL);
        if (cellPrice == null) {
            cellPrice = row.createCell(PRICE_COL);
        }
        cellPrice.setCellValue(CommonString.PRICE + " " + Integer.toString(worksheet));
        cellPrice = row.getCell(COURIER_COL);
        if (cellPrice == null) {
            cellPrice = row.createCell(COURIER_COL);
        }
        cellPrice.setCellValue(CommonString.COURIER + " " + CommonTime.getYear());

        /**
         * Part 1: Prices.
         */
        int n = prices.length;
        int m = prices[0].length;
        for (int i = 0; i < n; i++) {

            row = sheet.getRow(i + PRICE_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + PRICE_ROW_START);
            }

            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);
                cellProfit = row.getCell(j + PROFIT_COL_START);
                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }
                if (cellProfit == null) {
                    cellProfit = row.createCell(j + PROFIT_COL_START);
                }
                cellPrice.setCellValue(prices[i][j]);
                cellProfit.setCellValue(priceProfit[i][j]);
            }
        }

        /**
         * Part 2: Adders.
         */
        n = adders.length;
        m = adders[0].length;

        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + ADDERS_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + ADDERS_ROW_START);
            }
            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);
                cellProfit = row.getCell(j + PROFIT_COL_START);
                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }
                if (cellProfit == null) {
                    cellProfit = row.createCell(j + PROFIT_COL_START);
                }
                cellPrice.setCellValue(adders[i][j]);
                cellProfit.setCellValue(addersProfit[i][j]);
            }

        }

    }

    public void closeAndSave() throws FileNotFoundException, IOException {
        try {

            FileOutputStream fileOut = new FileOutputStream(OUTPUT_FILE);

            System.out.println("\tSaving...");
            WORK_BOOK.write(fileOut);
            fileOut.flush();
            System.out.println("\tFinish\t-\t[" + CommonTime.getTime() + "]");
        } catch (FileNotFoundException ex) {
            System.out.println("\tFileNotFoundException!!! " + ex.getMessage());
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
            System.out.println("\tIOException!!! " + ex.getMessage());
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void writeIndividual(double[][] prices, double[][] adders, int worksheet, File dir) throws FileNotFoundException, IOException {

        XSSFWorkbook wb;
        File outputFile = new File(dir.getAbsoluteFile() + "\\" + worksheet + ".xlsx");
        try {

            wb = new XSSFWorkbook(new FileInputStream(outputFile));

        } catch (FileNotFoundException ex) {

            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {

            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        wb.setSheetName(0, Integer.toString(worksheet));
        XSSFSheet sheet = wb.getSheetAt(0);

        /**
         * Part 0: row and cell declaration.
         */
        XSSFRow row;

        XSSFCell cellPrice;

        row = sheet.getRow(TITLE_ROW);
        cellPrice = row.getCell(PRICE_COL);
        if (cellPrice == null) {
            cellPrice = row.createCell(PRICE_COL);
        }
        cellPrice.setCellValue(CommonString.PRICE + " " + Integer.toString(worksheet));
        cellPrice = row.getCell(COURIER_COL);
        if (cellPrice == null) {
            cellPrice = row.createCell(COURIER_COL);
        }
        cellPrice.setCellValue(CommonString.COURIER + " " + CommonTime.getYear());

        /**
         * Part 1: Prices.
         */
        int n = prices.length;
        int m = prices[0].length;
        for (int i = 0; i < n; i++) {

            row = sheet.getRow(i + PRICE_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + PRICE_ROW_START);
            }

            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);

                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }

                cellPrice.setCellValue(prices[i][j]);

            }
        }

        /**
         * Part 2: Adders.
         */
        n = adders.length;
        m = adders[0].length;

        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + ADDERS_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + ADDERS_ROW_START);
            }
            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);

                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }

                cellPrice.setCellValue(adders[i][j]);

            }

        }

        try {

            FileOutputStream fileOut = new FileOutputStream(outputFile);

            wb.write(fileOut);
            fileOut.flush();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {

            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

    }

}
