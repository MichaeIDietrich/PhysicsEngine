package de.engineapp.controls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
    // interface to recognize drops
    public interface DropCallback
    {
        public void drop(String command, Point location);
    }
    
    
    // interface to recognize repaints caused by component resizing
    public interface RepaintCallback
    {
        public void repaint();
    }
    
    
    private static final long serialVersionUID = -5320479580417617983L;
    
    private BufferedImage buffer = null;
    
    
    public Canvas(final DropCallback dropCallback, final RepaintCallback repaintCallback)
    {
        
        /** set up Drag and Drop */
        new DropTarget(this, new DropTargetAdapter()
        {
            @Override
            public void dragOver(DropTargetDragEvent e)
            {
                if (!e.getTransferable().getTransferDataFlavors()[0].isFlavorTextType())
                {
                    e.rejectDrag();
                }
            }
            
            
            @Override
            public void drop(DropTargetDropEvent e)
            {
                if (e.isLocalTransfer())
                {
                    Transferable tr = e.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();
                    
                    try
                    {
                        if (flavors != null && flavors.length == 1 && tr.getTransferData(flavors[0]) instanceof String)
                        {
                            String command = (String) tr.getTransferData(flavors[0]);
                            
                            System.out.println("Drop accepted.");
                            
                            if (command.equals("circle") || command.equals("rect") || command.equals("ground"))
                            {
                                dropCallback.drop(command, e.getLocation());
                                e.acceptDrop(e.getSourceActions());
                                
                                return;
                            }
                            else
                            {
                                // should never occur
                                System.err.println("Unknown Drop Command");
                            }
                        }
                    }
                    catch (UnsupportedFlavorException | IOException ex)
                    {
                        System.err.println("Something went wrong while dropping some Flavor");
                        ex.printStackTrace();
                    }
                    
                }
                System.err.println("Drop rejected. Foreign component.");
                e.rejectDrop();
            }
        });
        
        this.addComponentListener(new ComponentAdapter()
        {
            /** resize back buffer */
            @Override
            public void componentResized(ComponentEvent e)
            {
                buffer = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), BufferedImage.TYPE_INT_RGB);

                // fire repaint
                repaintCallback.repaint();
            }
        });
    }
    
    
    public void clearBuffer(Graphics2D g)
    {
        g.setBackground(this.getBackground());
        
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
    
    
    public Graphics2D getGraphics()
    {
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        
        // automatically clear buffer, may be removed later
        clearBuffer(g);
        
        return g;
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(buffer, 0, 0, null);
    }
}