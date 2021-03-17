/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.validation;

import orm.annotations.Required;
import java.lang.reflect.Field;
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

        for(Field field : cls.getDeclaredFields()) // declared fields will include private ones
        {
            if(field.isAnnotationPresent(Required.class))
            {
                field.setAccessible(true);
                Required required = field.getAnnotation(Required.class);
                
                String message = required.errorMessage();
                
                if(required.errorMessage().isEmpty())
                    message = String.format("%s is required", field.getName());
                
                try
                {
                    Object obj = field.get(object);
                    
                    if(Objects.isNull(obj) || (obj.toString().isBlank() || obj.toString().isEmpty()))
                        errors.add(message);
                }
                catch(Exception ex)
                {
                    
                }
            }
        }
        
        return new ValidationResult(errors);
    }
}
