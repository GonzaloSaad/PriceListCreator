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
public class ImpoExpoOutputCreator extends OutputCreator {

    private File READ_FILE;
    private File WRITE_FILE;
    private File BASE_FOLDER;
    private double[] FIXS;
    private double[] PERCENTAGES;

    public ImpoExpoOutputCreator() {

    }

    @Override
    public void setCreator(File read, File write, File baseFolder, double[] fixes, double[] percentages) {
        READ_FILE = read;
        WRITE_FILE = write;
        BASE_FOLDER = baseFolder;
        FIXS = fixes;
        PERCENTAGES = percentages;
    }

    @Override
    public void write(boolean individuals) throws IOException {
        ExpoImpoExcelReader er = new ExpoImpoExcelReader(READ_FILE);

        double[][] docBase = er.getDocBase();
        double[][] nonDocBase = er.getNondocBae();
        double[] adderBase = er.getAdders();
        ExpoImpoExcelWritter eiw = new ExpoImpoExcelWritter(WRITE_FILE);

        for (int i = 0; i < FIXS.length; i++) {

            ImpoExpoBuilder ieb = new ImpoExpoBuilder(docBase, nonDocBase, adderBase, FIXS[i], PERCENTAGES[i]);
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
