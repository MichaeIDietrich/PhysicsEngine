package de.engineapp.windows;

import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import de.engine.math.Vector;


public class InfoWindow
{
    class InfoFrame extends JFrame
    {
        private static final long serialVersionUID = -7772367527425143529L;
        
        
        private JFrame attachedFrame = null;
        
        
        private HashMap<Integer, String>  hmap = null;
        private DefaultListModel<String> model = null;
        private JList<String>             list = null;
        
        private boolean isChanged = false;
        
        
        public InfoFrame()
        {
            super( "Informationen" );
            
            hmap  = new HashMap<>();
            model = new DefaultListModel<>();
            
            list = new JList<>();
            list.setModel( model );
            list.setFont( new Font("Times New Roman", Font.PLAIN, 13) );
            
            add( list );
            
            setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
            setSize( 250, 310 );
        }
        
        
        private void setData( Integer type, Object data )
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
                default: ;
            }
        }
        
        
        public void refresh()
        {
            if (isChanged)
            {
                model.clear();
                
//            for(int i=0; hmap.get(i)!=null; i++)
//            {
//                model.addElement( hmap.get(i) );
//            }
                for (int index : hmap.keySet())
                {
                    model.addElement(hmap.get(index));
                }
                
                isChanged = false;
            }
        }
        
        public void attachToFrame(final JFrame frame)
        {
            attachedFrame = frame;
            
            frame.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentMoved(ComponentEvent e)
                {
                    updateLocation();
                }
            });
            
            frame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowDeiconified(WindowEvent e)
                {
                    InfoFrame.this.setState(JFrame.NORMAL);
                }
                
                @Override
                public void windowIconified(WindowEvent e)
                {
                    InfoFrame.this.setState(JFrame.ICONIFIED);
                }
                
            });
        }
        
        public void updateLocation()
        {
            if (this.isVisible() && (attachedFrame != null))
            {
                InfoFrame.this.setLocation(attachedFrame.getX() + attachedFrame.getWidth(), attachedFrame.getY());
            }
        }
        
        public void showWindow( boolean bool )
        {
            this.setVisible(bool);
            
            updateLocation();
        }
    }
    
    
    public final static int COORDINATES    = 0;
    public final static int TIMEFORDRAWING = 1;
    public final static int ACTION         = 2;
    public final static int DROPPING       = 3;
    public final static int VELOCITY       = 4;
    public final static int POSITION       = 5;
    public final static int FPS            = 6;
    
    
    private static InfoFrame info = null;
    
    
    private InfoWindow() { }
    
    
    private static InfoFrame getInfo()
    {
        if (info == null)
        {
            info = (new InfoWindow()).new InfoFrame();
        }
        
        return info;
    }
    
    
    public static void showWindow( boolean bool )
    {
        getInfo().showWindow(bool);
    }
    
    
    public static void dispose()
    {
        getInfo().dispose();
    }
    
    
    public static void setData( Integer type, Object data )
    {
        getInfo().setData(type, data);
    }
    
    
    public static void refresh()
    {
        getInfo().refresh();
    }
    
    public static void attachToFrame(JFrame frame)
    {
        getInfo().attachToFrame(frame);
    }
}