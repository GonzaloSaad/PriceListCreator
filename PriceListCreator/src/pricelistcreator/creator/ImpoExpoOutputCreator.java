/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.creator;

import java.io.File;
import java.io.IOException;
import pricelistcreator.builder.ImpoExpoBuilder;
import pricelistcreator.excel.reader.ExpoImpoExcelReader;
import pricelistcreator.excel.writter.ExpoImpoExcelWritter;

/**
 *
 * @author Gonzalo
 */
public class ImpoExpoOutputCreator  {

    private File READ_FILE;
    private File WRITE_FILE;
    private File BASE_FOLDER;
    private double[] DOC_PERCENTAGES;
    private double[] NON_DOC_FIXS;
    private double[] NON_DOC_PERCENTAGES;

    public ImpoExpoOutputCreator() {

    }

    
    public void setCreator(File read, File write,File baseFolder,double [] docPercentages, double[] nonDocfixes, double[] nonDocPercentages) {
        READ_FILE = read;
        WRITE_FILE = write;
        BASE_FOLDER = baseFolder;
        DOC_PERCENTAGES = docPercentages;
        NON_DOC_FIXS = nonDocfixes;
        NON_DOC_PERCENTAGES = nonDocPercentages;
    }

   
    public void write(boolean individuals) throws IOException {
        ExpoImpoExcelReader er = new ExpoImpoExcelReader(READ_FILE);

        double[][] docBase = er.getDocBase();
        double[][] nonDocBase = er.getNondocBae();
        double[] adderBase = er.getAdders();
        ExpoImpoExcelWritter eiw = new ExpoImpoExcelWritter(WRITE_FILE);

        for (int i = 0; i < DOC_PERCENTAGES.length; i++) {

            ImpoExpoBuilder ieb = new ImpoExpoBuilder(docBase, nonDocBase, adderBase, DOC_PERCENTAGES[i],NON_DOC_FIXS[i], NON_DOC_PERCENTAGES[i]);
            double[][] docPrice = ieb.getDocPrice();
            double[][] nonDocPrice = ieb.getNondocPrice();
            double[] adder = ieb.getAdders();
            double[][] docProfit = ieb.getDocPriceProfit();
            double[][] nonDocProfit = ieb.getNondocPriceProfit();
            double[] adderProfit = ieb.getAddersProfit();

            eiw.write(docPrice, docProfit, nonDocPrice, nonDocProfit, adder, adderProfit, i + 1);
            if (individuals) {
                eiw.writeIndividual(docPrice, nonDocPrice, adder, i + 1, BASE_FOLDER);
            }
        }
        eiw.closeAndSave();

    }

}
