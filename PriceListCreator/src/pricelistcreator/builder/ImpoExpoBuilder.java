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
public class ImpoExpoBuilder {

    private final int DOC_PRICE_ROWS;
    private final int NONDOC_PRICE_ROWS;
    private final int PRICE_COLS;

    private final double[][] DOC_BASE;
    private final double[][] NONDOC_BASE;
    private final double[] ADDERS_BASE;
    private final double FIX;
    private final double PORC;
    private double[][] docPrice;
    private double[][] nondocPrice;
    private double[] adders;
    private double[][] docPriceProfit;
    private double[][] nondocPriceProfit;
    private double[] addersProfit;

    public ImpoExpoBuilder(double[][] docBase, double[][] nondocBase, double[] adderBase, double fix, double porc) {
        DOC_BASE = docBase;
        NONDOC_BASE = nondocBase;
        ADDERS_BASE = adderBase;

        FIX = fix;
        PORC = porc;

        DOC_PRICE_ROWS = docBase.length;
        NONDOC_PRICE_ROWS = nondocBase.length;
        PRICE_COLS = docBase[0].length;

        docPrice = new double[DOC_PRICE_ROWS][PRICE_COLS];
        nondocPrice = new double[NONDOC_PRICE_ROWS][PRICE_COLS];
        adders = new double[PRICE_COLS];

        docPriceProfit = new double[DOC_PRICE_ROWS][PRICE_COLS];
        nondocPriceProfit = new double[NONDOC_PRICE_ROWS][PRICE_COLS];
        addersProfit = new double[PRICE_COLS];
        buildDocPrice();
        buildNondocPrice();
        buildAdder();
        buildDocProfit();
        buildNondocProfit();
        buildAddersProfit();
    }

    private void buildDocPrice() {
        double pfix = FIX;
        double decrement = FIX / DOC_PRICE_ROWS;

        for (int i = 0; i < DOC_PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {
                docPrice[i][j] = CommonMath.round(DOC_BASE[i][j] * (1 + PORC) + pfix, 2);
            }

            if (pfix <= 0) {
                pfix = 0;
            } else {
                pfix -= decrement;
            }
        }
    }

    private void buildNondocPrice() {
        double pfix = FIX;
        double decrement = FIX / NONDOC_PRICE_ROWS;

        for (int i = 0; i < NONDOC_PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {
                nondocPrice[i][j] = CommonMath.round(NONDOC_BASE[i][j] * (1 + PORC) + pfix, 2);
            }

            if (pfix <= 0) {
                pfix = 0;
            } else {
                pfix -= decrement;
            }
        }
    }

    private void buildAdder() {

        for (int j = 0; j < PRICE_COLS; j++) {
            adders[j] = CommonMath.round(ADDERS_BASE[j] * (1 + PORC), 2);
        }
    }

    private void buildDocProfit() {
        for (int i = 0; i < DOC_PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {
                docPriceProfit[i][j] = CommonMath.round(docPrice[i][j] - DOC_BASE[i][j],2);
            }
        }
    }

    private void buildNondocProfit() {
        for (int i = 0; i < NONDOC_PRICE_ROWS; i++) {
            for (int j = 0; j < PRICE_COLS; j++) {
                nondocPriceProfit[i][j] = CommonMath.round(nondocPrice[i][j] - NONDOC_BASE[i][j],2);
            }
        }
    }

    private void buildAddersProfit() {
        for (int j = 0; j < PRICE_COLS; j++) {
            addersProfit[j] = CommonMath.round(adders[j] - ADDERS_BASE[j],2);
        }
    }

    public double[][] getDocPrice() {
        return docPrice;
    }

    public double[][] getNondocPrice() {
        return nondocPrice;
    }

    public double[] getAdders() {
        return adders;
    }

    public double[][] getDocPriceProfit() {
        return docPriceProfit;
    }

    public double[][] getNondocPriceProfit() {
        return nondocPriceProfit;
    }

    public double[] getAddersProfit() {
        return addersProfit;
    }
    
    
}
