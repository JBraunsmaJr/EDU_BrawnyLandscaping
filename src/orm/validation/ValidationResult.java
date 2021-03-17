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
    private boolean valid;
    private ArrayList<String> errors;
    
    public ValidationResult(ArrayList<String> errors)
    {
        this.errors = errors;
        valid = errors.size() == 0;
    }
    
    public boolean isValid() { return valid; }
    public ArrayList<String> getErrors() { return errors; }
}