package de.engineapp;

import de.engineapp.windows.MainWindow;

import static de.engineapp.Constants.*;

public class Main
{
    
    public static void main(String[] args)
    {
        Configuration.load();
        Configuration config = Configuration.getInstance();
        
        Double doubleValue;
        
        int index = -1;
        
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
                    
                case "-nodebug":
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