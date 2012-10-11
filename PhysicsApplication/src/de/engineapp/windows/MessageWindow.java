package de.engineapp.windows;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
    
    private static final long serialVersionUID = 1L;
    private static HashMap<Integer, String>  hmap = null;
    private static DefaultListModel<String> model = null;
    private JList<String>             list = null;
   
    
    public MessageWindow( Point mainframepos )
    {
        super( "Informationen" );
        setLocation( mainframepos.x, mainframepos.y );
        MessageWindow.instance = this;
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().setLayout( new BorderLayout() );
        
        hmap  = new HashMap<>();
        model = new DefaultListModel<>();
        
        list = new JList<>();
        list.setModel( model );
        
        gbl.setConstraints( list, gbc);
        
        add( list );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 250, 310 );
        setVisible( true );
    }
    
    
    public static void setData( Integer typ, String data )
    {
        switch( typ )
        {
            case 0: hmap.put( typ, "x-y-Koordinaten: " + data);
                    break;
            case 1: hmap.put( typ, "zeichne Objekte: " + data + " ms");
                    break;
            case 2: hmap.put( typ, "Aktion: " + data );
                    break;
            default: ;
        }
    }
    
    
    public static void refresh()
    {
        model.clear();
        
        for(int i=0; hmap.get(i)!=null; i++)
        {
            model.addElement( hmap.get(i) );
        }
    }
    
    
    // Tells the information window where the main window is located and set its location nearby
    public void updateLocation( int location_x, int location_y )
    {
        setLocation( location_x, location_y );
        System.out.println( location_x );
    }
    
    
    public static MessageWindow getInstance()
    {
        if(MessageWindow.instance == null){
            MessageWindow.instance = new MessageWindow( new Point(0,0) );
        }
 
        return MessageWindow.instance;
    }
}
