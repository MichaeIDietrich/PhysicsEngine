package de.engineapp.util;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Util class, that provides easy methods for GUI usage.
 * 
 * @author Micha
 */
public final class GuiUtil
{
    private static HashMap<String, ImageIcon> iconMap;
    private static String path;
    private static String root;
    
    static
    {
        iconMap = new HashMap<>();
        root = "images/";
        path = "default/";
    }
    
    /**
     * Hidden constructor
     */
    private GuiUtil() { }
    
    
    /**
     * Loads an icon from a resource name.
     * 
     * @param id - if of icon to be loaded
     * @return - loaded icon
     */
    public static ImageIcon getIcon(String id)
    {
        ImageIcon icon = iconMap.get(id);
        
        if (icon == null)
        {
            icon = loadImage(root + id + ".png");
            
            if (icon != null)
            {
                iconMap.put(id, icon);
            }
            else
            {
                icon = loadImage(root + path + id + ".png");
                
                if (icon != null)
                {
                    iconMap.put(id, icon);
                }
                else if (!id.equals("not_available"))
                {
                    icon = getIcon("not_available");
                }
            }
        }
        
        return icon;
    }
    
    
    /**
     * Loads an image from a resource name.
     * 
     * @param id - id of image to be loaded
     * @return - loaded image
     */
    public static Image getImage(String id)
    {
        ImageIcon icon = getIcon(id);
        
        if (icon != null)
        {
            return icon.getImage();
        }
        
        return null;
    }
    
    
    /**
     * Helper method, to load icons from a resource name.
     * 
     * @param path - path to icon to be loaded
     * @return - laoded icon
     */
    private static ImageIcon loadImage(String path)
    {
        try
        {
            InputStream stream = getResource(path);
            
            if (stream != null)
            {
                ImageIcon icon = new ImageIcon(ImageIO.read(stream));
                
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE)
                {
                    return icon;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    /**
     * Gets the path name that is related to current skin image path.
     * 
     * @return
     */
    public static String getImageSourcePath()
    {
        return path;
    }
    
    /**
     * Sets the path name for for the current skin related images.
     * 
     * @param path - skin path
     */
    public static void setImageSourcePath(String path)
    {
        GuiUtil.path = path.endsWith("/") ? path : path + "/";
    }
    
    
    /**
     * Gets the root path of the images.
     * 
     * @return - root path
     */
    public static String getImageRootPath()
    {
        return root;
    }
    
    /**
     * Sets the root path of the images.
     * 
     * @param path - root path
     */
    public static void setImageRootPath(String path)
    {
        GuiUtil.root = path.endsWith("/") ? path : path + "/";
    }
    
    
    /**
     * Creates a stream from a resource name.
     * 
     * @param name - name of the resource
     * @return - resource stream
     */
    public static InputStream getResource(String name)
    {
        if (name.charAt(0) != '/')
        {
            name = "/" + name;
        }
        
        return GuiUtil.class.getResourceAsStream(name);
    }
    
    
    /**
     * Checks, wether a resource exists.
     * 
     * @param name - resource to be checked
     * @return - true, if the resource exists
     */
    public static boolean resourceExists(String name)
    {
        if (name.charAt(0) != '/')
        {
            name = "/" + name;
        }
        
        return GuiUtil.class.getResource(name) != null;
    }
    
    
    /**
     * Retrieves an array of available resources within a directory.
     * 
     * @param path - direcoty to be analyzied
     * @return - array of resources
     */
    public static String[] getResources(String path)
    {
        if (path == null)
        {
            path = new String();
        }
        
        if (path.length() > 0 && path.charAt(0) == '/')
        {
            path = path.substring(1);
        }
        
        try
        {
            String classPath = System.getProperty("java.class.path");
            
            if (classPath.endsWith(".jar"))
            {
                ZipFile zipFile = new ZipFile(classPath);
                
                List<String> entries = new ArrayList<>();
                
                for (ZipEntry entry : Collections.list(zipFile.entries()))
                {
                    String name = entry.getName();
                    if (name.startsWith(path))
                    {
                        String res = name.substring(path.length() + 1);
                        int i = res.indexOf('/');
                        
                        if (i == -1)
                        {
                            if (res.length() > 0)
                            {
                                entries.add(res);
                            }
                        }
                        else
                        {
                            res = res.substring(0, i);
                            
                            if (!entries.contains(res))
                            {
                                entries.add(res);
                            }
                        }
                    }
                }
                
                zipFile.close();
                
                return entries.toArray(new String[entries.size()]);
            }
            else
            {
                return new File("resources/" + path).list();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    /**
     * Creates a HTML-Image-Tag with the specified image id.
     * 
     * @param id - id of the image to be loaded
     * @return - created HTML-Image-Tag
     */
    public static String getHtmlImage(String id)
    {
        URL url = GuiUtil.class.getResource("/" + root + path + id + ".png");
        
        if (url != null)
        {
            return "<image src=\"" + url.toString() + "\">";
        }
        
        return "<image src=\"\">";
    }
    
    
    /**
     * Checks, wether the left mouse button and the specified modifier keys are pressed.
     * 
     * @param e - mouse event to be checked
     * @param ctrlDown - wether ctrl is pressed or not, or null if not important
     * @param shiftDown - wether shift is pressed or not, or null if not important
     * @param altDown - wether alt is pressed or not, or null if not important
     * @return - true, if the mouse event equals the specified parameters
     */
    public static boolean isLeftButton(MouseEvent e, Boolean ctrlDown, Boolean shiftDown, Boolean altDown)
    {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0 && 
               (ctrlDown  == null || e.isControlDown() == ctrlDown)  &&
               (shiftDown == null || e.isShiftDown()   == shiftDown) && 
               (altDown   == null || e.isAltDown()     == altDown);
    }
    
    /**
     * Checks, wether the right mouse button and the specified modifier keys are pressed.
     * 
     * @param e - mouse event to be checked
     * @param ctrlDown - wether ctrl is pressed or not, or null if not important
     * @param shiftDown - wether shift is pressed or not, or null if not important
     * @param altDown - wether alt is pressed or not, or null if not important
     * @return - true, if the mouse event equals the specified parameters
     */
    public static boolean isRightButton(MouseEvent e, Boolean ctrlDown, Boolean shiftDown, Boolean altDown)
    {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0 && 
               (ctrlDown  == null || e.isControlDown() == ctrlDown)  &&
               (shiftDown == null || e.isShiftDown()   == shiftDown) && 
               (altDown   == null || e.isAltDown()     == altDown);
    }
    
    /**
     * Checks, wether the left and the right mouse button and the specified modifier keys are pressed.
     * 
     * @param e - mouse event to be checked
     * @param ctrlDown - wether ctrl is pressed or not, or null if not important
     * @param shiftDown - wether shift is pressed or not, or null if not important
     * @param altDown - wether alt is pressed or not, or null if not important
     * @return - true, if the mouse event equals the specified parameters
     */
    public static boolean isLeftRightButton(MouseEvent e, Boolean ctrlDown, Boolean shiftDown, Boolean altDown)
    {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0 && 
               (ctrlDown  == null || e.isControlDown() == ctrlDown)  &&
               (shiftDown == null || e.isShiftDown()   == shiftDown) && 
               (altDown   == null || e.isAltDown()     == altDown);
    }
    
    /**
     * Checks, wether the middle mouse button and the specified modifier keys are pressed.
     * 
     * @param e - mouse event to be checked
     * @param ctrlDown - wether ctrl is pressed or not, or null if not important
     * @param shiftDown - wether shift is pressed or not, or null if not important
     * @param altDown - wether alt is pressed or not, or null if not important
     * @return - true, if the mouse event equals the specified parameters
     */
    public static boolean isMiddleButton(MouseEvent e, Boolean ctrlDown, Boolean shiftDown, Boolean altDown)
    {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0 && 
               (ctrlDown  == null || e.isControlDown() == ctrlDown)  &&
               (shiftDown == null || e.isShiftDown()   == shiftDown) && 
               (altDown   == null || e.isAltDown()     == altDown);
    }
    
    
    /**
     * Checks, wether no mouse button is currently pressed.
     * 
     * @param e - mouse event to be checked
     * @return - true, if no mouse button is pressed
     */
    public static boolean isNoButtonDown(MouseEvent e)
    {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == 0 && 
               (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0;
    }
}