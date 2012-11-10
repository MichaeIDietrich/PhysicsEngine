package de.engine.colldetect;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;

public class Helper
{
    public static class MinMax
    {
        public double min, max;
        
        public MinMax()
        {
            min = Double.MAX_VALUE;
            max = Double.MIN_VALUE;
        }
    }
    
    public static MinMax polygonInterval(Vector axis, Polygon p, double time)
    {
        MinMax mm = new MinMax();
        for (int i = 0; i < p.points.length; i++)
        {
            double d = Util.scalarProduct(axis, p.getWorldPointPos(i, time));
            if (d < mm.min)
                mm.min = d;
            else if (d > mm.max)
                mm.max = d;
        }
        return mm;
    }
    
    public static MinMax circleInterval(Vector axis, Circle c, double time)
    {
        MinMax mm = new MinMax();
        double cn = Util.scalarProduct(axis, c.getPosition(time));
        mm.min = cn - c.getRadius();
        mm.max = cn + c.getRadius();
        return mm;
    }
    
    public static boolean intersect(MinMax mm1, MinMax mm2)
    {
        return (mm1.min <= mm2.max && mm2.min <= mm1.max);
    }
    
    public static boolean collideCirclePolygonAxis(Vector axis, Circle o1, Polygon o2, double time)
    {
        MinMax mm_o1 = circleInterval(axis, o1, time);
        MinMax mm_o2 = polygonInterval(axis, o2, time);
        
        return intersect(mm_o1, mm_o2);
    }
    
    public static boolean collidePolygonPolygonAxis(Vector axis, Polygon o1, Polygon o2, double time)
    {
        MinMax mm_o1 = polygonInterval(axis, o1, time);
        MinMax mm_o2 = polygonInterval(axis, o2, time);

        return intersect(mm_o1, mm_o2);
    }
}
