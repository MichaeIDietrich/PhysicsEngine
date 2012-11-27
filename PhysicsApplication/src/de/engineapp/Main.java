package de.engineapp;

import de.engineapp.windows.MainWindow;

import static de.engineapp.Constants.*;

/**
 * Entry point for the PhysicsApplication.
 * 
 * @author Micha
 */
public final class Main
{
    // no instance desired
    private Main() { }
    
    public static void main(String[] args)
    {
        // checks wether there is an config file within the application directory
        Configuration.load();
        Configuration config = Configuration.getInstance();
        
        Double doubleValue;
        
        int index = -1;
        
        // check the assigned parameters
        while (++index < args.length)
        {
            switch (args[index].toLowerCase())
            {
                case "-grid":
                    config.setState(STG_GRID, true);
                    break;
                    
                case "-zoom":
                    if (++index < args.length)
                    {
                        doubleValue = extractDouble(args[index]);
                        
                        if (doubleValue != null && doubleValue >= 0.1 && doubleValue <= 10.0)
                        {
                            config.setZoom(doubleValue);
                        }
                        else
                        {
                            System.out.println("The Parameter next to '-zoom' has a wrong format");
                        }
                    }
                    else
                    {
                        System.out.println("The Parameter next to '-zoom' is missing");
                    }
                    break;
                    
                case "-lang":
                    if (++index < args.length)
                    {
                        config.setProperty(PRP_LANGUAGE_CODE, args[index]);
                    }
                    else
                    {
                        System.out.println("The Parameter next to '-lang' is missing");
                    }
                    break;
                    
                case "-debug":
                    config.setState(STG_DEBUG, true);
                    break;
                    
                case "-nodebug": // necessary, if the debug flag is saved in the config file
                    config.setState(STG_DEBUG, false);
                    break;
                    
                case "-skin":
                    if (++index < args.length)
                    {
                        config.setProperty(PRP_SKIN, args[index]);
                    }
                    else
                    {
                        System.out.println("The Parameter next to '-skin' is missing");
                    }
                    break;
                    
                default:
                    if (index == args.length - 1)
                    {
                        config.setProperty(PRP_CURRENT_FILE, args[index]);
                    }
            }
        }
        
        // creates a new window that combines all the GUI stuff
        new MainWindow();
    }
    
    
    private static Double extractDouble(String value)
    {
        try
        {
            return new Double(value);
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }
}