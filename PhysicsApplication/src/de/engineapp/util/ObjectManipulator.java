package de.engineapp.util;

import static de.engineapp.Constants.DECOR_ANGLE_VIEWER;
import static de.engineapp.Constants.DECOR_RANGE;
import de.engine.math.*;
import de.engine.objects.*;
import de.engineapp.PresentationModel;
import de.engineapp.visual.IDecorable;
import de.engineapp.visual.decor.*;


/**
 * Class that provides methods to manipulate scene objects.
 * 
 * @author Micha
 */
public final class ObjectManipulator
{
    private PresentationModel pModel;
    
    
    public ObjectManipulator(PresentationModel model)
    {
        pModel = model;
    }
    
    
    public void translateObjects(Vector cursor)
    {
        if (pModel.hasSelectedObject())
        {
            Vector diff = Util.minus(cursor, pModel.getSelectedObject().getPosition());
            
            for (ObjectProperties object : pModel.getMultipleSelectionObjects())
            {
                object.getPosition().add(diff);
            }
        }
    }
    
    
    public void initRotateObjects()
    {
        if (pModel.hasSelectedObject())
        {
            AngleViewer angleViewer = new AngleViewer(pModel.getSelectedObject());
            ((IDecorable) pModel.getSelectedObject()).putDecor(DECOR_ANGLE_VIEWER, angleViewer);
        }
    }
    
    
    public void rotateObjects(Vector cursor)
    {
        if (pModel.hasSelectedObject())
        {
            double x = cursor.getX() - pModel.getSelectedObject().getX();
            double y = cursor.getY() - pModel.getSelectedObject().getY();
            
            double newAngle = Math.atan2(y, x);
            
            for (ObjectProperties object : pModel.getMultipleSelectionObjects())
            {
                object.setRotationAngle(newAngle);
            }
        }
    }
    
    
    public void initScaleObjects()
    {
        if (pModel.hasSelectedObject())
        {
            Range range = new Range(pModel.getSelectedObject(), "radius", true);
            ((IDecorable) pModel.getSelectedObject()).putDecor(DECOR_RANGE, range);
        }
    }
    
    
    public void scaleObjects(Vector cursor)
    {
        if (pModel.hasSelectedObject())
        {
            double distance = Util.distance(pModel.getSelectedObject().getPosition(), cursor);
            
            for (ObjectProperties object : pModel.getMultipleSelectionObjects())
            {
                object.setRadius(distance);
            }
        }
    }
    
    
    public void scaleRect(Vector cursor)
    {
        if (pModel.hasSelectedObject() && !pModel.hasMultiSelectionObjects() && 
                pModel.getSelectedObject() instanceof Square)
        {
            Square rect = (Square) pModel.getSelectedObject();
            
            Vector localCursor = Util.minus(cursor, rect.getPosition());
            
            Matrix inverseRotationMatrix = new Transformation.Rotation(-rect.getRotationAngle()).getMatrix();
            Vector distanceVector = inverseRotationMatrix.multVector(localCursor);
            
            rect.setSize(Math.abs(distanceVector.getX()) * 2, Math.abs(distanceVector.getY()) * 2);
        }
    }
    
    
    public void speedObjects(Vector cursor)
    {
        if (pModel.hasSelectedObject())
        {
            for (ObjectProperties object : pModel.getMultipleSelectionObjects())
            {
//                object.velocity = Util.minus(cursor, object.getPosition());
                object.velocity = Util.minus(cursor, pModel.getSelectedObject().getPosition());
            }
        }
    }
}