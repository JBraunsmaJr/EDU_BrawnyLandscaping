package io;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Locale;

public class ImageFileFilter extends FileFilter
{
    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";

    @Override
    public boolean accept(File file)
    {
        if(file.isDirectory())
            return true;

        String extension = getExtension(file);

        if(extension == null)
            return false;

        if(extension.equals(TIFF) ||
            extension.equals(TIF) ||
            extension.equals(PNG) ||
            extension.equals(GIF) ||
            extension.equals(JPG) ||
            extension.equals(JPEG)
        ) return true;

        return false;
    }

    @Override
    public String getDescription() { return "Image Only"; }

    String getExtension(File file)
    {
        String name = file.getName();
        int i = name.lastIndexOf(".");

        if(i > 0 && i < name.length() - 1)
            return name.substring(i + 1).toLowerCase(Locale.ROOT);

        return "";
    }
}
