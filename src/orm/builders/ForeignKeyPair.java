package orm.builders;

import java.lang.reflect.Field;

public class ForeignKeyPair
{
    /**
     * Class of foreign entity
     */
    public final Class<?> foreignEntityClass;

    /**
     * Field from foreign entity
     */
    public final Field foreignPKField;

    /**
     * Field on entity which references foreign object(s)
     */
    public final Field referenceField;

    /**
     * Field on entity which contains the foreign key
     */
    public final Field entityForeignKeyField;

    /**
     *
     * @param baseEntityClass (class of entity)
     * @param foreignEntityClass (class of foreign entity)
     * @param foreignEntityKeyFieldName (name of field within foreignEntityClass)
     * @param baseEntityFKName (field ON entity which contains the foreign key value)
     * @param baseEntityRefFieldName (field on entity that references foreign entity)
     */
    public ForeignKeyPair(Class<?> baseEntityClass, Class<?> foreignEntityClass, String foreignEntityKeyFieldName, String baseEntityFKName, String baseEntityRefFieldName)
    {
        Field foreignKeyFieldFound;
        Field baseEntityFKField;
        Field baseEntityRefField;

        try
        {
            foreignKeyFieldFound = foreignEntityClass.getDeclaredField(foreignEntityKeyFieldName);
            foreignKeyFieldFound.setAccessible(true);

            baseEntityFKField = baseEntityClass.getDeclaredField(baseEntityFKName);
            baseEntityRefField = baseEntityClass.getDeclaredField(baseEntityRefFieldName);

            baseEntityFKField.setAccessible(true);
            baseEntityRefField.setAccessible(true);
        }
        catch(NoSuchFieldException ex)
        {
            ex.printStackTrace();
            foreignKeyFieldFound = null;
            baseEntityFKField = null;
            baseEntityRefField = null;
        }

        /**
         * Java, unlike C#, does not allow you to work with private/protected fields
         * So to ensure the system properly sets/gets values as needed
         * the fields accessbility will be set to true
         */


        foreignPKField = foreignKeyFieldFound;
        this.foreignEntityClass = foreignEntityClass;
        this.referenceField = baseEntityRefField;
        this.entityForeignKeyField = baseEntityFKField;
    }
}
