package de.engineapp.controls;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import de.engineapp.*;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;

public final class IconComboBox<T> extends JComboBox<T>
{
    private final static Localizer LOCALIZER = Localizer.getInstance();
    private static boolean isGTK = Configuration.getInstance().getProperty(PRP_LOOK_AND_FEEL).equals("GTK+");
    
//    private final static Color FOREGROUND = UIManager.getColor("List.foreground");
//    private final static Color BACKGROUND = UIManager.getColor("List.background");
    private final static Color SELECTION_FOREGROUND = UIManager.getColor("List.selectionForeground");
    private final static Color SELECTION_BACKGROUND = UIManager.getColor("List.selectionBackground");
    
    
    private class ItemRenderer implements ListCellRenderer<T>
    {
        private Border MARGIN = BorderFactory.createEmptyBorder(0, 2, 0, 0);
        private Border GTK_BORDER = BorderFactory.createLineBorder(new Color(170, 170, 170));
        
        
        private List<Icon> icons;
        private List<String> entries;
        
        
        public ItemRenderer(List<String> entries, List<Icon> icons)
        {
            this.entries = entries;
            this.icons = icons;
        }
        
        
        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus)
        {
            int i = getIndexFromData(list.getModel(), value);
            
            JLabel cell = new JLabel(entries.get(i), icons.get(i), SwingConstants.LEFT);
            
            if (index > -1) cell.setBorder(MARGIN);
            
            if (index > -1 && (isSelected || cellHasFocus))
            {
                cell.setOpaque(true);
                cell.setForeground(SELECTION_FOREGROUND);
                cell.setBackground(SELECTION_BACKGROUND);
            }
//            else
//            {
//                cell.setForeground(FOREGROUND);
//                cell.setBackground(BACKGROUND);
//            }
            
            if (isGTK && index == -1)
            {
                cell.setBorder(GTK_BORDER);
            }
            
            return cell;
        }
        
        private int getIndexFromData(ListModel<? extends T> model, T data)
        {
            for (int i = 0; i < model.getSize(); i++)
            {
                if (model.getElementAt(i) == data)
                {
                    return i;
                }
            }
            
            return -1;
        }
    }
    
    
    private static final long serialVersionUID = 1228085055379713385L;
    
    
    public IconComboBox(T[] data, List<String> entries, List<Icon> icons)
    {
        super(data);
        this.setFocusable(false);
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    
    public IconComboBox(T[] data, List<String> entries, String iconFolder)
    {
        super(data);
        this.setFocusable(false);
        
        List<Icon> icons = new ArrayList<>();
        
        for (String entry : entries)
        {
            icons.add(GuiUtil.getIcon(iconFolder + "/" + entry));
        }
        
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    
    public IconComboBox(T[] data, String iconFolder)
    {
        super(data);
        this.setFocusable(false);
        
        List<String> entries = new ArrayList<>();
        List<Icon> icons = new ArrayList<>();
        
        for (T entry : data)
        {
            entries.add(LOCALIZER.getString(entry.toString()));
            icons.add(GuiUtil.getIcon(iconFolder + "/" + entry));
        }
        
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    
    @Override
    public T getSelectedItem()
    {
        return this.getItemAt(this.getSelectedIndex());
    }
}