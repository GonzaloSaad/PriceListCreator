/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gonzalo
 */
public class CommonTime {

    public static String getTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    public static String getYear() {
        DateFormat df = new SimpleDateFormat("yyyy");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
}
