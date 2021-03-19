package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Abstract layer for connection to a database
 * @author jonbr
 */
public abstract class DbContext
{
    private ConnectionConfig config;
    private static Connection connection;
    private ArrayList<Field> tableCache;

    /**
     * Create a DbContext with specified connection properties
     * @param config
     */
    public DbContext(ConnectionConfig config)
    {
        this.config = config;
        tableCache = new ArrayList<>();
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
        {
            if(field.getType().isAssignableFrom(DbSet.class))
            {
                try
                {
                    if(!tableCache.contains(field));
                        tableCache.add(field);

                    // For some reason the find method function DOES NOT LOCATE this setDbContext....
                    for(Method method : field.getType().getDeclaredMethods())
                        if(method.getName().equals("setDbContext"))
                        {
                            method.invoke(field.get(this), this);
                            break;
                        }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        ensureCreated();
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
