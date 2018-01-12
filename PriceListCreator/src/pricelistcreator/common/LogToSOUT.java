/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.common;

/**
 *
 * @author Gonzalo
 */
public class LogToSOUT {
    
    
    public static void log(String msg){
        System.out.println("["+CommonTime.getTime()+"] "+msg);
    }
    
     public static void log(int msg){
        System.out.println("["+CommonTime.getTime()+"] "+msg);
    }
}
