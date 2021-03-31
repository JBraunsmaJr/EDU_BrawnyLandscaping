package orm;

import orm.builders.ForeignKeyPair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract layer for connection to a database
 * @author jonbr
 */
public abstract class DbContext
{
    private ConnectionConfig config;
    private static Connection connection;
    private ArrayList<Field> tableCache;
    protected Map<Class<?>, DbSet<?>> tableMap;

    /**
     * Maps foreign key set for classes
     */
    protected Map<Class<?>, ArrayList<ForeignKeyPair>> foreignKeySet;

    /**
     * Create a DbContext with specified connection properties
     * @param config
     */
    public DbContext(ConnectionConfig config)
    {
        this.config = config;
        tableCache = new ArrayList<>();
        foreignKeySet = new HashMap<>();
        tableMap = new HashMap<>();
    }

    /**
     * Place to further define entity relationships
     */
    protected abstract void onModelCreating();

    /**
     * Determines if class has foreign key relationships
     * @param entityClass
     * @return
     */
    protected boolean hasForeignKeySet(Class<?> entityClass)
    {
        return foreignKeySet.containsKey(entityClass);
    }

    /**
     * Retrieves table for specified class
     * used during backref stage of querying
     * @param entityClass
     * @return
     */
    public DbSet<?> getTableFor(Class<?> entityClass)
    {
        if(tableMap.containsKey(entityClass))
            return tableMap.get(entityClass);
        return null;
    }

    /**
     * Get Foreign Key Set for entity
     * @param entity
     * @return
     */
    public ArrayList<ForeignKeyPair> getFKRelationshipsFor(Class<?> entity)
    {
        if(hasForeignKeySet(entity))
            return foreignKeySet.get(entity);

        return new ArrayList<>();
    }

    /**
     * Adds FK Pair to entityClass's fk set
     * @param entityClass
     * @param pair
     */
    protected void addFKPair(Class<?> entityClass, ForeignKeyPair pair)
    {
        if(!foreignKeySet.containsKey(entityClass))
            foreignKeySet.put(entityClass, new ArrayList<>());

        foreignKeySet.get(entityClass).add(pair);
    }

    /**
     * Initializes the context
     */
    public void initialize() throws SQLException
    {
        try
        {
            if(connection != null)
                connection.close();

            registerDriver();
            establishConnection();
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println(String.format("Error establishing connection:\n\n\t%s", ex));
        }

        for(Field field : this.getClass().getDeclaredFields())
            if(field.getType().isAssignableFrom(DbSet.class))
                if(!tableCache.contains(field))
                    tableCache.add(field);

        ensureCreated();

        onModelCreating();
    }

    /**
     * Attempts to create the database on the server (if it doesn't exist)
     * @throws SQLException
     */
    private void ensureDatabaseCreated() throws SQLException
    {
        System.out.println("Ensuring database has been created");
        Connection dbCon = DriverManager.getConnection(config.getServerString(), config.getUsername(), config.getPassword());
        Statement statement = dbCon.createStatement();
        statement.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS " + config.getDatabaseName()));
        dbCon.close();
    }

    /**
     * Attempts to ensure each table within this context has been created in the database
     */
    private void ensureCreated()
    {
        for(Field field : tableCache)
        {
            try
            {
                for(Method method : field.getType().getDeclaredMethods())
                    if(method.getName().equals("createTable"))
                        method.invoke(field.get(this));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();;
            }
        }

    }

    private void registerDriver() throws ClassNotFoundException
    {
        Class.forName(config.getDriverRegistration());
    }

    /**
     * Attempts to establish connection to the database using @config
     * @throws SQLException
     */
    protected void establishConnection() throws SQLException
    {
        ensureDatabaseCreated();
        this.connection = DriverManager.getConnection(config.getConnectionString(), config.getUsername(), config.getPassword());
    }

    /**
     * Retrieve current database connection (if applicable)
     * @return
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Should be called! this will clean up the connection resources
     * @throws SQLException
     */
    public void dispose() throws SQLException
    {
        if(connection != null)
            connection.close();
    }
}
