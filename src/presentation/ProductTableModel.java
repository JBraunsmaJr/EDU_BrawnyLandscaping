/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import models.Product;
import util.Logging;

/**
 *
 * @author jonbr
 */
public class ProductTableModel extends AbstractTableModel
{
    protected ArrayList<Product> products = new ArrayList<Product>();
    
    protected String[] columnNames = new String[]
    {
        "Name", "Description", "Unit Price", "Requires Dimensions"
    };
    
    protected Class[] columnClasses = new Class[]
    {
        String.class, String.class, Double.class, Boolean.class
    };
    
    public ProductTableModel()
    {
        super();
    }
    
    
    @Override
    public int getColumnCount() { return 4; }
    
    @Override
    public int getRowCount() 
    { 
        if(products == null)
            products = new ArrayList<>();
        
        return products.size(); 
    }
    
    /**
     * Everything in the table should be editable
     * @param row
     * @param col
     * @return 
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col)
    {
        Product item = (Product) products.toArray()[row];
        
        if(value != null)
            Logging.warning(value.getClass().getSimpleName());

        switch(col)
        {
            case 0:
                item.setName(value.toString());
                break;
            case 1:
                item.setDescription(value.toString());
                break;
            case 2:
                item.setPrice((double)value);
                break;
            case 3:
                item.setRequiresDimensions((boolean)value);
                break;
            default:
                Logging.severe(String.format("Was unable to update record on row %s, col %s, with %s", row, col, value));
                break;
        }
        
        // This must get called so anything listening knows value was changed
        fireTableDataChanged();
    }
    
    /**
     * Update the image path for specified product row
     * @param row
     * @param path 
     */
    public void setImagePathFor(int row, String path)
    {
        if(!(row >= 0 && row < products.size()))
            return;
        
        Product item = (Product)products.toArray()[row];
        item.setImagePath(path);
    }
    
    /**
     * Retrieves the image path for specified product row
     * @param row
     * @return 
     */
    public String getImagePathFor(int row)
    {
        if(!(row >= 0 && row < products.size()))
            return "";
        
        Product item = (Product)products.toArray()[row];
        return item.getImagePath();
    }
    
    @Override
    public Class getColumnClass(int col) { return columnClasses[col]; }
    
    @Override
    public String getColumnName(int col) { return columnNames[col]; }
    
    @Override
    public Object getValueAt(int row, int col)
    {
        if(!(row >= 0 && row < products.size()))
            return null;
        
        Product item = products.get(row);
        
        switch(col)
        {
            case 0:
                return item.getName();
            case 1:
                return item.getDescription();
            case 2: 
                return item.getPrice();
            case 3:
                return item.getRequiresDimensions();
            default:
                Logging.severe(String.format("%s is an invalid column", col));
                return null;
        }               
    }    
            
    public void add(Product product) 
    {
        products.add(product);
        
        Vector<Object> rowVector = new Vector<>();
        rowVector.add(product.getName());
        rowVector.add(product.getDescription());
        rowVector.add(product.getPrice());
        rowVector.add(product.getRequiresDimensions());
        
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }
    
    public void remove(int index)
    {
        products.remove(index);
        fireTableDataChanged();
        //super.removeRow(index);
    }
    
    public void addRange(Collection<Product> products)
    {
        this.products.addAll(products);
    }
    
    public ArrayList<Product> getProducts() { return products; }
}
