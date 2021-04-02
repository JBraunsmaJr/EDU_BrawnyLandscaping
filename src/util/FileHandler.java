/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 *
 * @author jonbr
 */
public class FileHandler 
{
    /**
     * Copies file from original location to a destination directory
     * @param originalPath - original path of intended file
     * @param destinationDirectory - where the file should be copied to
     * @return UUID of file in destination directory
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String copyFileTo(String originalPath, String destinationDirectory) throws FileNotFoundException, IOException
    {
        InputStream input;
        OutputStream output;

        UUID id = UUID.randomUUID();
        String destinationFile = destinationDirectory + File.separator + id.toString();

        input = new FileInputStream(originalPath);
        output = new FileOutputStream(destinationFile);

        byte[] buffer = new byte[1024];
        int length;

        while((length = input.read(buffer)) > 0)
            output.write(buffer,0,length);

        input.close();
        output.close();

        return destinationFile;
    }


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
