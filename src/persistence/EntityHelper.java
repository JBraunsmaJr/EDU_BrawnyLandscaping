/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import annotations.Required;
import java.sql.*;
import java.util.Objects;

/**
 *
 * @author jonbr
 */
public class EntityHelper 
{
    public static Connection getConnection() throws Exception
    {
        Connection connection = ApplicationDbContext.getConnection();
        
        if(connection == null)
            throw new Exception("Requires a valid connection");
        
        return connection;
    }
    
    public static boolean IS_STRING_EMPTY(String value)
    {
        if(value == null)
            return true;
        
        return value.isEmpty() || value.isBlank();
    }    
}
