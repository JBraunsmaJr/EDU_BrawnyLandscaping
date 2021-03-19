/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import orm.annotations.Required;
import java.sql.*;

/**
 *
 * @author jonbr
 */
public class EntityHelper 
{
    public static boolean IS_STRING_EMPTY(String value)
    {
        if(value == null)
            return true;
        
        return value.isEmpty() || value.isBlank();
    }    
}
