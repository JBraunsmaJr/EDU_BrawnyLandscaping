/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm;

import orm.Exceptions.ValidationException;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Basic contract required for interfacing with database tables
 * @author jonbr
 * @param <TEntity>
 */
public interface IDbEntity<TEntity> 
{
    /**
     * Determines if entity is valid
     * returns orm.validation result indicating errors (if any)
     * @param entity
     * @return 
     * @throws ValidationException
     */
    void validate(TEntity entity) throws ValidationException;

    /**
     * Ensure the creation of TEntity's table
     */
    void createTable();

    /**
     * Insert entity into database
     * @param entity
     * @return - generated primary key for specified entity
     * @throws ValidationException
     */
    int insert(TEntity entity) throws ValidationException;
    
    /**
     * Update entity in database
     * @param entity 
     * @throws ValidationException
     */
    void update(TEntity entity) throws ValidationException;
    
    /**
     * Delete entity with specified pk from database
     * @param primaryKey 
     */
    void delete(Object primaryKey);

    /**
     * Delete entity with specified where statement
     * @param sqlStatement
     */
    void deleteWhere(String sqlStatement);
    
    /**
     * Attempt to find entity with specified pk
     * @param primaryKey
     * @return 
     */
    TEntity find(Object primaryKey);

    /**
     * Attempt to find entity with specified pk
     * -- will attempt to fill backreference variables with matching names
     * @param primaryKey
     * @param include
     * @return
     */
    TEntity find(Object primaryKey, String... include);
    
    /**
     * Retrieve all records of entity
     * @return 
     */
    ArrayList<TEntity> get();

    /**
     * Retrieve all records of entity, also include back references with matching names
     * @param include
     * @return
     */
    ArrayList<TEntity> get(String... include);

    /**
     * Retrieve all records of entity which meet the provided condition
     * TODO: Figure out how to build SQL statement PRIOR to db call using predicates
     * @param condition
     * @return
     */
    ArrayList<TEntity> get(Predicate<TEntity> condition);

    /**
     * Retrieve all records of entity which meet the provided condition
     * Include backreferences with matching names
     * @param condition
     * @param include
     * @return
     */
    ArrayList<TEntity> get(Predicate<TEntity> condition, String... include);
}
