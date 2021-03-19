/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.validation;

import java.util.ArrayList;

/**
 *
 * @author jonbr
 */
public class ValidationResult 
{
    private ArrayList<String> errors;
    
    /**
     *
     * @param errors
     */
    public ValidationResult(ArrayList<String> errors)
    {
        this.errors = errors;
    }
    
    /**
     *
     * @return
     */
    public boolean isValid() { return errors.size() == 0; }

    /**
     *
     * @return
     */
    public ArrayList<String> getErrors() { return errors; }
}
