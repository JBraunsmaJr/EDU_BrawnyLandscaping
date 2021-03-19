package orm.annotations;

/**
 *  If enabled, gives an entity the ability to populate
 *  an array of objects dependent
 */
public @interface BackReferences
{
    public Class<?> backRefClass();
}
