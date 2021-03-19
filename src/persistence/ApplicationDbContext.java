/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import models.*;
import orm.ConnectionConfig;
import orm.DbContext;
import orm.DbSet;

import java.sql.*;

/**
 * Singleton class - should only have 1 connection to database per application
 * @author jonbr
 */
public class ApplicationDbContext extends DbContext
{
    private static ApplicationDbContext instance;
    
    /**
     * Retrieves connection (if available)
     * @return 
     */

    public static ApplicationDbContext getInstance() { return instance; }

    public final DbSet<Customer> customers=  new DbSet<>(Customer.class);
    public final DbSet<Address> addresses = new DbSet<>(Address.class);
    public final DbSet<Product> products = new DbSet<>(Product.class);
    public final DbSet<CustomerOrder> orders = new DbSet<>(CustomerOrder.class);
    public final DbSet<OrderItem> orderItems = new DbSet<>(OrderItem.class);

    public ApplicationDbContext(ConnectionConfig config) throws SQLException
    {
        super(config);
        instance = this;

        initialize();
    }


}
