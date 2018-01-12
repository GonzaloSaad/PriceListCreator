/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.creator;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Gonzalo
 */
public abstract class OutputCreator {

    public abstract void setCreator(File read, File write,File baseFolder, double[] fixes, double[] percentages);

    public abstract void write(boolean individuals) throws IOException;
}
