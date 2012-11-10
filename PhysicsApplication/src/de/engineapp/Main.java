package de.engineapp;

import de.engineapp.windows.MainWindow;

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
                    config.setShowGrid(true);
                    break;
                    
                case "-zoom":
                    if (++index < args.length)
                    {
                        doubleValue = extractDouble(args[index]);
                        
                        if (doubleValue != null && doubleValue >= 0.1 && doubleValue <= 5.0)
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
                        config.setLangCode(args[index]);
                    }
                    else
                    {
                        System.out.println("The Parameter next to '-lang' is missing");
                    }
                    break;
                    
                case "-debug":
                    config.setDebug(true);
                    break;
                    
                case "-nodebug":
                    config.setDebug(false);
                    break;
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
