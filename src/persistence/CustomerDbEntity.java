/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.util.ArrayList;
import java.sql.*;
import models.Address;
import models.Customer;

/**
 *
 * @author jonbr
 */
public class CustomerDbEntity 
{    
    /**
     * Determines if all required fields are filled out
     * @param customer
     * @return 
     */
    public static boolean IS_VALID(Customer customer)
    {
        return (
                   !EntityHelper.IS_STRING_EMPTY(customer.getFirstName()) ||
                   !EntityHelper.IS_STRING_EMPTY(customer.getLastName()) ||
                   !EntityHelper.IS_STRING_EMPTY(customer.getEmail()) ||
                   !EntityHelper.IS_STRING_EMPTY(customer.getPhone())
                );
    }
    
    /**
     * Returns validation errors (if any)
     * @param customer
     * @return 
     */
    public static ArrayList<String> VALIDATE(Customer customer)
    {
        ArrayList<String> errors = new ArrayList<>();
        
        if(EntityHelper.IS_STRING_EMPTY(customer.getFirstName()))
            errors.add("First name is required");
        if(EntityHelper.IS_STRING_EMPTY(customer.getLastName()))
            errors.add("Last name is required");
        if(EntityHelper.IS_STRING_EMPTY(customer.getEmail()))
            errors.add("Email is required");
        if(EntityHelper.IS_STRING_EMPTY(customer.getPhone()))
            errors.add("Phone is required");
        
        return errors;
    }
    
    public static void CREATE_TABLE() throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "CREATE TABLE IF NOT EXISTS Customer ("
                + "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,"
                + "firstName VARCHAR(100) NOT NULL,"
                + "lastName VARCHAR(100) NOT NULL,"
                + "email VARCHAR(100),"
                + "phone VARCHAR(16)"
                + ")";
                
        Statement statement = connection.createStatement();
        statement.executeUpdate(text);
    }
    
    public static int INSERT_RECORD(Customer customer) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "INSERT INTO customer (firstName,lastName,email,phone) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(text, Statement.RETURN_GENERATED_KEYS);
        
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setString(3, customer.getEmail());
        statement.setString(4, customer.getPhone());
        
        statement.executeUpdate();
        ResultSet results = statement.getGeneratedKeys();
        
        if(results.next())
            return results.getInt(1);
        
        return -1;
    }
    
    public static void UPDATE_RECORD(Customer customer) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();       
        
        String text = "UPDATE customer SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setString(3, customer.getEmail());
        statement.setString(4, customer.getPhone());
        statement.setInt(5, customer.getId());
        
        statement.executeUpdate();
    }
    
    public static void DELETE_RECORD(int id) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "DELETE FROM customer WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        
        statement.setInt(1, id);
        
        statement.executeUpdate();
    }
    
    public static Customer GET_CUSTOMER(int id, boolean includeAddresses) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "SELECT id,firstName,lastName,email,phone FROM customer WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(text);
        
        statement.setInt(1, id);
        
        ResultSet results = statement.executeQuery();
        Customer customer = new Customer();
        
        while(results.next())
        {
            customer.setId(id);
            customer.setFirstName(results.getString("firstName"));
            customer.setLastName(results.getString("lastName"));
            customer.setEmail(results.getString("email"));
            customer.setPhone(results.getString("phone"));
        }
        
        if(includeAddresses)
        {
            ArrayList<Address> addresses = AddressDbEntity.GET_CUSTOMER_ADDRESSES(id);
            customer.getAddresses().addAll(addresses);
        }
        
        return customer;
    }
    
    public static ArrayList<Customer> GET_CUSTOMERS(boolean includeAddress) throws SQLException, Exception
    {
        Connection connection = EntityHelper.getConnection();
        
        String text = "SELECT id,firstName,lastName,email,phone FROM customer";
        PreparedStatement statement = connection.prepareStatement(text);
                
        ResultSet results = statement.executeQuery();
        
        ArrayList<Customer> customers = new ArrayList<>();
        
        while(results.next())
        {
            Customer customer = new Customer();
            
            customer.setId(results.getInt("id"));
            customer.setFirstName(results.getString("firstName"));
            customer.setLastName(results.getString("lastName"));
            customer.setEmail(results.getString("email"));
            customer.setPhone(results.getString("phone"));
            
            if(includeAddress)
            {
                var addresses = AddressDbEntity.GET_CUSTOMER_ADDRESSES(customer.getId());
                customer.getAddresses().addAll(addresses);
            }
            
            customers.add(customer);
        }
        
        return customers;
    }
}
