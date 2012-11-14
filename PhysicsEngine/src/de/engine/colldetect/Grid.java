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
        ObjectProperties obj;
        //int id;
        double min_time;
        double max_time;
        
        public Element(ObjectProperties obj, double min_time, double max_time)
        {
            this.obj = obj;
            this.min_time = min_time;
            this.max_time = max_time;
        }
        
        public Element(Element e)
        {
            this.obj = e.obj;
            this.min_time = e.min_time;
            this.max_time = e.max_time;
        }
    }
    
    private double cellSize = 10;
    public Scene scene;
    private HashMap<Integer, HashMap<Integer, java.util.Vector<Element>>> objectFields;
    java.util.Vector<ObjectProperties[]> collisionPairs;
    java.util.Vector<Double[]> coll_times;
    java.util.Vector<Double> coll_time;
    
    public Grid(Scene scene)
    {
        this.scene = scene;
        reset();
    }
    
    public void scanScene()
    {
        this.reset();
        for (int i = 0; i < scene.getCount(); i++)
        {
            this.scanFieldsForObjectWithSweep(scene.getObject(i));
        }
    }
    
    public void reset()
    {
        objectFields = new HashMap<>();
        collisionPairs = new java.util.Vector<>();
        coll_times = new java.util.Vector<>();
        coll_time = new java.util.Vector<>();
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
                            if (element.obj.getId() == e.obj.getId())
                            {
                                e.max_time = element.max_time;
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted)
                        {
                            objectFields.get(i).get(j).add(new Element(element));
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
    
    public void scanFieldsForObjectWithSweep(ObjectProperties op)
    {
        // tuning needed
        double d_step = Util.scale(op.velocity, op.getTime()).getLength() / (2 * op.getRadius());
        double d_time = op.getTime() / d_step;
        
        Element element;
        if (1 > ((int) d_step))
            element = new Element(op, op.frametime, EnvProps.deltaTime());
        else
            element = new Element(op, op.frametime, d_time);
        Vector[] aabb = op.getAABB();
        scan(aabb, element);
        
        double help_time = 0;
        for (int i = 1; i <= (int) d_step; i++)
        {
            element = new Element(op, op.frametime + d_time * (i - 1), op.frametime + d_time * (i + 1));
            help_time = element.min_time;
            aabb = op.getAABB(d_time * i);
            scan(aabb, element);
        }
        
        element = new Element(op, help_time, EnvProps.deltaTime());
        aabb = op.getNextAABB();
        scan(aabb, element);
    }
    
    public void calcCollisionPairs()
    {
        collisionPairs = new java.util.Vector<ObjectProperties[]>();
        coll_times = new java.util.Vector<Double[]>();
        coll_time = new java.util.Vector<>();
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
                            insertCollPair(ops, i, j);
                        }
                    }
                }
            }
        }
    }
    
    public Integer getNextCollision()
    {
        if (0 < coll_times.size())
        {
            int index = 0;
            Double colltime = CollisionTimer.getCollTime(collisionPairs.get(index)[0], collisionPairs.get(index)[1], coll_times.get(index)[0], coll_times.get(index)[1]);
            coll_time.set(index, colltime);
            int i = 1;
            while ( i < coll_times.size())
            {
                if(colltime == null)
                {
                    colltime = CollisionTimer.getCollTime(collisionPairs.get(i)[0], collisionPairs.get(i)[1], coll_times.get(i)[0], coll_times.get(i)[1]);
                    collisionPairs.remove(index);
                    coll_times.remove(index);
                    coll_time.remove(index);
                    index = i - 1;
                    coll_time.set(index, colltime);
                    continue; 
                }
                
                if (coll_times.get(i)[0] <= colltime && colltime <= coll_times.get(i)[1])
                {
                    if(coll_time.get(i) == null)
                    {
                        Double colltime_i = CollisionTimer.getCollTime(collisionPairs.get(i)[0], collisionPairs.get(i)[1], coll_times.get(i)[0], coll_times.get(i)[1]);
                        if(colltime_i == null)
                        {
                            collisionPairs.remove(i);
                            coll_times.remove(i);
                            coll_time.remove(i);
                            continue;
                        }
                        coll_time.set(i, colltime_i);   
                    }
                    if(coll_time.get(i) < colltime)
                    {
                        index = i;
                        colltime = coll_time.get(i);
                    }
                }
                i++;
            }
            if(colltime == null)
            {
                collisionPairs.remove(index);
                coll_times.remove(index);
                coll_time.remove(index);
                return null;   
            }
            else 
                return index;
        }
        return null;
    }
    
    private void clearCollPairs(ObjectProperties obj)
    {
        int i = 0;
        while (i < collisionPairs.size())
        {
            if (collisionPairs.get(i)[0].getId() == obj.getId() || collisionPairs.get(i)[1].getId() == obj.getId())
            {
                collisionPairs.remove(i);
                coll_times.remove(i);
                coll_time.remove(i);
            }
            else
                i++;
        }
    }
    
    private void clearFields(ObjectProperties obj)
    {
        Set<Integer> xs = objectFields.keySet();
        for (Integer x : xs)
        {
            Set<Integer> ys = objectFields.get(x).keySet();
            for (Integer y : ys)
            {
                int i = 0;
                while (i < objectFields.get(x).get(y).size())
                {
                    if (objectFields.get(x).get(y).get(i).obj.getId() == obj.getId())
                    {
                        objectFields.get(x).get(y).remove(i);
                        break;
                    }
                    i++;
                }
            }
        }
    }
    
    private void updateCollisionPairs(ObjectProperties obj)
    {
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
                        if (ops.get(i).obj.getId() == obj.getId())
                        {
                            for (int j = 0; j < ops.size(); j++)
                            {
                                if (i != j)
                                {
                                    insertCollPair(ops, i, j);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void update(ObjectProperties obj)
    {
        clearCollPairs(obj);
        clearFields(obj);
        scanFieldsForObjectWithSweep(obj);
        updateCollisionPairs(obj);
    }
    
    private void insertCollPair(java.util.Vector<Element> ops, int i, int j)
    {
        if (ops.get(i).min_time < ops.get(j).max_time && ops.get(j).min_time < ops.get(i).max_time)
        {
            ObjectProperties[] opPair = new ObjectProperties[2];
            if (ops.get(i).obj.getId() < ops.get(j).obj.getId())
            {
                opPair[0] = ops.get(i).obj;
                opPair[1] = ops.get(j).obj;
            }
            else
            {
                opPair[0] = ops.get(j).obj;
                opPair[1] = ops.get(i).obj;
            }
            if (!collisionPairs.contains(opPair))
            {
                collisionPairs.add(opPair);
                Double[] min_max_times = new Double[2];
                min_max_times[0] = (ops.get(i).min_time < ops.get(j).min_time) ? ops.get(i).min_time : ops.get(j).min_time;
                min_max_times[1] = (ops.get(i).max_time > ops.get(j).max_time) ? ops.get(i).max_time : ops.get(j).max_time;
                coll_times.add(min_max_times);
                coll_time.add(null);
            }
        }
        
    }
}
