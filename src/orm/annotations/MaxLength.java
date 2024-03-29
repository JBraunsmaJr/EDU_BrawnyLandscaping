/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.annotations;


import java.lang.annotation.*;

/**
 * Determines the minimum length allowed for the provided field
 * @author jonbr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxLength 
{

    /**
     *
     * @return
     */
    public int length() default Integer.MAX_VALUE;
}
