/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricelistcreator.creator;

import java.io.IOException;

/**
 *
 * @author Gonzalo
 */
public class DirectoryNotCreatedException extends IOException {
    
    public DirectoryNotCreatedException(String message){
        super(message);
    }
}
