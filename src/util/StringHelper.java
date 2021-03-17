/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author jonbr
 */
public class StringHelper 
{
    public static String repeat(String text, int amount, String delimeter)
    {
        String[] collection = new String[amount];
        for(int i = 0; i < amount; i++)
            collection[i] = text;
        
        return String.join(delimeter, collection);
    }
}
