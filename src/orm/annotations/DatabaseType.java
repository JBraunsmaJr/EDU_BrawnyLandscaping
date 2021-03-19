package orm.annotations;

import orm.models.DbType;

/**
 *
 * @author jonbr
 */
public @interface DatabaseType
{

    /**
     *
     * @return
     */
    public DbType type() default DbType.TEXT;

    /**
     *
     * @return
     */
    public int length() default 255;
}
