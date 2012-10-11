package de.engineapp.controls.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

import javax.swing.JComponent;

import de.engine.environment.Scene;


public class DragAndDropController extends DropTarget
{
    
    // interface to recognize drops
    public interface DropCallback
    {
        public void drop(String command, Point location);
    }
    
    private static final long serialVersionUID = 5L;
    
    private Scene scene;
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
        // TODO: Compare the Y-coordinate of the ball to the Y-coordinate of the drawn function
        if (!e.getTransferable().getTransferDataFlavors()[0].isFlavorTextType())// ||
           //( (scene.getPhysicsEngine2D()!=null)?(!scene.getPhysicsEngine2D().semaphore):false))
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
    
    
    public Scene getScene()
    {
        return scene;
    }
    
    
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }
}
