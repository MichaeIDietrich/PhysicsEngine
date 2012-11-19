package de.engine.colldetect;

import java.util.HashMap;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;
import de.engine.objects.Polygon;

public class CollisionTimer
{
    
    public static Double getCollTime(ObjectProperties o1, ObjectProperties o2, double begin, double end)
    {
        if (o1 instanceof Circle && o2 instanceof Circle)
        {
            return getCirclesCollTime((Circle) o1, (Circle) o2, begin, end);
        }
        else
        {
            return getCirclePolygonsCollTime(o2, o1, begin, end);
        }
    }
    
    public static Double getCirclesCollTime(Circle o1, Circle o2, double begin, double end)
    {
        //double start_time = (o1.frametime > o2.frametime) ? o1.frametime : o2.frametime;
        double distance = Util.minus(o1.getPosition(begin), o2.getPosition(begin)).getLength();
        double min_distance = o1.getRadius() + o2.getRadius();
        double velocity = Util.minus(o1.velocity, o2.velocity).getLength();
        double coll_time = ((distance - min_distance) / velocity) + begin;
        //double after_distance = Util.minus(o1.getPosition(coll_time), o2.getPosition(coll_time)).getLength();
        //TODO: (distance - 0.01) not beautiful
        if (coll_time > end || coll_time < begin || (distance - 0.01) < Util.minus(o1.getPosition(coll_time), o2.getPosition(coll_time)).getLength())
            return null;
        else
            return coll_time;
    }
    
    public static Double getCirclePolygonsCollTime(ObjectProperties o1, ObjectProperties o2, double begin, double end)
    {
        double min_time_step = 1 / Util.minus(o1.velocity, o2.velocity).getLength();
        if(min_time_step > 0.001)
            min_time_step = 0.001;
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
            if (o1 instanceof Circle && o2 instanceof Polygon)
            {
                coll.put(i, collideCirclePolygon((Circle) o1, (Polygon) o2, times.get(i)));
            }
            else if (o1 instanceof Polygon && o2 instanceof Circle)
            {
                coll.put(i, collideCirclePolygon((Circle) o2, (Polygon) o1, times.get(i)));
            }
            else if (o1 instanceof Polygon && o2 instanceof Polygon)
            {
                coll.put(i, collidePolygons((Polygon) o1, (Polygon) o2, times.get(i)));
            }
            else
            {
                return null;
            }
            
            if (coll.get(i) && !coll.get(pre.get(i)))
            {
                times.setSize(i + 1);
                pre.setSize(i + 1);
                past.setSize(i + 1);
                past.set(i, i);
                coll_id = i;
            }
            
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
            /*if (!coll.containsValue(true) && time_delta < (min_time_step * 5))
            {
                return null;
            }*/
            if (time_delta < min_time_step)
            {
                if (coll_id > 0)
                {
                    return times.get(coll_id);
                }
                break;
            }
        }
        return null;
    }
    
    private static boolean collideCirclePolygon(Circle o1, Polygon o2, double time)
    {
        Vector axis[] = Util.getAxis(o1, o2, time);
        for (Vector a : axis)
        {
            if (!Helper.collideCirclePolygonAxis(a, o1, o2, time))
                return false;
        }
        return true;
    }
    
    private static boolean collidePolygons(Polygon o1, Polygon o2, double time)
    {
        Vector[] axis = Util.getAxis(o1, o2, time);
        for (Vector a : axis)
        {
            if (!Helper.collidePolygonPolygonAxis(a, o1, o2, time))
                return false;
        }
        return true;
    }
}
