/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.builder;

import pricelistcreator.common.CommonMath;

/**
 *
 * @author Gonzalo
 */
public class DomesticBuilder {

    private final int PRICE_ROWS;
    private final int PRICE_COLS;
    private final int ADDERS_ROWS;
    private final int ADDERS_COLS;

    private final double[][] PRICE_BASE;
    private final double[][] ADDERS_BASE;
    private final double FIX;
    private final double PORC;
    private double[][] price;
    private double[][] adders;
    private double[][] priceProfit;
    private double[][] addersProfit;

    public DomesticBuilder(double[][] base, double[][] baseAdders, double fix, double porc) {

        PRICE_BASE = base;
        ADDERS_BASE = baseAdders;
        FIX = fix;
        PORC = porc;
        PRICE_ROWS = base.length;
        PRICE_COLS = base[0].length;
        ADDERS_ROWS = baseAdders.length;
        ADDERS_COLS = baseAdders[0].length;

        price = new double[PRICE_ROWS][PRICE_COLS];
        adders = new double[ADDERS_ROWS][ADDERS_COLS];
        priceProfit = new double[PRICE_ROWS][PRICE_COLS];
        addersProfit = new double[ADDERS_ROWS][ADDERS_COLS];
        
        buildPrice();
        buildAdder();
        buildPriceProfit();
        buildAddersProfit();

    }

    private void buildPrice() {
        double pfix = FIX;
        double decrement = FIX / PRICE_ROWS;      
        
        for (int i = 0; i < PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {
                price[i][j] = CommonMath.round(PRICE_BASE[i][j] * (1 + PORC) + pfix, 2);                
                pfix -= decrement;
            }
        }
    }

    private void buildAdder() {

        for (int i = 0; i < ADDERS_ROWS; i++) {
            for (int j = 0; j < ADDERS_COLS; j++) {
                adders[i][j] = CommonMath.round(ADDERS_BASE[i][j] * (1 + PORC), 2);
            }
        }
    }

    private void buildPriceProfit() {
        for (int i = 0; i < PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {

                priceProfit[i][j] = price[i][j] - PRICE_BASE[i][j];

            }
        }
    }

    private void buildAddersProfit() {
        for (int i = 0; i < ADDERS_ROWS; i++) {

            for (int j = 0; j < ADDERS_COLS; j++) {
                addersProfit[i][j] = adders[i][j] - ADDERS_BASE[i][j];
            }

        }
    }

    public double[][] getPrice() {
        return price;
    }

    public double[][] getAdders() {
        return adders;
    }

    public double[][] getPriceProfit() {
        return priceProfit;
    }

    public double[][] getAddersProfit() {
        return addersProfit;
    }

    
    
}
