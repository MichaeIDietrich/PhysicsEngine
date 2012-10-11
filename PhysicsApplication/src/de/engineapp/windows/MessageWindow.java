package de.engineapp.windows;

import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;


public class MessageWindow extends JFrame
{
    private static MessageWindow instance;
    
    public final static int COORDINATES    = 0;
    public final static int TIMEFORDRAWING = 1;
    public final static int ACTION         = 2;
    public final static int DROPPING       = 3;
    
    private static final long serialVersionUID = 1L;
    private static HashMap<Integer, String>  hmap = null;
    private static DefaultListModel<String> model = null;
    private JList<String>             list = null;
   
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
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 250, 310 );
    }
    
    
    public static void setData( Integer typ, String data )
    {
        switch( typ )
        {
            case 0: hmap.put( typ, "x-y-Koordinaten: " + data);
                    isChanged = true;
                    break;
            case 1: hmap.put( typ, "zeichne Objekte: " + data + " ms");
                    isChanged = true;
                    break;
            case 2: hmap.put( typ, "letzte Aktion: "   + data );
                    isChanged = true;
                    break;
            case 3: hmap.put( typ, "Drop " + data );
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
            
            for(int i=0; hmap.get(i)!=null; i++)
            {
                model.addElement( hmap.get(i) );
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
