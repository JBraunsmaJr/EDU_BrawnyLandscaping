/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm;

/**
 *
 * @author jonbr
 */
public class ConnectionConfig 
{
    private String username;
    private String password;
    private String hostname;
    private String databaseName;
    private int port;
    
    /**
     * String required to register driver
     */
    private final String driverRegistrationString = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Use default values
     */
    public ConnectionConfig()
    {
        // set default values
        hostname = "localhost";
        port = 3306;
        username = "root";
    }
    
    /**
     * Create config for specified username and password
     * @param username
     * @param password 
     */
    public ConnectionConfig(String username, String password)
    {
        this();
        this.username = username;
        this.password = password;
    }
    
    /**
     * Create config for specified username, password and port
     * @param username
     * @param password
     * @param port 
     */
    public ConnectionConfig(String username, String password, int port)
    {
        this(username, password);
        this.port = port;
    }
    
    /**
     * Create config for specified username, password, port, and database 
     * @param username
     * @param password
     * @param databaseName
     * @param port 
     */
    public ConnectionConfig(String username, String password, String databaseName, int port)
    {
        this(username,password,port);
        this.databaseName = databaseName;
    }

    public ConnectionConfig(String hostname, String databaseName, String username, String password, int port)
    {
        this(username,password,databaseName,port);
        this.hostname = hostname;
    }
    
    /**
     * Username to use when connecting to database
     * @return 
     */
    public String getUsername() { return username; }
    
    /**
     * Password to use when connecting to database
     * @return 
     */
    public String getPassword() { return password; }
    
    /**
     * Generates connection string using username/password in this instance
     * @return 
     */
    public String getConnectionString() { return String.format("jdbc:mysql://%s:%s/%s", hostname, port, databaseName); }
    
    /**
     * Retrieves string required to register driver
     * @return 
     */
    public String getDriverRegistration() { return driverRegistrationString; }
}
