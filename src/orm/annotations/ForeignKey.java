/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.annotations;

import java.lang.annotation.*;

/**
 *
 * @author jonbr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey 
{

    /**
     *
     * @return
     */
    public Class<?> referenceClass();

    /**
     *
     * @return
     */
    public String backreferenceVariableName() default "";

    /**
     *
     * @return
     */
    public String referenceField() default "id";
}
