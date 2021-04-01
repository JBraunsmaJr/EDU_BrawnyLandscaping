/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 *
 * @author jonbr
 */
public class AppConfig 
{
    private Map<String, String> Configuration = new HashMap<String,String>();
    
    private static AppConfig instance;
    
    /**
     * Singleton -- shouldn't have more than 1 configuration file loaded at a time
     * @return 
     */
    public static AppConfig getInstance() 
    {
        if(instance == null)
            instance = new AppConfig();
        
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
    
    private void setConfigVar(String key, String value)
    {
        Configuration.put(key, value);
    }   

    private void initializeDefaultValues()
    {
        setConfigVar("RootDirectory", "BrawnyLandscapingData");
        setConfigVar("ContentDirectory", "images");
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
        //Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        
        File configFile = new File(currentWorkingDir + File.separator + "BrawnyLandscapingConfig.cfg");
        
        boolean created = configFile.createNewFile();
        
        if(created)
            System.out.println("Loading BrawnyLandscapingConfig.cfg");            
        
        AppConfig config = new AppConfig();
        
        try
        {
            Scanner reader = new Scanner(configFile);
            
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                String[] split = line.split("=");
                
                // Replace any quotation marks in the value
                // as we don't give a hoot about "quotation" marks
                setConfigVar(split[0], split[1].replace("\"", ""));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
