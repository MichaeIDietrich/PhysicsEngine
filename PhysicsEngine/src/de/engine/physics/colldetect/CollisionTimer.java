package de.engine.physics.colldetect;

import java.util.HashMap;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;

public class CollisionTimer
{
    
    public static void calcCollTime(CollisionData collPair)
    {
        if (collPair.obj1 instanceof Circle && collPair.obj2 instanceof Circle)
        {
            calcCirclesCollTime(collPair);
        }
        else
        {
            calcCirclePolygonsCollTime(collPair);
        }
    }
    
    public static void calcCirclesCollTime(CollisionData collPair)
    {
        double distance = Util.minus(collPair.obj1.getPosition(collPair.min_time), collPair.obj2.getPosition(collPair.min_time)).getLength();
        double min_distance = collPair.obj1.getRadius() + collPair.obj2.getRadius();
        double velocity = Util.minus(collPair.obj1.velocity, collPair.obj2.velocity).getLength();
        double coll_time = ((distance - min_distance) / velocity) + collPair.min_time;
        //TODO: (distance - 0.01) not beautiful
        if (coll_time > collPair.max_time || coll_time < collPair.min_time || (distance - 0.01) < Util.minus(collPair.obj1.getPosition(coll_time), collPair.obj2.getPosition(coll_time)).getLength())
            return;
        else {
            collPair.coll_time = coll_time;
            collPair.calc_time = coll_time - (0.01 / velocity);
        }
    }
    
    public static void calcCirclePolygonsCollTime(CollisionData collPair)
    {
        double min_time_step = 1 / Util.minus(collPair.obj1.velocity, collPair.obj2.velocity).getLength();
        if(min_time_step > 0.001)
            min_time_step = 0.001;
        java.util.Vector<Double> times = new java.util.Vector<Double>();
        java.util.Vector<Integer> pre = new java.util.Vector<Integer>();
        java.util.Vector<Integer> past = new java.util.Vector<Integer>();
        HashMap<Integer, Boolean> coll = new HashMap<Integer, Boolean>();
        times.add(collPair.min_time);
        times.add(collPair.max_time);
        pre.add(0);
        pre.add(0);
        past.add(1);
        past.add(1);
        double time_delta = Double.MAX_VALUE;
        int coll_id = -1;
        for (int i = 0; i < times.size(); i++)
        {
            if (collPair.obj1 instanceof Circle && collPair.obj2 instanceof Polygon)
            {
                coll.put(i, collideCirclePolygon((Circle) collPair.obj1, (Polygon) collPair.obj2, times.get(i)));
            }
            else if (collPair.obj1 instanceof Polygon && collPair.obj2 instanceof Circle)
            {
                coll.put(i, collideCirclePolygon((Circle) collPair.obj2, (Polygon) collPair.obj1, times.get(i)));
            }
            else if (collPair.obj1 instanceof Polygon && collPair.obj2 instanceof Polygon)
            {
                coll.put(i, collidePolygons((Polygon) collPair.obj1, (Polygon) collPair.obj2, times.get(i)));
            }
            else
            {
                return;
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
                    collPair.coll_time = times.get(coll_id);
                    collPair.calc_time = times.get(pre.get(coll_id));
                }
                break;
            }
        }
        return;
    }
    
    private static boolean collideCirclePolygon(Circle o1, Polygon o2, double time)
    {
        Vector axis[] = Util.getAxis(o1, o2, time);
        for (Vector a : axis)
        {
            if (!Util.collideCirclePolygonAxis(a, o1, o2, time))
                return false;
        }
        return true;
    }
    
    private static boolean collidePolygons(Polygon o1, Polygon o2, double time)
    {
        Vector[] axis = Util.getAxis(o1, o2, time);
        for (Vector a : axis)
        {
            if (!Util.collidePolygonPolygonAxis(a, o1, o2, time))
                return false;
        }
        return true;
    }
}
