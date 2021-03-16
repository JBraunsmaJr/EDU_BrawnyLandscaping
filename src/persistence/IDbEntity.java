/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import validation.*;
import java.util.ArrayList;

/**
 *
 * @author jonbr
 */
public interface IDbEntity<TEntity> 
{
    /**
     * Determines if entity is valid
     * returns validation result indicating errors (if any)
     * @param entity
     * @return 
     */
    ValidationResult validate(TEntity entity);
    
    /**
     * Insert entity into database
     * @param entity
     * @return - generated primary key for specified entity
     */
    int insert(TEntity entity);
    
    /**
     * Update entity in database
     * @param entity 
     */
    void update(TEntity entity);
    
    /**
     * Delete entity with specified pk from database
     * @param primaryKey 
     */
    void delete(int primaryKey);
    
    /**
     * Attempt to find entity with specified pk
     * @param primaryKey
     * @return 
     */
    TEntity find(int primaryKey);
    
    /**
     * Retrieve all records of entity
     * @return 
     */
    ArrayList<TEntity> get();
}
