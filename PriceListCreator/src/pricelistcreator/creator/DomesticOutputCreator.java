/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.creator;

import java.io.File;
import java.io.IOException;
import pricelistcreator.builder.DomesticBuilder;
import pricelistcreator.excel.reader.DomesticReader;
import pricelistcreator.excel.writter.DomesticExcelWritter;

/**
 *
 * @author Gonzalo
 */
public class DomesticOutputCreator  {

    private File READ_FILE;
    private File WRITE_FILE;
    private File BASE_FOLDER;
    
    private double[] PERCENTAGES;

    public DomesticOutputCreator() {

    }

    
    public void setCreator(File read, File write,File baseFolder, double[] percentages) {
        READ_FILE = read;
        WRITE_FILE = write;
        BASE_FOLDER = baseFolder;
       
        PERCENTAGES = percentages;
    }

    
    public void write(boolean individuals) throws IOException {
        
        
        
        DomesticReader dr = new DomesticReader(READ_FILE);
        double[][] base = dr.getDocBase();
        double[][] adderBase = dr.getAdders();

        DomesticExcelWritter dew = new DomesticExcelWritter(WRITE_FILE);

        for (int i = 0; i < PERCENTAGES.length; i++) {

            DomesticBuilder db = new DomesticBuilder(base, adderBase, PERCENTAGES[i]);
            double[][] price = db.getPrice();
            double[][] adder = db.getAdders();
            double[][] priceProfit = db.getPriceProfit();
            double[][] adderProfit = db.getAddersProfit();

            dew.write(price, priceProfit, adder, adderProfit, i + 1);
            if (individuals){
                dew.writeIndividual(price, adder, i+1, BASE_FOLDER);
            }
            dew.writeParameters(PERCENTAGES, BASE_FOLDER);
            
        }
        dew.closeAndSave();
    }

}
