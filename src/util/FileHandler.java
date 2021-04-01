/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jonbr
 */
public class FileHandler 
{
    /**
     * Attempts to load image from disk
     * @param image
     * @return 
     */
    public static BufferedImage LoadImage(String image)
    {
        BufferedImage img = null;
        
        try
        {
            img = ImageIO.read(new File(image));
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        return img;
    }
}
