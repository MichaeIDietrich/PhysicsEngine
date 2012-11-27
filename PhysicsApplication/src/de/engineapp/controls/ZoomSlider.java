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

import de.engineapp.Configuration;

import static de.engineapp.Constants.*;


/**
 * Custom slider, that handles thr application zoom of the scene well. ;)
 * 
 * @author Micha
 */
public final class ZoomSlider extends JPanel
{
    private static final long serialVersionUID = -8005150860316824860L;
    
    private static boolean isGTK = Configuration.getInstance().getProperty(PRP_LOOK_AND_FEEL).equals("GTK+");
    private static boolean isNimbus = Configuration.getInstance().getProperty(PRP_LOOK_AND_FEEL).equals("Nimbus");
    
    
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
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(240, 30));
        this.setMinimumSize(new Dimension(240, 30));
        this.setPreferredSize(new Dimension(240, 30));
        
        slider = new JSlider(-9, 18, 0)
        {
            private static final long serialVersionUID = 170776352557001522L;
            
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                
                // do not show ticks on GTK-LAF
                if (!isGTK)
                {
                    if (isNimbus)
                    {
                        g.setColor(Color.BLACK);
                        // 0.1
                        g.drawLine(8, 20, 8, 26);
                        // 1.0
                        g.drawLine(69, 20, 69, 26);
                        // 2.0
                        g.drawLine(137, 20, 137, 26);
                        // 10.0
                        g.drawLine(192, 20, 192, 26);
                    }
                    else
                    {
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
                }
            }
        };
        setValue(initValue);
        
        if (isGTK)
        {
            slider.setLocation(0, -8);
        }
        else
        {
            slider.setLocation(0, 0);
        }
        
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
        label.setSize(40, 30);
        label.setText(FORMATTER.format(getValue()) + "x");
        
        this.add(slider);
        this.add(label);
    }
    
    public double getValue()
    {
        if (slider.getValue() < 10)
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
        if (value < 2.0)
        {
            slider.setValue((int) (value * 10.0 - 10.0));
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
    
    
    @Override
    public void setToolTipText(String text)
    {
        super.setToolTipText(text);
        slider.setToolTipText(text);
        label.setToolTipText(text);
    }
}