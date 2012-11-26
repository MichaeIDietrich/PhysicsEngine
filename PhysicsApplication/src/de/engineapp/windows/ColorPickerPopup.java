package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import de.engineapp.controls.ColorBox;

public final class ColorPickerPopup
{
    private static final class ColorArea extends JComponent
    {
        private static final long serialVersionUID = 838387832738396737L;
        
        private static final int WHITE = 255;
        
        private Color colorAngle;
        private Point posCursor;
        private Color chosenColor;
        
        public ColorArea(Color chosenColor)
        {
            colorAngle = Color.RED;
            
            setChosenColor(chosenColor);
            
            this.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        ColorArea.this.setPosCursor(e.getPoint());
                    }
                }
            });
            
            this.addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        ColorArea.this.setPosCursor(e.getPoint());
                    }
                }
            });
        }
        
        
        public void addChangeListener(ChangeListener listener)
        {
            listenerList.add(ChangeListener.class, listener);
        }
        
//        public void removeChangeListener(ChangeListener listener)
//        {
//            listenerList.remove(ChangeListener.class, listener);
//        }
        
//        public ChangeListener[] getChangeListeners()
//        {
//            return listenerList.getListeners(ChangeListener.class);
//        }
        
        
        private Color getColorFromPoint(int x, int y)
        {
            x = Math.min(Math.max(x, 0), this.getWidth() - 1);
            y = Math.min(Math.max(y, 0), this.getHeight() - 1);
            
            float percX = (x + 1.0f) / this.getWidth();
            float percY = (y + 1.0f) / this.getHeight();
            
            float InvPercX = 1 - percX;
            float InvPercY = 1 - percY;
            
            int red   = (int) ((WHITE * InvPercX + colorAngle.getRed()   * percX) * InvPercY);
            int green = (int) ((WHITE * InvPercX + colorAngle.getGreen() * percX) * InvPercY);
            int blue  = (int) ((WHITE * InvPercX + colorAngle.getBlue()  * percX) * InvPercY);
            
            return new Color(red, green, blue);
        }
        
        
        @Override
        public void paint(Graphics g)
        {
            for (int x = 0; x < this.getWidth(); x++)
            {
                for (int y = 0; y < this.getHeight(); y++)
                {
                    g.setColor(getColorFromPoint(x, y));
                    
                    g.drawLine(x, y, x, y);
                }
            }
            
            Color invColor = new Color(255 - chosenColor.getRed(), 255 - chosenColor.getGreen(), 255 - chosenColor.getBlue());
            
            g.setColor(invColor);
            
            g.drawLine(posCursor.x,     posCursor.y - 4, posCursor.x,     posCursor.y - 1);
            g.drawLine(posCursor.x,     posCursor.y + 1, posCursor.x,     posCursor.y + 4);
            g.drawLine(posCursor.x - 4, posCursor.y,     posCursor.x - 1, posCursor.y);
            g.drawLine(posCursor.x + 1, posCursor.y,     posCursor.x + 4, posCursor.y);
        }
        
        
//        public Color getColorAngle()
//        {
//            return colorAngle;
//        }
        
        public void setColorAngle(Color colorAngle)
        {
            this.colorAngle = colorAngle;
            
            this.chosenColor = getColorFromPoint(posCursor.x, posCursor.y);
            
            for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
            {
                listener.stateChanged(new ChangeEvent(this));
            }
            
            this.repaint();
        }
        
        
//        public Point getPosCursor()
//        {
//            return posCursor;
//        }
        
        public void setPosCursor(Point cursor)
        {
            this.posCursor = new Point(Math.min(Math.max(cursor.x, 0), this.getWidth() - 1),
                                       Math.min(Math.max(cursor.y, 0), this.getHeight() - 1));
            
            this.chosenColor = getColorFromPoint(posCursor.x, posCursor.y);
            
            for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
            {
                listener.stateChanged(new ChangeEvent(this));
            }
            
            this.repaint();
        }
        
        
        public Color getChosenColor()
        {
            return chosenColor;
        }
        
        public void setChosenColor(Color chosenColor)
        {
            float[] parts = Color.RGBtoHSB(chosenColor.getRed(), chosenColor.getGreen(), chosenColor.getBlue(), null);
            setPosCursor(new Point((int) ((this.getWidth()  - 1) * parts[1]), 
                                   (int) ((this.getHeight() - 1) * (1 - parts[2]))));
        }
    }
    
    
    private static final class ColorSlider extends JComponent
    {
        private static final long serialVersionUID = -1933555793959946009L;
        
        private static final Color[] COLOR_ANGLES = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED };
        
        
        private Color chosenColor;
        private int posCursor;
        
        
        public ColorSlider(Color chosenColor)
        {
            this.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        ColorSlider.this.setPosCursor(e.getY());
                    }
                }
            });
            
            this.addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        ColorSlider.this.setPosCursor(e.getY());
                    }
                }
            });
            
            setChosenColor(chosenColor);
        }
        
        
        public void addChangeListener(ChangeListener listener)
        {
            listenerList.add(ChangeListener.class, listener);
        }
        
//        public void removeChangeListener(ChangeListener listener)
//        {
//            listenerList.remove(ChangeListener.class, listener);
//        }
        
//        public ChangeListener[] getChangeListeners()
//        {
//            return listenerList.getListeners(ChangeListener.class);
//        }
        
        
        private Color getColorFromPos(int y)
        {
            int index = (int) ((float) y / this.getHeight() * 6);
            
            int min = this.getHeight() * index / 6;
            int max = this.getHeight() * (index + 1) / 6;
            
            float perc = (float) (y - min) / (max - min);
            float InvPerc = 1 - perc;
            
            int red   = (int) (COLOR_ANGLES[index].getRed()   * InvPerc + COLOR_ANGLES[index + 1].getRed()   * perc);
            int green = (int) (COLOR_ANGLES[index].getGreen() * InvPerc + COLOR_ANGLES[index + 1].getGreen() * perc);
            int blue  = (int) (COLOR_ANGLES[index].getBlue()  * InvPerc + COLOR_ANGLES[index + 1].getBlue()  * perc);
            
            return new Color(red, green, blue);
        }
        
        
        @Override
        public void paint(Graphics g)
        {
            for (int y = 0; y < this.getHeight(); y++)
            {
                g.setColor(getColorFromPos(y));
                
                g.drawLine(0, y, this.getWidth() - 1, y);
            }
            
            
            Color invColor = new Color(255 - chosenColor.getRed(), 255 - chosenColor.getGreen(), 255 - chosenColor.getBlue());
            
            g.setColor(invColor);
            
            g.drawLine(0, posCursor, this.getWidth() - 1, posCursor);
        }
        
        
//        public int getPosCursor()
//        {
//            return posCursor;
//        }
        
        public void setPosCursor(int posCursor)
        {
            this.posCursor = Math.max(Math.min(posCursor, this.getHeight() - 1), 0);
            
            this.chosenColor = getColorFromPos(this.posCursor);
            
            for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
            {
                listener.stateChanged(new ChangeEvent(this));
            }
            
            this.repaint();
        }
        
        
        public Color getChosenColor()
        {
            return chosenColor;
        }
        
        public void setChosenColor(Color chosenColor)
        {
            float[] parts = Color.RGBtoHSB(chosenColor.getRed(), chosenColor.getGreen(), chosenColor.getBlue(), null);
            
            if (parts[0] == 0)
            {
                setPosCursor(0);
            }
            else
            {
                setPosCursor((int) ((1 - parts[0]) * (this.getHeight() - 1)));
            }
        }
    }
    
    
    private static final class ColorPickerFrame extends JWindow implements ChangeListener, AWTEventListener
    {
        private static final long serialVersionUID = 8293081073997096521L;
        
        
        private ColorBox colorBox;
        
        private JTabbedPane tabbedPane;
        private static int lastChosenTab = 0;
        
        // tab 1 - RGB
        
        private boolean doNotUpdate = false;
        
        private JSlider sliderRed;
        private JSlider sliderGreen;
        private JSlider sliderBlue;
        
        private ColorBox boxRed;
        private ColorBox boxGreen;
        private ColorBox boxBlue;
        
        // tab 2 - HSV
        
        private ColorArea colorArea;
        private ColorSlider colorSlider;
        
        
        public ColorPickerFrame(ColorBox colorBox)
        {
            this.colorBox = colorBox;
            
            // tab 1
            
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
            
            JPanel pnlRGB = new JPanel(new GridLayout(3, 1));
            
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
            
            pnlRGB.add(pnlRed);
            pnlRGB.add(pnlGreen);
            pnlRGB.add(pnlBlue);
            
            
            // tab 2
            
//            colorSlider = new ColorSlider(colorBox.getForeground());
            colorSlider = new ColorSlider(Color.RED);
            colorSlider.addChangeListener(this);
            colorArea   = new ColorArea(Color.RED);
//            colorArea   = new ColorArea(colorBox.getForeground());
            colorArea.addChangeListener(this);
            
            colorSlider.setPreferredSize(new Dimension(20, 1));
            
            JPanel pnlHSV = new JPanel(new BorderLayout());
            
            pnlHSV.add(colorSlider, BorderLayout.LINE_END);
            pnlHSV.add(colorArea);
            
            
            // tabbed pane
            
            tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM | JTabbedPane.LEFT);
            tabbedPane.setBackground(this.getBackground());
            tabbedPane.addTab("RGB", pnlRGB);
            tabbedPane.addTab("HSV", pnlHSV);
            tabbedPane.setSelectedIndex(lastChosenTab);
            
            tabbedPane.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    lastChosenTab = ((JTabbedPane) e.getSource()).getSelectedIndex();
                }
            });
            
            JPanel pnlSimpleBorder = new JPanel(new BorderLayout());
            pnlSimpleBorder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            pnlSimpleBorder.add(tabbedPane);
            
            this.add(pnlSimpleBorder);
            
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
            if (!doNotUpdate)
            {
                doNotUpdate = true;
                
                if ((e.getSource().equals(sliderRed)   || 
                        e.getSource().equals(sliderGreen) || 
                        e.getSource().equals(sliderBlue)))
                {
                    Color color = getRGBColor();
                    setRGBColor(color);
                    
                    colorBox.setForeground(color);
                    
                    colorSlider.setChosenColor(color);
                    colorArea.setColorAngle(colorSlider.getChosenColor());
                    colorArea.setChosenColor(color);
                }
                else if (e.getSource().equals(colorArea))
                {
                    Color color = colorArea.getChosenColor();
                    
                    setRGBColor(color);
                    colorBox.setForeground(color);
                }
                else if (e.getSource().equals(colorSlider))
                {
                    Color color = colorSlider.getChosenColor();
                    colorArea.setColorAngle(color);
                    
                    setRGBColor(colorArea.getChosenColor());
                    colorBox.setForeground(color);
                }
                
                doNotUpdate = false;
            }
        }
        
        
        private Color getRGBColor()
        {
            return new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
        }
        
        
        private void setRGBColor(Color color)
        {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            
            boxRed.setForeground(new Color(red, 0, 0));
            boxGreen.setForeground(new Color(0, green, 0));
            boxBlue.setForeground(new Color(0, 0, blue));
            
            if (sliderRed.getValue() != red)
            {
                sliderRed.setValue(red);
            }
            if (sliderGreen.getValue() != green)
            {
                sliderGreen.setValue(green);
            }
            if (sliderBlue.getValue() != blue)
            {
                sliderBlue.setValue(blue);
            }
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