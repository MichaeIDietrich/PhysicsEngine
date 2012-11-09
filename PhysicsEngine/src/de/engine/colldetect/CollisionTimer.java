package de.engine.colldetect;

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
        else
        {
            return getCirclePolygonsCollTime(o2, o1, begin, end);
        }
        //return -1;
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
    
    public static double getCirclePolygonsCollTime(ObjectProperties o1, ObjectProperties o2, double begin, double end)
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
            if (o1 instanceof Circle && o2 instanceof Polygon) {
                coll.put(i, collideCirclePolygon((Circle) o1, (Polygon) o2, times.get(i)));
            }
            else if (o1 instanceof Polygon && o2 instanceof Circle) {
                coll.put(i, collideCirclePolygon((Circle) o2, (Polygon) o1, times.get(i)));
            }
            else if (o1 instanceof Polygon && o2 instanceof Polygon) {
                coll.put(i, collidePolygons((Polygon) o1, (Polygon) o2, times.get(i)));
            } else {
                return -1;
            }
            
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
            if (!coll.containsValue(true) && time_delta < 0.005)
            {
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
            if (!Helper.collideCirclePolygonAxis(axis, o1, o2, time))
                return false;
        }
        for (int i = 0; i < o2.points.length; i++)
        {
            Vector axis = Util.minus(o1.getPosition(), o2.getWorldPointPos(i, time));
            if (!Helper.collideCirclePolygonAxis(axis, o1, o2, time))
                return false;
        }
        return true;
    }
    
    private static boolean collidePolygons(Polygon o1, Polygon o2, double time)
    {
        for (int i = 0; i < o1.points.length; i++)
        {
            int j = (i == o1.points.length - 1) ? 0 : i + 1;
            Vector axis = Util.minus(o1.getWorldPointPos(j, time), o1.getWorldPointPos(i, time)).getNormalVector().getUnitVector();
            if (!Helper.collidePolygonPolygonAxis(axis, o1, o2, time))
                return false;
        }
        for (int i = 0; i < o2.points.length; i++)
        {
            int j = (i == o2.points.length - 1) ? 0 : i + 1;
            Vector axis = Util.minus(o2.getWorldPointPos(j, time), o2.getWorldPointPos(i, time)).getNormalVector().getUnitVector();
            if (!Helper.collidePolygonPolygonAxis(axis, o1, o2, time))
                return false;
        }
        return true;
    }
}
