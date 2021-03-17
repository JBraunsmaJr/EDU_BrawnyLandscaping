/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import orm.annotations.Required;

/**
 *
 * @author jonbr
 */
public class Product 
{
    private int id;
    
    @Required(errorMessage = "Name is required")
    private String name;
    
    @Required(errorMessage = "Description is required")
    private String description;
    private double price;
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    
    public void setId(int value) { id = value; }
    public void setName(String value) { name = value; }
    public void setDescription(String value) { description = value; }
    public void setPrice(double value) { price = value; }
}
