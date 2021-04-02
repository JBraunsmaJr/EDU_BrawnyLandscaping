/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.util.ArrayList;

/**
 * Enforced functionality between any panel that interfaces with a table
 * @author jonbr
 */
public interface IDbPanel<TEntity>
{
    boolean isDirty = false;
    void setDirty(boolean value);
    void onItemSelected(int index);
    void delete();
    void save();
    ArrayList<TEntity> getAllItems();
    void resetItemData();
}
