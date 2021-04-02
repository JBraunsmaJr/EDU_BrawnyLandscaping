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
public class Product 
{
    @Id
    private int id;
    
    @Required
    private String name;
    
    @Required
    private String description;
    private double price;
    private boolean requiresDimensions;
    private String imagePath;
    
    public String getImagePath() { return imagePath; }
    public boolean getRequiresDimensions() { return requiresDimensions; }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    
    public void setImagePath(String value) { imagePath = value; }
    public void setRequiresDimensions(boolean value) { requiresDimensions = value; }
    public void setId(int value) { id = value; }
    public void setName(String value) { name = value; }
    public void setDescription(String value) { description = value; }
    public void setPrice(double value) { price = value; }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        if(id > 0)
            builder.append("Id: " + id + "\n");
        
        builder.append("Name: " + name + "\n");
        builder.append("Desc: " + description + "\n");
        builder.append("Price: " + price + "\n");
        builder.append("Requires Dims: " + requiresDimensions + "\n");
        builder.append("Image Path: " + imagePath + "\n\n");
        
        return builder.toString();
    }
}
