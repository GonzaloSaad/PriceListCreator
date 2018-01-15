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
public enum ServiceType {
    DOM,
    IMPO,
    EXPO;
    
    public static String getServiceFileName(ServiceType st){
        
        StringBuilder sb = new StringBuilder();
        
        switch (st){
            case DOM:
                sb.append("Domestico Clientes");
                break;
            case IMPO:
                sb.append("Impo Clientes");
                break;
            case EXPO:
                sb.append("Expo Clientes");
                break;
        }
        sb.append(CommonTime.getYear());
        return sb.toString();
                
        
    }
    
    public static String getServicePath(ServiceType st){
        
        StringBuilder sb = new StringBuilder();
        
        switch (st){
            case DOM:
                sb.append("\\Domestico ");
                break;
            case IMPO:
                sb.append("\\Impo ");
                break;
            case EXPO:
                sb.append("\\Expo ");
                break;
        }            
        
        return sb.toString();
                
        
    }
    
    public static String getName(ServiceType st){
        switch (st){
            case DOM:
                return "Domestico";
                
            case IMPO:
                return "Importacion";
                
            case EXPO:
                return "Exportacion";
                
        }
        return "";
    }
    
    
}
