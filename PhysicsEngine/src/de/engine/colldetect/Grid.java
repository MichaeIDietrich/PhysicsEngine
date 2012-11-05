package de.engine.colldetect;

import java.util.HashMap;
import java.util.Set;

import de.engine.environment.EnvProps;
import de.engine.environment.Scene;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;

public class Grid
{
    private static class Element
    {
        int id;
        double min_time;
        double max_time;
    }
    
    private double cellSize = 5;
    public Scene scene;
    private HashMap<Integer, HashMap<Integer, java.util.Vector<Element>>> objectFields;
    java.util.Vector<Double[]> coll_times;
    
    public Grid(Scene scene)
    {
        this.scene = scene;
        objectFields = new HashMap<>();
    }
    
    public void scanScene()
    {
        this.reset();
        for (int i = 0; i < scene.getCount(); i++)
        {
            this.scanFieldsForObjectWithSweep(scene.getObject(i), i);
        }
    }
    
    public void reset()
    {
        objectFields = new HashMap<>();
    }
    
    private void scan(Vector[] aabb, Element element)
    {
        int minx = (int) (aabb[0].getX() / cellSize) + 1;
        int miny = (int) (aabb[0].getY() / cellSize) + 1;
        int maxx = (int) (aabb[1].getX() / cellSize) + 1;
        int maxy = (int) (aabb[1].getY() / cellSize) + 1;
        
        for (int i = minx; i <= maxx; i++)
        {
            for (int j = miny; j <= maxy; j++)
            {
                if (objectFields.containsKey(i))
                {
                    if (objectFields.get(i).containsKey(j))
                    {
                        boolean inserted = false;
                        for (Element e : objectFields.get(i).get(j))
                        {
                            if (element.id == e.id)
                            {
                                e.max_time = element.max_time;
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted)
                        {
                            objectFields.get(i).get(j).add(element);
                        }
                    }
                    else
                    {
                        java.util.Vector<Element> ops = new java.util.Vector<Element>();
                        ops.add(element);
                        objectFields.get(i).put(j, ops);
                    }
                }
                else
                {
                    HashMap<Integer, java.util.Vector<Element>> row = new HashMap<Integer, java.util.Vector<Element>>();
                    java.util.Vector<Element> ops = new java.util.Vector<Element>();
                    ops.add(element);
                    row.put(j, ops);
                    objectFields.put(i, row);
                }
            }
            
        }
    }
    
    public void scanFieldsForObjectWithSweep(ObjectProperties op, Integer id)
    {
        // tuning needed
        double d_step = Util.scale(op.velocity, EnvProps.deltaTime()).getLength() / (2 * op.getRadius());
        double d_time = EnvProps.deltaTime() / d_step;
        
        Element element = new Element();
        element.id = id;
        element.min_time = 0.0;
        if (1 > ((int) d_step))
        {
            element.max_time = EnvProps.deltaTime();
        }
        Vector[] aabb = op.getAABB();
        scan(aabb, element);
        
        double help_time = 0;
        for (int i = 1; i <= (int) d_step; i++)
        {
            element = new Element();
            element.id = id;
            element.min_time = d_time * (i - 1);
            help_time = element.min_time;
            element.max_time = d_time * (i + 1);
            aabb = op.getAABB(d_time * i);
            scan(aabb, element);
        }
        
        element = new Element();
        element.id = id;
        element.min_time = help_time;
        element.max_time = EnvProps.deltaTime();
        aabb = op.getNextAABB();
        scan(aabb, element);
    }
    
    public java.util.Vector<Integer[]> getCollisionPairs()
    {
        java.util.Vector<Integer[]> collisionPairs = new java.util.Vector<Integer[]>();
        coll_times = new java.util.Vector<Double[]>();
        Set<Integer> xs = objectFields.keySet();
        for (Integer x : xs)
        {
            Set<Integer> ys = objectFields.get(x).keySet();
            for (Integer y : ys)
            {
                if (1 < objectFields.get(x).get(y).size())
                {
                    java.util.Vector<Element> ops = objectFields.get(x).get(y);
                    for (int i = 0; i < ops.size() - 1; i++)
                    {
                        for (int j = i + 1; j < ops.size(); j++)
                        {
                            if (ops.get(i).min_time < ops.get(j).max_time || ops.get(j).min_time < ops.get(i).max_time)
                            {
                                Integer[] opPair = new Integer[2];
                                if (ops.get(i).id < ops.get(j).id)
                                {
                                    opPair[0] = ops.get(i).id;
                                    opPair[1] = ops.get(j).id;
                                }
                                else
                                {
                                    opPair[0] = ops.get(j).id;
                                    opPair[1] = ops.get(i).id;
                                }
                                if (!collisionPairs.contains(opPair))
                                {
                                    collisionPairs.add(opPair);
                                    Double[] min_max_times = new Double[2];
                                    min_max_times[0] = (ops.get(i).min_time < ops.get(j).min_time) ? ops.get(i).min_time : ops.get(j).min_time;
                                    min_max_times[1] = (ops.get(i).max_time > ops.get(j).max_time) ? ops.get(i).max_time : ops.get(j).max_time;
                                    coll_times.add(min_max_times);
                                }
                            }
                        }
                    }
                }
            }
        }
        return collisionPairs;
    }
}
