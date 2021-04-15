/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.validation;

import orm.annotations.*;
import util.Logging;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Validation logic has been placed here
 * This processes all validation annotations on the object
 *
 * -- Must add validation logic for each annotation. Annotations are not allowed
 *    to have methods... so this is the workaround
 * @author jonbr
 */
public class Validator 
{

    /**
     *
     * @param object
     * @return
     */
    public static ValidationResult validate(Object object)
    {        
        ArrayList<String> errors = new ArrayList<>();

        if(Objects.isNull(object))
        {
            errors.add("Object cannot be null");
            return new ValidationResult(errors);
        }

        
        Class<?> cls = object.getClass();

        for(Field field : cls.getDeclaredFields()) // declared fields will include private ones
        {
            field.setAccessible(true);

            Object value = null;
            try { value = field.get(object); } catch (Exception ex) { ex.printStackTrace();}

            /**
             * Ensure the required field has some sort of value in it
             */
            if(field.isAnnotationPresent(Required.class))
            {
                Required required = field.getAnnotation(Required.class);
                
                String message = required.errorMessage();
                
                if(required.errorMessage().isEmpty())
                    message = String.format("%s is required", field.getName());
                
                if(Objects.isNull(value) || value.toString().isBlank() || value.toString().isEmpty())
                {
                    Logging.warning(String.format("Value: %s", value));
                    errors.add(message);
                }
            }

            /**
             * Ensure the value has the minimum character limit
             */
            if(field.isAnnotationPresent(MinLength.class))
            {
                MinLength minLength = field.getAnnotation(MinLength.class);
                if(value == null || value.toString().length() < minLength.length())
                    errors.add(String.format("%s minimum length of %s", field.getName(), minLength.length()));
            }

            /**
             * Ensure the value does not exceed maximum character limit
             */
            if(field.isAnnotationPresent(MaxLength.class))
            {
                MaxLength maxLength = field.getAnnotation(MaxLength.class);
                if(value == null || value.toString().length() > maxLength.length())
                    errors.add(String.format("%s maximum length of %s", field.getName(), maxLength.length()));
            }

            /**
             * Ensure the value does not dip below the minimum numeric value
             */
            if(field.isAnnotationPresent(Min.class))
            {
                Min min = field.getAnnotation(Min.class);
                boolean invalid = true;

                if(field.getType().equals(Integer.TYPE))
                    invalid = (int)value < min.value();
                else if(field.getType().equals(Double.TYPE))
                    invalid = (double)value < min.value();
                else if(field.getType().equals(Float.TYPE))
                    invalid = (float)value < min.value();
                else if(field.getType().equals(Long.TYPE))
                    invalid = (long)value < min.value();

                if(invalid)
                    errors.add(String.format("%s cannot be less than %s", field.getName(), min.value()));
            }

            /**
             * Ensure the value does not exceed the maximum numeric value
             */
            if(field.isAnnotationPresent(Max.class))
            {
                Max max = field.getAnnotation(Max.class);
                boolean invalid = true;

                if(field.getType().equals(Integer.TYPE))
                    invalid = (int)value > max.value();
                else if(field.getType().equals(Double.TYPE))
                    invalid = (double)value > max.value();
                else if(field.getType().equals(Float.TYPE))
                    invalid = (float)value > max.value();
                else if(field.getType().equals(Long.TYPE))
                    invalid = (long)value > max.value();

                if(invalid)
                    errors.add(String.format("%s cannot be more than %s", field.getName(), max.value()));
            }
        }
        
        return new ValidationResult(errors);
    }
}
