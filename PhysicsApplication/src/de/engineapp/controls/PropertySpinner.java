package de.engineapp.controls;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class PropertySpinner extends JSpinner
{

    /**
     * 
     */
    private static final long serialVersionUID = -7629422754400062262L;
    
    
    public PropertySpinner(double start, double min, double max, double step, ChangeListener cl)
    {
        super(new SpinnerNumberModel(start,min,max,step));
        this.addChangeListener(cl);
    }
    
    
    //@Override
    public Double getValue()
    {
        return (Double) this.getModel().getValue();
    }

    @Override
    public void setValue(Object value)
    {
        this.getModel().setValue(value);
    }
    
}
