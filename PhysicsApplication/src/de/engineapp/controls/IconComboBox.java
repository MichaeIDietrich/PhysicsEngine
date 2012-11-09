package de.engineapp.controls;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import de.engineapp.*;

public final class IconComboBox<T> extends JComboBox<T>
{
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    private final static Color SELECTION_FOREGROUND = UIManager.getColor("List.selectionForeground");
    private final static Color SELECTION_BACKGROUND = UIManager.getColor("List.selectionBackground");
    
    
    private class ItemRenderer implements ListCellRenderer<T>
    {
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
            
            if (index > -1 && (isSelected || cellHasFocus))
            {
                cell.setForeground(SELECTION_FOREGROUND);
                cell.setBackground(SELECTION_BACKGROUND);
                cell.setOpaque(true);
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
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    public IconComboBox(T[] data, List<String> entries, String iconFolder)
    {
        super(data);
        
        List<Icon> icons = new ArrayList<>();
        
        for (String entry : entries)
        {
            icons.add(Util.getIcon(iconFolder + "/" + entry));
        }
        
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    public IconComboBox(T[] data, String iconFolder)
    {
        super(data);
        
        List<String> entries = new ArrayList<>();
        List<Icon> icons = new ArrayList<>();
        
        for (T entry : data)
        {
            entries.add(LOCALIZER.getName(entry.toString()));
            icons.add(Util.getIcon(iconFolder + "/" + entry));
        }
        
        this.setRenderer(new ItemRenderer(entries, icons));
    }
    
    @Override
    public T getSelectedItem()
    {
        return this.getItemAt(this.getSelectedIndex());
    }
}