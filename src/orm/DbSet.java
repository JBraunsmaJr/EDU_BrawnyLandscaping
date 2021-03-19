package orm;

import orm.Exceptions.ValidationException;
import orm.annotations.DatabaseType;
import util.*;
import orm.annotations.*;
import orm.validation.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.sql.*;

/**
 * Basic functionality for database entities
 * @param <TEntity>
 */
public class DbSet<TEntity> implements IDbEntity<TEntity>
{
    private Map<String, Field> fieldMap = new HashMap<String,Field>();
    private Field primaryKeyField;
    private DbContext context;

    /**
     * Class which represents this table entity
     */
    private final Class<TEntity> tableClass;
    
    /**
     *
     * @param tableClass
     */
    public DbSet(Class<TEntity> tableClass)
    {
        this.tableClass = tableClass;
        initializeCache();
    }

    /**
     * Sets the database context of this DbSet
     * @param context
     */
    public void setDbContext(DbContext context)
    {
        System.out.println("Setting context: " + context + ", " + tableClass.getName());
        this.context = context;
    }

    /**
     * Initializes the field cache map for this type
     * Identifies primary key
     */
    private void initializeCache()
    {
        for(Field field : tableClass.getDeclaredFields())
        {
            // ignore fields not meant to be in the database
            // ignore static fields
            if(field.isAnnotationPresent(NotMapped.class) ||
                    Modifier.isStatic(field.getModifiers()))
                continue;
            
            // must set it accessible otherwise we won't be able to get/set private fields
            // that are meant to be used in this application
            field.setAccessible(true);
            if(field.isAnnotationPresent(Id.class))
                primaryKeyField = field;
            
            fieldMap.put(field.getName(), field);
        }
    }
    
    /**
     * Retrieves what the name of the table should be (in the database)
     * @return
     */
    protected String getTableName()
    {        
        return tableClass.getSimpleName();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void validate(TEntity tEntity) throws ValidationException
    {
        Validator.validate(tEntity);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void createTable()
    {
        String tableName = getTableName();        
        
        StringBuilder builder = new StringBuilder();
        
        // start the creation statement
        builder.append("CREATE TABLE IF NOT EXISTS " + tableName + "(");

        // can have multiple fields
        ArrayList<String> fields = new ArrayList<>();

        // can have multiple foreign keys
        ArrayList<String> foreignKeys = new ArrayList<>();



        for(String name : fieldMap.keySet())
        {
            Field field = fieldMap.get(name);
            String isRequired = field.isAnnotationPresent(Required.class) ? "NOT NULL" : "";

            if(field.isAnnotationPresent(ForeignKey.class))
            {
                ForeignKey annotation = field.getAnnotation(ForeignKey.class);
                foreignKeys.add(String.format("FOREIGN KEY (%s) REFERENCES %s(%s) ", field.getName(), annotation.referenceClass().getSimpleName(), annotation.referenceField()));
            }

            // is the current field meant to be the primary key?
            if(field.isAnnotationPresent(Id.class))
                fields.add(String.format("%s %s AUTO_INCREMENT NOT NULL PRIMARY KEY", field.getName(), field.getType().getName()));
            else if(field.isAnnotationPresent(DatabaseType.class))
            {
                DatabaseType annotation = field.getAnnotation(DatabaseType.class);

                String definition = annotation.type().getName();

                // if the type requires a length -- add it within parenthesis
                if(annotation.type().requiresLength() && annotation.length() > 0)
                    definition += "(" + annotation.length() + ") ";

                fields.add(String.format("%s %s %s", field.getName(), definition, isRequired));
            }
            else
            {
                if(field.getType().equals(Double.TYPE) || field.getType().equals(Long.TYPE))
                    fields.add(String.format("%s DECIMAL(10,2) %s", field.getName(), isRequired));
                else if(field.getType().equals(Integer.TYPE))
                    fields.add(String.format("%s INT %s", field.getName(), isRequired));
                else if(field.getType().equals(Date.class))
                    fields.add(String.format("%s DATE %s", field.getName(), isRequired));
                else if(field.getType().equals(String.class))
                {
                    String dbType = "TEXT";
                    
                    // TODO: implement better method to determine which string type to utilize
                    if(field.isAnnotationPresent(MaxLength.class))
                    {
                        int length = field.getAnnotation(MaxLength.class).length();
                        dbType = "VARCHAR(" + length + ")";
                    }
                    
                    fields.add(String.format("%s %s %s", field.getName(), dbType, isRequired));
                }
            }
        }
        
        builder.append(String.join(",", fields));   // add all the field definitations

        if(foreignKeys.size() > 0)
            builder.append(", " + String.join(", ", foreignKeys));

        // finish the creation statement
        builder.append(")");
        System.out.println(builder.toString());
        try
        {
            Connection connection = context.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(builder.toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public int insert(TEntity tEntity) throws ValidationException
    {
        validate(tEntity);

        StringBuilder builder = new StringBuilder();
        
        ArrayList<Object> values = new ArrayList<>();
        ArrayList<String> fields = new ArrayList<>();
        
        builder.append("INSERT INTO " + getTableName() + " (" );

        try
        {
            // build out the columns
            for(String name : fieldMap.keySet())
            {
                if(fieldMap.get(name).isAnnotationPresent(Id.class))
                    continue;

                fieldMap.get(name).setAccessible(true);
                
                fields.add(name);
                values.add(fieldMap.get(name).get(tEntity));
            }
                        
            builder.append(String.join(",", fields) + ") VALUES (" + StringHelper.repeat("?", fields.size(), ",") + ")");
        
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            
            populateStatement(statement, fields, values);
            
            statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();
            
            if(results.next())
                return results.getInt(1);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return -1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update(TEntity tEntity) throws ValidationException
    {
        validate(tEntity);
        
        StringBuilder builder = new StringBuilder();
        
        ArrayList<Object> values = new ArrayList<>();
        ArrayList<String> fields = new ArrayList<>();
        
        builder.append(String.format("UPDATE %s SET ", getTableName()));

        try
        {
            for(String name : fieldMap.keySet())
            {
                Field field = fieldMap.get(name);
                if(field.isAnnotationPresent(Id.class))
                    continue;

                Object value = field.get(tEntity);
                values.add(value);

                fields.add(String.format("%s = ?", name));
            }
            
            Connection connection = context.getConnection();
            
            builder.append(String.join(",", fields));
            String whereClause = String.format("%s = %s", primaryKeyField.getName(), primaryKeyField.get(tEntity));
            PreparedStatement statement = connection.prepareStatement(String.format("%s WHERE %s", builder.toString(), whereClause));
            
            populateStatement(statement, fields, values);
            
            statement.executeUpdate();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void delete(Object primaryKey)
    {
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        fields.add(primaryKeyField.getName());
        values.add(primaryKey);

        try
        {
            String query = String.format("DELETE FROM %s WHERE %s = ?", getTableName(), primaryKeyField.getName());
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            populateStatement(statement, fields, values);
            statement.executeUpdate();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public TEntity find(Object primaryKey)
    {
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        fields.add(primaryKeyField.getName());
        values.add(primaryKey);

        try
        {
            String query = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), primaryKeyField.getName());
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            populateStatement(statement, fields, values);

            ResultSet results = statement.executeQuery();
            TEntity instance = (TEntity) tableClass.newInstance(); // I know it's deprecated...
            
            if(results.next())
            {
                for(String name : fieldMap.keySet())
                {
                    Field field = fieldMap.get(name);
                    field.set(instance, results.getObject(name));
                }
                
                return instance;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<TEntity> get()
    {
        ArrayList<TEntity> entities = new ArrayList<>();

        try
        {
            String query = String.format("SELECT * FROM %s", getTableName());
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet results = statement.executeQuery();
            
            while(results.next())
            {
                TEntity instance = (TEntity) tableClass.newInstance();

                for(String name : fieldMap.keySet())
                {
                    Field field = fieldMap.get(name);
                    field.set(instance, results.getObject(name));
                }                               
                
                entities.add(instance);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return entities;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<TEntity> get(Predicate<TEntity> condition)
    {
        ArrayList<TEntity> entities = new ArrayList<>();

        try
        {
            String query = String.format("SELECT * FROM %s", getTableName(), primaryKeyField.getName());
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            
            ResultSet results = statement.executeQuery();
            
            while(results.next())
            {
                TEntity instance = (TEntity) tableClass.newInstance();

                for(String name : fieldMap.keySet())
                {
                    Field field = fieldMap.get(name);
                    field.set(instance, results.getObject(name));
                }                               
                
                // only entities that pass the test
                // TODO: need to figure out how to move this into the query rather than post query
                if(condition.test(instance))
                    entities.add(instance);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return entities;
    }

    /**
     * Reusable function facilitating the population of preparedStatement with data from fields and values
     * @param statement prepared statement
     * @param fields (names of fields)
     * @param values (values of fields)
     */
    protected void populateStatement(PreparedStatement statement, ArrayList<String> fields, ArrayList<Object> values)
    {
        try
        {
            for(int i = 0; i < fields.size(); i++)
            {
                Object value = values.get(i);

                if(value.getClass().equals(String.class))
                    statement.setString(i+1, (String)value);
                else if(value.getClass().equals(Integer.class) || value.getClass().equals(Integer.TYPE))
                    statement.setInt(i+1, (int)value);
                else if(value.getClass().equals(Double.class) || value.getClass().equals(Double.TYPE))
                    statement.setDouble(i+1, (double)value);
                else if(value.getClass().equals(Long.class) || value.getClass().equals(Long.TYPE))
                    statement.setLong(i+1, (long)value);
                else if(value.getClass().equals(Boolean.class) || value.getClass().equals(Boolean.TYPE))
                    statement.setBoolean(i+1, (boolean)value);
                else if(value.getClass().equals(Date.class) || value.getClass().equals(java.sql.Date.class))
                    statement.setDate(i+1, (java.sql.Date)value);
                else 
                    statement.setObject(i+1, value);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
