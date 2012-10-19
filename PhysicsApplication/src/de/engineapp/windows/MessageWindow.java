package de.engineapp.windows;

import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import de.engine.math.Vector;


public class MessageWindow extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    
    private static MessageWindow instance;
    
    public final static int COORDINATES    = 0;
    public final static int TIMEFORDRAWING = 1;
    public final static int ACTION         = 2;
    public final static int DROPPING       = 3;
    public final static int VELOCITY       = 4;
    public final static int POSITION       = 5;
    public final static int FPS            = 6;
    public final static int NEWVELODIR     = 7;
    
    private static HashMap<Integer, String>  hmap = null;
    private static DefaultListModel<String> model = null;
    private static JList<String>             list = null;
   
    private static boolean isChanged = false;
    
    
    public MessageWindow( Point mainframepos )
    {
        super( "Informationen" );
        setLocation( mainframepos.x, mainframepos.y );
        MessageWindow.instance = this;

        hmap  = new HashMap<>();
        model = new DefaultListModel<>();
        
        list = new JList<>();
        list.setModel( model );
        list.setFont( new Font("Times New Roman", Font.PLAIN, 13) );
        
        add( list );
        
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setSize( 270, 330 );
    }
    
    

    public static void setData( Integer type, Object data )
    {   
        switch( type )
        {
            case COORDINATES:
                hmap.put( type, "x-y-Koordinaten: " + (int)((Vector) data).getX() +", "+ (int)((Vector) data).getY());
                isChanged = true;
                break;
                
            case TIMEFORDRAWING:
                hmap.put( type, "zeichne Objekte: " + data + " ms");
                isChanged = true;
                break;
                
            case ACTION:
                hmap.put( type, "letzte Aktion: "   + data );
                isChanged = true;
                break;
                
            case DROPPING:
                hmap.put( type, "Drop " + data );
                isChanged = true;
                break;
                
            case VELOCITY:
                hmap.put( type, "Geschwindigkeit: " + data );
                isChanged = true;
                break;
                
            case POSITION:
                hmap.put( type, "Position: " + data );
                isChanged = true;
                break;
                
            case FPS:
                hmap.put( type, "FPS: " + data );
                isChanged = true;
                break;
            case NEWVELODIR:
                hmap.put( type, "setze Geschwindigk.: vx="+ (int)((Vector)data).getX() +" m/s,  vy="+ (int)((Vector)data).getY() +" m/s");
                isChanged = true;
                break;
            default: ;
        }
    }

    
    
    public static void refresh()
    {
        if (isChanged)
        {
            model.clear();
            
            for (int index : hmap.keySet())
            {
                try 
                {
                    model.addElement(hmap.get(index));
                } 
                    catch(Exception e) 
                {
                    System.out.println("Text");
                }
            }

            isChanged = false;
        }
    }
    
    
    // Tells the information window where the main window is located and set its location nearby
    public void updateLocation( int location_x, int location_y )
    {
        setLocation( location_x, location_y );
    }
    
    
    public static MessageWindow getInstance()
    {
        return MessageWindow.instance;
    }
    
    
    public void showWindow( boolean bool )
    {
        this.setVisible(bool);
    }
}
