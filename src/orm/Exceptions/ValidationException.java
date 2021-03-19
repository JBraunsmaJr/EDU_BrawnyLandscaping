/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.Exceptions;

import orm.validation.ValidationResult;

/**
 * Exception thrown when something is not valid
 * @author jonbr
 */
public class ValidationException extends Exception 
{
    private ValidationResult result;
    
    /**
     * @param result 
     */
    public ValidationException(ValidationResult result)
    {
        this.result = result;
    }
    
    /**
     * Retrieves errors from orm.validation result
     * @return 
     */
    public String getErrors()
    {
        return String.join("\n", result.getErrors());
    }

    @Override
    public String toString() { return getErrors(); }
}
