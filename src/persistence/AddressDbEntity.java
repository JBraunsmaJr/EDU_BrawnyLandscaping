/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;
import java.sql.*;
import java.util.ArrayList;
import models.Address;

/**
 *
 * @author jonbr
 */
public class AddressDbEntity 
{
    /**
     * Are all the required fields filled out?
     * @param address
     * @return 
     */
    public static boolean IS_VALID(Address address)
    {
        return (!EntityHelper.IS_STRING_EMPTY(address.getStreet()) ||
                !EntityHelper.IS_STRING_EMPTY(address.getCity())   ||
                !EntityHelper.IS_STRING_EMPTY(address.getState())  ||
                !EntityHelper.IS_STRING_EMPTY(address.getZip()));            
    }
    
    /**
     * Returns orm.validation errors (if any)
     * @param address
     * @return 
     */
    public static ArrayList<String> VALIDATE(Address address)
    {
        ArrayList<String> errors = new ArrayList<>();
        
        if(EntityHelper.IS_STRING_EMPTY(address.getStreet()))
            errors.add("Street is required");
        if(EntityHelper.IS_STRING_EMPTY(address.getCity()))
            errors.add("City is required");
        if(EntityHelper.IS_STRING_EMPTY(address.getState()))
            errors.add("State is required");
        if(EntityHelper.IS_STRING_EMPTY(address.getZip()))
            errors.add("Zip is required");
        
        return errors;
    }
    /**
     * Ensures the address table has been created
     * @throws SQLException
     * @throws Exception 
     */
    public static void CREATE_TABLE() throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "CREATE TABLE IF NOT EXISTS Address ("
                + "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,"
                + "street VARCHAR(100) NOT NULL,"
                + "city VARCHAR(100) NOT NULL,"
                + "state VARCHAR(100) NOT NULL,"
                + "zip VARCHAR(16) NOT NULL,"
                + "customerId int NOT NULL,"
                + "FOREIGN KEY (customerId) REFERENCES customer(id)"
                + ")";
        
        Statement statement = connection.createStatement();
        statement.executeUpdate(text);
    }
    
    /**
     * Inserts hydrated instance of address into the database
     * @param address
     * @throws SQLException
     * @throws Exception 
     */
    public static int INSERT_RECORD(Address address) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "INSERT INTO address (street, city, state, zip, customerId) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(text, Statement.RETURN_GENERATED_KEYS);
        
        statement.setString(1, address.getStreet());
        statement.setString(2, address.getCity());
        statement.setString(3, address.getState());
        statement.setString(4, address.getZip());
        statement.setInt(5, address.getCustomerId());
        
        statement.executeUpdate();
        ResultSet results = statement.getGeneratedKeys();
        
        if(results.next())
            return results.getInt(1);
        
        return -1;
    }
    
    public static void DELETE_RECORD(int id) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "DELETE FROM address WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        statement.setInt(1, id);
        
        statement.executeUpdate();
    }
    
    /**
     * Deletes all addresses with the associated customer id
     * @param customerId
     * @throws SQLException
     * @throws Exception 
     */
    public static void DELETE_RECORD_WITH_CUSTOMER_ID(int customerId) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "DELETE FROM address WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        statement.setInt(1, customerId);
        statement.executeUpdate();
    }
    
    /**
     * Updates address with the hydrated instance of Address
     * @param address
     * @throws SQLException
     * @throws Exception 
     */
    public static void UPDATE_RECORD(Address address) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "UPDATE address SET street = ?, city = ?, state = ?, zip = ?, customerId = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        
        statement.setString(1, address.getStreet());
        statement.setString(2, address.getCity());
        statement.setString(3, address.getState());
        statement.setString(4, address.getZip());
        statement.setInt(5, address.getCustomerId());
        statement.setInt(6, address.getId());
        
        statement.executeUpdate();
    }
    
    /**
     * Retrieve address with specified primary key
     * Should query hydrate the customer property
     * @param id
     * @param includeCustomer
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static Address GET_ADDRESS(int id, boolean includeCustomer) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "SELECT id,street,city,state,zip,customerId FROM address WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        
        statement.setInt(1, id);
        
        ResultSet results = statement.executeQuery();
        Address address = new Address();
        
        while(results.next())
        {
            address.setId(id);
            address.setStreet(results.getString("street"));
            address.setCity(results.getString("city"));
            address.setState(results.getString("state"));
            address.setZip(results.getString("zip"));
            address.setCustomerId(results.getInt("customerId"));
        }
        
        if(includeCustomer)
        {
            address.setCustomer(CustomerDbEntity.GET_CUSTOMER(address.getCustomerId(), false));
        }
        
        return address;
    }
    
    /**
     * Retrieves all addresses in the system
     * @param includeCustomer
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static ArrayList<Address> GET_ADDRESSES(boolean includeCustomer) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "SELECT id,street,city,state,zip,customerId FROM address";
        PreparedStatement statement = connection.prepareStatement(text);
        
        ArrayList<Address> items = new ArrayList<>();
        ResultSet results = statement.executeQuery();
        
        while(results.next())
        {
            Address address = new Address();
            address.setId(results.getInt("id"));
            address.setCustomerId(results.getInt("customerId"));
            address.setStreet(results.getString("street"));
            address.setCity(results.getString("city"));
            address.setState(results.getString("state"));
            address.setZip(results.getString("zip"));
            
            if(includeCustomer)
                address.setCustomer(CustomerDbEntity.GET_CUSTOMER(address.getCustomerId(), false));
            
            items.add(address);
        }
        
        return items;
    }
    
    /**
     * Retrieves all addresses with the associated customer id
     * @param customerId
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static ArrayList<Address> GET_CUSTOMER_ADDRESSES(int customerId) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        String text = "SELECT id,street,city,state,zip,customerId FROM address WHERE customerId = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        
        statement.setInt(1, customerId);
        
        ArrayList<Address> items = new ArrayList<>();
        ResultSet results = statement.executeQuery();
        
        while(results.next())
        {
            Address address = new Address();
            address.setId(results.getInt("id"));
            address.setCustomerId(results.getInt("customerId"));
            address.setStreet(results.getString("street"));
            address.setCity(results.getString("city"));
            address.setState(results.getString("state"));
            address.setZip(results.getString("zip"));
            items.add(address);
        }
        
        return items;
    }
}
