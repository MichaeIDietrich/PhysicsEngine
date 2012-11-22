package de.engineapp;

import java.util.*;

import de.engineapp.visual.IDrawable;

public final class RenderingSorter extends PriorityQueue<IDrawable>
{
    private static final long serialVersionUID = 4203785184464683783L;
    
    
    public RenderingSorter()
    {
        super(20, new Comparator<IDrawable>()
        {
            @Override
            public int compare(IDrawable o1, IDrawable o2)
            {
                if (o1.getDrawPriority() < o2.getDrawPriority())
                {
                    return -1;
                }
                if (o1.getDrawPriority() > o2.getDrawPriority())
                {
                    return 1;
                }
                
                return 0;
            }
        });
    }
}
