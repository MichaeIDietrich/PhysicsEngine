package de.engineapp.controls.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

import javax.swing.JComponent;

import de.engineapp.windows.InfoWindow;


public class DragAndDropController extends DropTarget
{
    // interface to recognize drops
    public interface DropCallback
    {
        public void drop(String command, Point location);
    }
    
    private static final long serialVersionUID = 5L;
    
    private DropCallback dropCallback;
    public boolean dontDrag = false;

    
    public DragAndDropController(JComponent source, DropCallback dropCallback)
    {
        this.setComponent(source);
        
        this.dropCallback = dropCallback;
    }
    
    
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
                    
                    InfoWindow.setData( InfoWindow.DROPPING, "akzepiert" );
                    
                    if (command.equals("circle") || command.equals("square") || command.equals("ground"))
                    {
                        dropCallback.drop(command, new Point(e.getLocation().x, e.getLocation().y));
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
}