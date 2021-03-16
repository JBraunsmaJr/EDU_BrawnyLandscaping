/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import annotations.*;

/**
 *
 * @author jonbr
 */
public class Customer 
{
    public final static String TABLE_NAME = "Customer";
    
    private int id;
    
    @Required(errorMessage = "First name is required")
    private String firstName;
    
    @Required(errorMessage = "Last name is required")
    private String lastName;
    
    @Required(errorMessage = "Email is required")
    private String email;
    
    @Required(errorMessage = "Phone is required")
    private String phone;
    
    private ArrayList<Address> addresses;
    
    public Customer()
    {
        addresses = new ArrayList<>();
    }
    
    /**
     * Adds address to instance
     * (does not update persistence model)
     * @param address 
     */
    public void addAddress(Address address) { addresses.add(address); }
    
    /**
     * Removes address from instance
     * (does not update persistence model)
     * @param address 
     */
    public void removeAddress(Address address) { addresses.remove(address); }
    
    /**
     * Retrieves addresses associated with this instance
     * (if populated during query)
     * @return 
     */
    public ArrayList<Address> getAddresses() { return addresses; }
    
    public void setId(int id) { this.id = id; }
    public void setFirstName(String value) { firstName = value; }
    public void setLastName(String value) { lastName = value; }
    public void setEmail(String value) { email = value; }
    public void setPhone(String value) { phone = value; }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getId() { return id; }
}
