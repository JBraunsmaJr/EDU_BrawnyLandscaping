/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author jonbr
 */
public class JStringList extends AbstractListModel
{
    ArrayList<String> model;
    
    public JStringList()
    {
        model = new ArrayList<>();
    }
    
    public Object getElementAt(int index)
    {
        return model.toArray()[index];
    }
    
    public int getSize()
    {
        return model.size();
    }
    
    public void add(String element)
    {
        if(model.add(element))
            fireContentsChanged(this, 0, getSize());
    }
    
    public void addAll(String elements[])
    {
        Collection<String> c = Arrays.asList(elements);
        model.addAll(c);
        fireContentsChanged(this, 0 , getSize());
    }
    
    public void clear() 
    {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }
    
    public boolean contains(String element)
    {
        return model.contains(element);
    }
    
    public Object firstElement()
    {
        if(getSize() == 0)
            return null;
        return getElementAt(0);
    }
    
    public Iterator iterator()
    {
        return model.iterator();
    }
    
    public Object lastElement()
    {
        return model.toArray()[getSize()-1];
    }
    
    public boolean removeElement(String element)
    {
        boolean removed = model.remove(element);
        if(removed)
        {
            fireContentsChanged(this, 0 , getSize());
        }
        return removed;
    }
    
    
}
