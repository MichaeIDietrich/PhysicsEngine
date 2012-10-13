package de.engineapp.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
        this(1.0);
    }
    
    
    public ZoomSlider(double initValue)
    {
        this.setLayout(null);
        this.setMaximumSize(new Dimension(235, 30));
        this.setMinimumSize(new Dimension(235, 30));
        
        slider = new JSlider(-9, 18, 0)
        {
            private static final long serialVersionUID = 170776352557001522L;
            
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                
                g.setColor(Color.BLACK);
                // 0.1
                g.drawLine(7, 20, 7, 24);
                // 1.0
                g.drawLine(69, 20, 69, 24);
                // 2.0
                g.drawLine(138, 20, 138, 24);
                // 10.0
                g.drawLine(192, 20, 192, 24);
            }
        };
        setValue(initValue);
        
        slider.setLocation(0, 0);
        slider.setSize(200, 30);
        slider.setFocusable(false);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMinorTickSpacing(1);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                label.setText(FORMATTER.format(getValue()) + "x");
            }
        });
        
        label = new JLabel();
        label.setLocation(200, 0);
        label.setSize(35, 30);
        label.setText(FORMATTER.format(getValue()) + "x");
        
        this.add(slider);
        this.add(label);
    }
    
    public double getValue()
    {
        if (slider.getValue() < 0)
        {
            return slider.getValue() / 10.0 + 1.0;
        }
        else if (slider.getValue() == 0)
        {
            return 1.0;
        }
        else if (slider.getValue() < 10)
        {
            return slider.getValue() / 10.0 + 1.0;
        }
        else
        {
            return slider.getValue() - 8.0;
        }
    }
    
    public void setValue(double value)
    {
        if (value < 1.0)
        {
            slider.setValue((int) (value * 10.0 - 10.0));
        }
        else if (value == 1.0)
        {
            slider.setValue(0);
        }
        else if (value < 2.0)
        {
            slider.setValue((int) (value * 10.0 - 1.0));
        }
        else
        {
            slider.setValue((int) (value + 8.0));
        }
    }
    
    public void addChangeListener(ChangeListener listener)
    {
        slider.addChangeListener(listener);
    }
}
