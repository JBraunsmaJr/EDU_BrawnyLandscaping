/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jonbr
 */
public class CustomComboBoxModel<TEntity> extends DefaultComboBoxModel<TEntity>
{
    private ArrayList<TEntity> items;
    
    public CustomComboBoxModel()
    {
      items = new ArrayList<>();   
    }
    
    public CustomComboBoxModel(Collection<? extends TEntity> collection)
    {
        items = new ArrayList<>();
        items.addAll(collection);
    }
    
    @Override
    public TEntity getSelectedItem()
    {
        TEntity entity = (TEntity) super.getSelectedItem();
        
        return entity;
    }
    
    
}
