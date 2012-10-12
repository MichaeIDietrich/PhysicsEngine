package de.engineapp.controls;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZoomSlider extends JPanel
{
    private static final long serialVersionUID = -8005150860316824860L;
    
    
    private JSlider slider = null;
    private JLabel label = null;
    
    private final static DecimalFormat FORMATTER = new DecimalFormat("0.0");
    
    
    public ZoomSlider()
    {
        
        slider = new JSlider(1, 50, 10);
        slider.setFocusable(false);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(10);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                label.setText(FORMATTER.format(getValue()));
            }
        });
        
        label = new JLabel();
        
        this.add(slider);
        this.add(label);
    }
    
    public double getValue()
    {
        return slider.getValue() / 10.0;
    }
    
    public void setValue(double value)
    {
        slider.setValue((int) Math.round(value * 10.0));
    }
    
    public void addChangeListener(ChangeListener listener)
    {
        slider.addChangeListener(listener);
    }
}
