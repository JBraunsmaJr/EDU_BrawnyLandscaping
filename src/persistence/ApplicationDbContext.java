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
import orm.builders.ForeignKeyPair;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

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

    public final DbSet<Customer> customers;
    public final DbSet<Address> addresses;
    public final DbSet<Product> products;
    public final DbSet<CustomerOrder> orders;
    public final DbSet<OrderItem> orderItems;

    public ApplicationDbContext(ConnectionConfig config) throws SQLException
    {
        super(config);
        instance = this;
        System.out.println("Initializing ApplicationDbContext...");

        customers   =  new DbSet<>(Customer.class, this);
        addresses   = new DbSet<>(Address.class, this);
        products    = new DbSet<>(Product.class, this);
        orders      = new DbSet<>(CustomerOrder.class, this);
        orderItems  = new DbSet<>(OrderItem.class, this);

        initialize();
    }

    @Override
    protected void onModelCreating()
    {
        // TODO: Is there a better way of mapping these without manually specifying
        tableMap.put(Customer.class, customers);
        tableMap.put(Address.class, addresses);
        tableMap.put(CustomerOrder.class, orders);
        tableMap.put(Product.class, products);
        tableMap.put(OrderItem.class, orderItems);


        addFKPair(Address.class, new ForeignKeyPair(Address.class, Customer.class, "id", "customerId", "customer"));
        addFKPair(Customer.class, new ForeignKeyPair(Customer.class, Address.class, "customerId", "id", "addresses"));
        addFKPair(CustomerOrder.class, new ForeignKeyPair(CustomerOrder.class, Address.class, "id", "addressId", "address"));
        addFKPair(OrderItem.class, new ForeignKeyPair(OrderItem.class, CustomerOrder.class, "id", "orderId", "customerOrder"));
        addFKPair(OrderItem.class, new ForeignKeyPair(OrderItem.class, Product.class, "id", "productId", "product"));
    }

}
