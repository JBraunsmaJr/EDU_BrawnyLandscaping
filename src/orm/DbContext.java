package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DbContext
{
    private ConnectionConfig config;
    private static Connection connection;

    public DbContext(ConnectionConfig config)
    {
        initialize(config);
    }

    /**
     * Initializes the context
     * @param config
     */
    public void initialize(ConnectionConfig config)
    {
        this.config = config;

        try
        {
            if(connection != null)
                connection.close();

            registerDriver();
            establishConnection();
        }
        catch(SQLException ex)
        {
            System.err.println("Error while attempting to close database connection");
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println(String.format("Error establishing connection:\n\n\t%s", ex));
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
