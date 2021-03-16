/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validation;

import annotations.Required;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.ArrayList;

/**
 *
 * @author jonbr
 */
public class Validator 
{
    public static ValidationResult validate(Object object)
    {        
        ArrayList<String> errors = new ArrayList<>();

        if(Objects.isNull(object))
            return new ValidationResult(errors);
        
        Class<?> cls = object.getClass();
        
        for(Field field : cls.getFields())
        {
            if(field.isAnnotationPresent(Required.class))
            {
                field.setAccessible(true);
                Required required = field.getAnnotation(Required.class);
                
                try
                {
                    Object obj = field.get(object);
                    
                    if(Objects.isNull(obj) || (obj.toString().isBlank() || obj.toString().isEmpty()))
                        errors.add(required.errorMessage());
                }
                catch(Exception ex)
                {
                    
                }
            }
        }
        
        return new ValidationResult(errors);
    }
}
