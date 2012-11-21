package de.engineapp.windows;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engineapp.controls.ColorBox;

public final class ColorPickerPopup
{
    private final class ColorPickerFrame extends JWindow implements ChangeListener, AWTEventListener
    {
        private static final long serialVersionUID = 8293081073997096521L;
        
        
        private ColorBox colorBox;
        
        
        private JSlider sliderRed;
        private JSlider sliderGreen;
        private JSlider sliderBlue;
        
        private ColorBox boxRed;
        private ColorBox boxGreen;
        private ColorBox boxBlue;
        
        
        public ColorPickerFrame(ColorBox colorBox)
        {
            this.colorBox = colorBox;
            
            boxRed   = new ColorBox();
            boxGreen = new ColorBox();
            boxBlue  = new ColorBox();
            
            sliderRed   = new JSlider(0, 255);
            sliderGreen = new JSlider(0, 255);
            sliderBlue  = new JSlider(0, 255);
            
            sliderRed.setPaintTicks(true);
            sliderRed.setSnapToTicks(true);
            sliderGreen.setPaintTicks(true);
            sliderGreen.setSnapToTicks(true);
            sliderBlue.setPaintTicks(true);
            sliderBlue.setSnapToTicks(true);
            
            this.setLayout(new GridLayout(3, 1));
            
            JPanel pnlRed   = new JPanel();
            JPanel pnlGreen = new JPanel();
            JPanel pnlBlue  = new JPanel();
            
            pnlRed.add(sliderRed);
            pnlRed.add(boxRed);
            pnlGreen.add(sliderGreen);
            pnlGreen.add(boxGreen);
            pnlBlue.add(sliderBlue);
            pnlBlue.add(boxBlue);
            
            sliderRed.addChangeListener(this);
            sliderGreen.addChangeListener(this);
            sliderBlue.addChangeListener(this);
            
            this.add(pnlRed);
            this.add(pnlGreen);
            this.add(pnlBlue);
            
            
            this.pack();
            
            
            Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
            
            colorBox.addAncestorListener(new AncestorListener()
            {
                @Override
                public void ancestorRemoved(AncestorEvent event)
                {
                    ColorPickerFrame.this.dispose();
                }
                
                @Override
                public void ancestorMoved(AncestorEvent event) { }
                
                @Override
                public void ancestorAdded(AncestorEvent event) { }
            });
        }
        
        
        @Override
        public void dispose()
        {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            super.dispose();
        }
        
        
        @Override
        public void stateChanged(ChangeEvent e)
        {
            Color color = new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
            boxRed.setForeground(new Color(sliderRed.getValue(), 0, 0));
            boxGreen.setForeground(new Color(0, sliderGreen.getValue(), 0));
            boxBlue.setForeground(new Color(0, 0, sliderBlue.getValue()));
            
            colorBox.setForeground(color);
        }
        
        
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        }
        
        
        @Override
        public void setVisible(boolean b)
        {
            if (b)
            {
                Color color = colorBox.getForeground();
                sliderRed.setValue(color.getRed());
                sliderGreen.setValue(color.getGreen());
                sliderBlue.setValue(color.getBlue());
                
                Point point = colorBox.getLocationOnScreen();
                point.x -= this.getWidth();
                this.setLocation(point);
            }
            
            super.setVisible(b);
        }
        
        
        @Override
        public void eventDispatched(AWTEvent event)
        {
            if (event.getID() == MouseEvent.MOUSE_PRESSED)
            {
                if (!(event.getSource() == this || event.getSource() instanceof JComponent && 
                        ((JComponent) event.getSource()).getTopLevelAncestor().equals(this)))
                {
                    this.setVisible(false);
                }
            }
        }
    }
    
    
    private ColorPickerFrame frame;
    
    
    public ColorPickerPopup(ColorBox colorBox)
    {
        frame = new ColorPickerFrame(colorBox);
    }
    
    
    public boolean isVisible()
    {
        return frame.isVisible();
    }
    
    
    public void setVisible(boolean b)
    {
        frame.setVisible(b);
    }
    
    
    public void dispose()
    {
        frame.dispose();
    }
}