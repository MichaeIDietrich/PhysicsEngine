package de.engineapp.containers;

import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import de.engineapp.*;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.controls.DragButton;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;


/**
 * Panel to handle the supported scene objects.
 * 
 * @author Micha
 */
public final class ObjectToolBar extends JToolBar implements MouseListener, StorageListener
{
    private static final long serialVersionUID = -5441109476332523959L;
    
    private Localizer LOCALIZER = Localizer.getInstance();
    
    
    PresentationModel pModel;
    
    private DragButton circle;
    private DragButton square;
    private DragButton ground;
    
    public ObjectToolBar(PresentationModel model)
    {
        super(JToolBar.VERTICAL);
        
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        circle = new DragButton(GuiUtil.getIcon(ICO_CIRCLE), OBJ_CIRCLE, true);
        square = new DragButton(GuiUtil.getIcon(ICO_SQUARE), OBJ_SQUARE, true);
        ground = new DragButton(GuiUtil.getIcon(ICO_GROUND), OBJ_GROUND, GuiUtil.getImage(ICO_RULER), new Point(16, 14));
        
        circle.addMouseListener(this);
        square.addMouseListener(this);
        ground.addMouseListener(this);
        
        setToolTips();
        
        this.add(circle);
        this.add(square);
        this.add(ground);
        
        pModel.addStorageListener(this);
    }
    
    
    private void setToolTips()
    {
        circle.setToolTipText(LOCALIZER.getString(TT_CIRCLE));
        square.setToolTipText(LOCALIZER.getString(TT_SQUARE));
        ground.setToolTipText(LOCALIZER.getString(TT_GROUND));
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (e.getComponent().equals(circle))
            {
                square.setSelected(false);
                ground.setSelected(false);
                if (circle.isSelected())
                {
                    pModel.setProperty(PRP_OBJECT_MODE, OBJ_CIRCLE);
                }
                else
                {
                    pModel.setProperty(PRP_OBJECT_MODE, null);
                }
            }
            else if (e.getComponent().equals(square))
            {
                circle.setSelected(false);
                ground.setSelected(false);
                if (square.isSelected())
                {
                    pModel.setProperty(PRP_OBJECT_MODE, OBJ_SQUARE);
                }
                else
                {
                    pModel.setProperty(PRP_OBJECT_MODE, null);
                }
            }
            else if (e.getComponent().equals(ground))
            {
                circle.setSelected(false);
                square.setSelected(false);
                if (ground.isSelected())
                {
                    pModel.setProperty(PRP_OBJECT_MODE, OBJ_GROUND);
                }
                else
                {
                    pModel.setProperty(PRP_OBJECT_MODE, null);
                }
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) { }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    
    @Override
    public void stateChanged(String id, boolean value) { }
    
    @Override
    public void propertyChanged(String id, String value)
    {
        if (id.equals(PRP_LANGUAGE_CODE))
        {
            setToolTips();
        }
    }
}