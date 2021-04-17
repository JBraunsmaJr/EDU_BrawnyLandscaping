/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import util.Logging;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import orm.ConnectionConfig;


/**
 *
 * @author jonbr
 */
public class AppConfig 
{
    private Map<String, String> Configuration = new HashMap<String,String>();
    
    private static AppConfig instance = new AppConfig();
    
    /**
     * Singleton -- shouldn't have more than 1 configuration file loaded at a time
     * @return 
     */
    public static AppConfig getInstance() 
    {
        return instance;
    }
    
    public AppConfig() 
    {
        instance = this;
        
        try
        {
            Load();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public String getConfigVar(String key)
    {
        if(Configuration.containsKey(key))
            return Configuration.get(key);
        
        return null;
    }

    public boolean hasVar(String key)
    {
        return Configuration.containsKey(key);
    }
    
    private void setConfigVar(String key, String value)
    {
        Configuration.put(key, value);
    }   

    /**
     * If the configuration file does not exist
     * Populate the configuration file with default values
     */
    private void initializeDefaultValues()
    {
        setConfigVar("RootDirectory", "BrawnyLandscapingData");
        setConfigVar("ContentDirectory", "images");
        
        /**
         *  Database Configuration that is available by default 
         */
        setConfigVar("Database-ServerName", "localhost");
        setConfigVar("Database-Port", "3306");
        setConfigVar("Database-Name", "brawnylandscapingdb");
        setConfigVar("Database-Username", "root");
        setConfigVar("Database-Password", "devry123");
        setConfigVar("Log-level", "all");

        File root = new File(getConfigVar("RootDirectory"));
        root.mkdirs();

        File images = new File(getContentDir());
        images.mkdirs();                
    }
    
    /**
     * Create connection configuration based on values within configuration file
     * @return 
     */
    public ConnectionConfig getConnectionConfig()
    {
        return new ConnectionConfig(
                                        getConfigVar("Database-ServerName"), 
                                        getConfigVar("Database-Name"), 
                                        getConfigVar("Database-Username"), 
                                        getConfigVar("Database-Password"), 
                                        Integer.parseInt(getConfigVar("Database-Port"))
                                   );
    }
    
    public String getContentDir()
    {
        return getConfigVar("RootDirectory") + File.separator + getConfigVar("ContentDirectory");
    }

    public String getRootDir()
    {
        return getConfigVar("RootDirectory");
    }

    public String fromRootDir(String relativePath)
    {
        return getRootDir() + File.separator + relativePath;
    }

    public String fromRootDir(String ... relativePath)
    {
        return getRootDir() + File.separator + String.join(File.separator, relativePath);
    }
    
    private void saveConfigurations()            
    {
        String currentWorkingDir = System.getProperty("user.dir");

        File configFile = new File(currentWorkingDir + File.separator + "BrawnyLandscapingConfig.cfg");
                
        try
        {
            configFile.createNewFile();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            
            for(String key : Configuration.keySet())
                writer.write(String.format("%s=%s\n", key, Configuration.get(key)));
            
            writer.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Loads configuration
     * @throws IOException
     * @throws FileNotFoundException 
     */
    public void Load() throws IOException, FileNotFoundException
    {
        Configuration.clear();
        initializeDefaultValues();
        
        // get current working directory of the application
        String currentWorkingDir = System.getProperty("user.dir");

        File configFile = new File(currentWorkingDir + File.separator + "BrawnyLandscapingConfig.cfg");
        
        if(!configFile.exists())
            saveConfigurations();
        else
            configFile.createNewFile();
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            String line;
            while((line = reader.readLine()) != null)
            {
                System.out.println("Config Consuming Value: " + line);
                String[] split = line.split("=");
                // Replace any quotation marks in the value
                // as we don't give a hoot about "quotation" marks
                setConfigVar(split[0], split[1].replace("\"", ""));
            }

            reader.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
