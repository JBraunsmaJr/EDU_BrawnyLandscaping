/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import orm.annotations.*;

/**
 *
 * @author jonbr
 */
public class Address 
{
    @Id
    private int id;
    
    @Required
    private String street;
    
    @Required
    private String city;
    
    @Required
    private String state;
    
    @Required
    private String zip;

    @ForeignKey(referenceClass = Customer.class, backreferenceVariableName = "customer")
    private int customerId;
    
    @NotMapped
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

    @Override
    public String toString()
    {
        return String.format("%s %s %s %s",street, city, state, zip);
    }
}
