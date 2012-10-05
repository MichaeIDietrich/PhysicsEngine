package de.engineapp.controls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;

public class Canvas extends JComponent
{
    
    public interface DropCallback
    {
        public void drop(String command);
    }
    
    
    private static final long serialVersionUID = -5320479580417617983L;
    
    private BufferedImage buffer = null;
    
    public Canvas(final DropCallback callback)
    {
        
        /** create back buffer */
//        buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        /** set up Drag and Drop */
        new DropTarget(this, new DropTargetAdapter()
        {
            @Override
            public void dragOver(DropTargetDragEvent e)
            {
                e.acceptDrag(e.getSourceActions());
            }
            
            @Override
            public void drop(DropTargetDropEvent e)
            {
                Transferable tr = e.getTransferable();
                DataFlavor[] flavors = tr.getTransferDataFlavors();
                
                try
                {
                    if (flavors != null && flavors.length == 1 && tr.getTransferData(flavors[0]) instanceof String)
                    {
                        String command = (String) tr.getTransferData(flavors[0]);
                        
                        if (command.equals("circle") || command.equals("rect"))
                        {
                            callback.drop(command);
                        }
                    }
                }
                catch (UnsupportedFlavorException | IOException ex)
                {
                    ex.printStackTrace();
                }
                
                
                e.acceptDrop(e.getSourceActions());
            }
        });
        
        
        this.addComponentListener(new ComponentAdapter()
        {
            /** resize back buffer */
            @Override
            public void componentResized(ComponentEvent e)
            {
                buffer = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
        });
    }
    
    
    public Graphics2D getGraphics()
    {
        return (Graphics2D) buffer.getGraphics();
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        g.drawImage(buffer, 0, 0, null);
    }
}