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

    private void initializeDefaultValues()
    {
        setConfigVar("RootDirectory", "BrawnyLandscapingData");
        setConfigVar("ContentDirectory", "images");

        File root = new File(getConfigVar("RootDirectory"));
        root.mkdirs();

        File images = new File(getContentDir());
        images.mkdirs();
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
        
        boolean created = configFile.createNewFile();
        
        if(created)
            System.out.println("Loading BrawnyLandscapingConfig.cfg");            
        
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
