package de.engine.colldetect;

import java.util.ArrayList;
import java.util.HashMap;

import de.engine.environment.EnvProps;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;
import de.engine.objects.Polygon;

public class CollisionTimer
{
    
    public static double getCollTime(ObjectProperties o1, ObjectProperties o2, double begin, double end)
    {
        if (o1 instanceof Circle && o2 instanceof Circle)
        {
            return getCirclesCollTime((Circle) o1, (Circle) o2);
        }
        else if (o1 instanceof Circle && o2 instanceof Polygon)
        {
            return getCirclePolygonCollTime((Circle) o1, (Polygon) o2, begin, end);
        }
        else if (o1 instanceof Polygon && o2 instanceof Circle)
        {
            return getCirclePolygonCollTime((Circle) o2, (Polygon) o1, begin, end);
            // } else if(o1 instanceof Polygon && o2 instanceof Polygon) {
        }
        return -1;
    }
    
    public static double getCirclesCollTime(Circle o1, Circle o2)
    {
        double distance = Util.minus(o1.getPosition(), o2.getPosition()).getLength();
        double min_distance = o1.getRadius() + o2.getRadius();
        double velocity = Util.minus(o1.velocity, o2.velocity).getLength();
        double coll_time = (distance - min_distance) / velocity;
        if ((min_distance + 2) < Util.minus(o1.getPosition(coll_time), o2.getPosition(coll_time)).getLength() || coll_time > EnvProps.deltaTime())
            return -1;
        else
            return coll_time;
    }
    
    public static double getCirclePolygonCollTime(Circle o1, Polygon o2, double begin, double end)
    {
        java.util.Vector<Double> times = new java.util.Vector<Double>();
        java.util.Vector<Integer> pre = new java.util.Vector<Integer>();
        java.util.Vector<Integer> past = new java.util.Vector<Integer>();
        HashMap<Integer, Boolean> coll = new HashMap<Integer, Boolean>();
        times.add(begin);
        times.add(end);
        pre.add(0);
        pre.add(0);
        past.add(1);
        past.add(1);
        double time_delta = Double.MAX_VALUE;
        int coll_id = -1;
        for (int i = 0; i < times.size(); i++)
        {
            coll.put(i, collideCirclePolygon(o1, o2, times.get(i)));
            
            if (!coll.get(i) && coll.get(pre.get(i)))
            {
                times.setSize(i + 1);
                pre.setSize(i + 1);
                past.setSize(i + 1);
                past.set(i, i);
                coll_id = i;
            }
            
            /*
             * if(coll.get(times.get(i)) && !coll.get(times.get(past.get(i)))) { pre.set(i, i); coll_id = past.get(i); }
             */
            
            if (pre.get(i) != i)
            {
                time_delta = (times.get(i) - times.get(pre.get(i))) / 2;
                Double time = times.get(pre.get(i)) + time_delta;
                if (!times.contains(time))
                {
                    times.add(time);
                    pre.add(pre.get(i));
                    pre.set(i, times.size() - 1);
                    past.add(i);
                }
            }
            if (past.get(i) != i)
            {
                time_delta = (times.get(past.get(i)) - times.get(i)) / 2;
                
                Double time = times.get(i) + time_delta;
                if (!times.contains(time))
                {
                    times.add(time);
                    past.add(past.get(i));
                    past.set(i, times.size() - 1);
                    pre.add(i);
                }
            }
            if(!coll.containsValue(true) && time_delta < 0.005) {
                return -1;
            }
            if (time_delta < 0.001)
            {
                if (coll_id > 0)
                {
                    return times.get(coll_id);
                }
                break;
            }
        }
        return -1;
    }
    
    private static boolean collideCirclePolygon(Circle o1, Polygon o2, double time)
    {
        for (int i = 0; i < o2.points.length; i++)
        {
            int j = (i == o2.points.length - 1) ? 0 : i + 1;
            Vector axis = Util.minus(o2.getWorldPointPos(j, time), o2.getWorldPointPos(i, time)).getNormalVector().getUnitVector();
            if (!collideCirclePolygonAxis(axis, o1, o2, time))
                return false;
        }
        for (int i = 0; i < o2.points.length; i++)
        {
            Vector axis = Util.minus(o1.getPosition(), o2.getWorldPointPos(i, time));
            if (!collideCirclePolygonAxis(axis, o1, o2, time))
                return false;
        }
        return true;
    }
    
    private static boolean collideCirclePolygonAxis(Vector axis, Circle o1, Polygon o2, double time)
    {
        MinMax mm_o2 = polygonInterval(axis, o2, time);
        MinMax mm_o1 = circleInterval(axis, o1, time);
        
        return (mm_o1.min <= mm_o2.max && mm_o2.min <= mm_o1.max);
    }
    
    private static MinMax polygonInterval(Vector axis, Polygon p, double time)
    {
        MinMax mm = new MinMax();
        for (int i = 0; i < p.points.length; i++)
        {
            double d = Util.scalarProduct(p.getWorldPointPos(i, time), axis);
            if (d < mm.min)
                mm.min = Double.valueOf(d);
            else if (d > mm.max)
                mm.max = Double.valueOf(d);
        }
        return mm;
    }
    
    private static MinMax circleInterval(Vector axis, Circle c, double time)
    {
        MinMax mm = new MinMax();
        double cn = Util.scalarProduct(axis, c.getPosition(time));
        mm.min = cn - c.getRadius();
        mm.max = cn + c.getRadius();
        return mm;
    }
    
    public static double getPolygonsCollTime(Polygon o1, Polygon o2, double radius_coll_time)
    {
        return -1;
    }
    
    private static class MinMax
    {
        public double min, max;
        
        public MinMax()
        {
            min = Double.MAX_VALUE;
            max = Double.MAX_VALUE;
        }
    }
}
