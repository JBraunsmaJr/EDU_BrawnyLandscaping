package util;

import configuration.AppConfig;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;
import java.util.logging.FileHandler;

/**
 * Implements a file-logging system based on today's date (yyyy-MM-dd)
 * The configuration consumed by this class is  "Log-level" which can have the following values
 *
 * All,
 * Config,
 * Warning,
 * Info,
 * Severe,
 * Fine,
 * Finest
 *
 * Or leave empty for a default log level of all
 */
public class Logging
{
    /**
     * Logger for this class, similar to ILogger in C#
     */
    private static final Logger log = Logger.getLogger(Logging.class.getName());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // This way it is already initialized when someone calls for it
    private static Logging instance = new Logging();

    FileHandler fileHandler;

    public Logging()
    {
        initialize();
    }

    /**
     * Logs using config level
     * @param obj
     */
    public static void config(Object obj)
    {
        if(obj == null)
            return;

        log.config(obj.toString());
    }

    /**
     * Logs using info level
     * @param obj
     */
    public static void info(Object obj)
    {
        if(obj == null)
            return;

        log.info(obj.toString());
    }

    /**
     * Logs using severe level
     * @param obj
     */
    public static void severe(Object obj)
    {
        if(obj == null)
            return;

        log.severe(obj.toString());
    }

    /**
     * Logs using fine level
     * @param obj
     */
    public static void fine(Object obj)
    {
        if(obj == null)
            return;

        log.fine(obj.toString());
    }

    /**
     * Logs using finest level
     * @param obj
     */
    public static void finest(Object obj)
    {
        if(obj == null)
            return;

        log.finest(obj.toString());
    }

    /**
     * Logs using warning level
     * @param obj
     */
    public static void warning(Object obj)
    {
        if(obj == null)
            return;

        log.warning(obj.toString());
    }

    private void initialize()
    {
        LocalDate date = LocalDate.now();
        String logFileLocation = AppConfig.getInstance().fromRootDir("logs");

        File file = new File(logFileLocation);
        file.mkdirs(); // ensure the path exists
        file = new File(logFileLocation + File.separator + dateFormatter.format(date) + ".log");

        try
        {
            if(!file.exists())
                file.createNewFile();

            fileHandler = new FileHandler(logFileLocation + File.separator + dateFormatter.format(date) + ".log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);

            String logLevel = "all";

            if(AppConfig.getInstance().hasVar("Log-level"))
                logLevel = AppConfig.getInstance().getConfigVar("Log-level");

            switch(logLevel.toLowerCase())
            {
                case "warning":
                    log.setLevel(Level.WARNING);
                    break;
                case "info":
                    log.setLevel(Level.INFO);
                    break;
                case "severe":
                    log.setLevel(Level.SEVERE);
                    break;
                case "fine":
                    log.setLevel(Level.FINE);
                    break;
                case "finest":
                    log.setLevel(Level.FINEST);
                    break;
                default:
                case "all":
                    log.setLevel(Level.ALL);
                    break;
                case "config":
                    log.setLevel(Level.CONFIG);
                    break;
            }

            log.addHandler(fileHandler);
            Logging.config("Logging initialized");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
