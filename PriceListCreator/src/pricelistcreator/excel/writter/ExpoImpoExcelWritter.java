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
public class ExpoImpoExcelWritter {

    private final int DOC_ROW_START = 9;
    private final int NONDOC_ROW_START = 14;
    private final int ADDERS_ROW_START = 295;
    private final int PRICE_COL_START = 3;
    private final int PROFIT_COL_START = 14;
    private final File OUTPUT_FILE;
    private final XSSFWorkbook WORK_BOOK;

    private final int TITLE_ROW = 4;
    private final int PRICE_COL = 4;
    private final int COURIER_COL = 7;

    public ExpoImpoExcelWritter(File file) throws FileNotFoundException, IOException {
        try {

            OUTPUT_FILE = file;
            
            WORK_BOOK = new XSSFWorkbook(new FileInputStream(OUTPUT_FILE));

           

        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
            
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void write(double[][] docPrices, double[][] docProfit, double[][] nondocPrices, double[][] nondocProfit, double[] adders, double[] addersProfit, int worksheet) {
        

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
         * Part 1: Docs.
         */
        int n = docPrices.length;
        int m = docPrices[0].length;
        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + DOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + DOC_ROW_START);
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
                cellPrice.setCellValue(docPrices[i][j]);
                cellProfit.setCellValue(docProfit[i][j]);
            }
        }

        /**
         * Part 2: NonDocs
         */
        n = nondocPrices.length;
        m = nondocPrices[0].length;
        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + NONDOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + NONDOC_ROW_START);
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
                cellPrice.setCellValue(nondocPrices[i][j]);
                cellProfit.setCellValue(nondocProfit[i][j]);
            }
        }

        /**
         * Part 3: Adders.
         */
        m = adders.length;
        row = sheet.getRow(ADDERS_ROW_START);
        if (row == null) {
            row = sheet.createRow(ADDERS_ROW_START);
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
            cellPrice.setCellValue(adders[j]);
            cellProfit.setCellValue(addersProfit[j]);
        }

    }

    public void closeAndSave() throws FileNotFoundException, IOException {
        try {

            FileOutputStream fileOut = new FileOutputStream(OUTPUT_FILE);
            //write this workbook to an Outputstream.
            
            WORK_BOOK.write(fileOut);
            fileOut.flush();
           
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IOException ex) {
           
            Logger.getLogger(ExpoImpoExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void writeIndividual(double[][] docPrices, double[][] nondocPrices, double[] adders, int worksheet, File dir) throws FileNotFoundException, IOException {
        XSSFWorkbook wb;

        
        File outputFile = new File(dir.getAbsoluteFile()+"\\"+worksheet+".xlsx");
        
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
         * Part 1: Docs.
         */
        int n = docPrices.length;
        int m = docPrices[0].length;
        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + DOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + DOC_ROW_START);
            }

            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);

                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }

                cellPrice.setCellValue(docPrices[i][j]);

            }
        }

        /**
         * Part 2: NonDocs
         */
        n = nondocPrices.length;
        m = nondocPrices[0].length;
        for (int i = 0; i < n; i++) {
            row = sheet.getRow(i + NONDOC_ROW_START);
            if (row == null) {
                row = sheet.createRow(i + NONDOC_ROW_START);
            }

            for (int j = 0; j < m; j++) {
                cellPrice = row.getCell(j + PRICE_COL_START);

                if (cellPrice == null) {
                    cellPrice = row.createCell(j + PRICE_COL_START);
                }

                cellPrice.setCellValue(nondocPrices[i][j]);

            }
        }

        /**
         * Part 3: Adders.
         */
        m = adders.length;
        row = sheet.getRow(ADDERS_ROW_START);
        if (row == null) {
            row = sheet.createRow(ADDERS_ROW_START);
        }
        for (int j = 0; j < m; j++) {
            cellPrice = row.getCell(j + PRICE_COL_START);

            if (cellPrice == null) {
                cellPrice = row.createCell(j + PRICE_COL_START);
            }

            cellPrice.setCellValue(adders[j]);

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
