/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import annotations.Required;

/**
 *
 * @author jonbr
 */
public class Address 
{
    public static final String TABLE_NAME = "Address";
        
    private int id;
    
    @Required(errorMessage = "Street is required")
    private String street;
    
    @Required(errorMessage = "City is required")
    private String city;
    
    @Required(errorMessage = "State is required")
    private String state;
    
    @Required(errorMessage = "Zip is required")
    private String zip;
        
    private int customerId;
    private Customer customer;
    
    public void setId(int value) { id = value;}
    public void setStreet(String value) { street = value; }    
    public void setCity(String value) { city = value; }
    public void setState(String value) { state = value; }
    public void setZip(String value) { zip = value; }
    public void setCustomerId(int id) { customerId = id; }
    public void setCustomer(Customer customer) { customer = customer; }
    
    public int getId() { return id;}
    public int getCustomerId() { return customerId; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
    public Customer getCustomer() { return customer; }
}
