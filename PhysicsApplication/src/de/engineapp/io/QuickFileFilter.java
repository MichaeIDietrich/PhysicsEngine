package de.engineapp.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * Fast file filter for JFileChooser.
 * 
 * @author Micha
 */
public final class QuickFileFilter extends FileFilter
{
    private String descrition;
    private String fileExtension;
    
    
    public QuickFileFilter(String description, String fileExtension)
    {
        this.descrition = description;
        this.fileExtension = fileExtension;
    }
    
    
    @Override
    public boolean accept(File f)
    {
        return f.getName().endsWith(fileExtension) || f.isDirectory();
    }
    
    
    @Override
    public String getDescription()
    {
        return descrition;
    }
    
    
    public String getFileExtension()
    {
        return fileExtension;
    }
}