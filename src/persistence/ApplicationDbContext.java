/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import orm.ConnectionConfig;

import java.sql.*;

/**
 * Singleton class - should only have 1 connection to database per application
 * @author jonbr
 */
public class ApplicationDbContext 
{
    private ConnectionConfig config;
    private static Connection connection;
    private static ApplicationDbContext instance;
    
    /**
     * Retrieves connection (if available)
     * @return 
     */
    public static Connection getConnection() { return connection; }
    
    public static ApplicationDbContext getInstance() { return instance; }
    
    public ApplicationDbContext() throws SQLException
    {
        instance = this;
    }
    
    public void initialize(ConnectionConfig config) throws ClassNotFoundException, SQLException
    {
        this.config = config;
        
        // We must close existing connection due to singleton approach
        // should not have more than 1 connection per running application
        if(connection != null)
            connection.close();
        
        registerDriver();
        establishConnection();
    }
    
    private void registerDriver() throws ClassNotFoundException
    {
        Class.forName(config.getDriverRegistration());
    }
    
    private void establishConnection() throws SQLException
    {
        this.connection = DriverManager.getConnection(config.getConnectionString(), config.getUsername(), config.getPassword());
    }
    
    /**
     * Disposes resources used by this instance
     * @throws SQLException 
     */
    public void dispose() throws SQLException
    {
        if(connection != null)
            connection.close();
    }
}
