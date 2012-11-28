package de.engine.physics.colldetect;

import java.util.ArrayList;
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
    
    private double cellSize = 50;
    public Scene scene;
    private HashMap<Integer, HashMap<Integer, ArrayList<Element>>> objectFields;
    ArrayList<CollisionData> collisionPairs;
    
    public Grid(Scene scene)
    {
        this.scene = scene;
        objectFields = new HashMap<>();
        collisionPairs = new ArrayList<>();
    }
    
    public void scanScene()
    {
        objectFields.clear();
        collisionPairs.clear();
        
        for (int i = 0; i < scene.getCount(); i++)
        {
            this.scanFieldsForObjectWithSweep(scene.getObject(i));
        }
        calcCollisionPairs();
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
                        ArrayList<Element> ops = new ArrayList<Element>();
                        ops.add(element);
                        objectFields.get(i).put(j, ops);
                    }
                }
                else
                {
                    HashMap<Integer, ArrayList<Element>> row = new HashMap<Integer, ArrayList<Element>>();
                    ArrayList<Element> ops = new ArrayList<Element>();
                    ops.add(element);
                    row.put(j, ops);
                    objectFields.put(i, row);
                }
            }
            
        }
    }
    
    private void scanFieldsForObjectWithSweep(ObjectProperties op)
    {
        Element element;
        if(op.isPinned)
        {
            element = new Element(op, op.getFrameTime(), EnvProps.deltaTime());
            Vector[] aabb = op.getAABB();
            scan(aabb, element);
            return;
        }
        
        double d_step =  Util.scale(op.velocity, op.getTime()).getLength() / (2 * op.getRadius());
        double d_time = op.getTime() / d_step;
        
        if (1 > ((int) d_step))
            element = new Element(op, op.getFrameTime(), EnvProps.deltaTime());
        else
            element = new Element(op, op.getFrameTime(), d_time);
        Vector[] aabb = op.getAABB();
        scan(aabb, element);
        
        double help_time = op.getFrameTime();
        for (int i = 1; i <= (int) d_step; i++)
        {
            element = new Element(op, op.getFrameTime() + d_time * (i - 1), op.getFrameTime() + d_time * (i + 1));
            help_time = element.min_time;
            aabb = op.getAABB(d_time * i);
            scan(aabb, element);
        }
        
        element = new Element(op, help_time, EnvProps.deltaTime());
        aabb = op.getNextAABB();
        scan(aabb, element);
    }
    
    private void calcCollisionPairs()
    {
        Set<Integer> xs = objectFields.keySet();
        for (Integer x : xs)
        {
            Set<Integer> ys = objectFields.get(x).keySet();
            for (Integer y : ys)
            {
                if (1 < objectFields.get(x).get(y).size())
                {
                    ArrayList<Element> ops = objectFields.get(x).get(y);
                    for (int i = 0; i < ops.size() - 1; i++)
                    {
                        for (int j = i + 1; j < ops.size(); j++)
                        {
                            insertCollPair(ops.get(i), ops.get(j));
                        }
                    }
                }
            }
        }
    }
    
    public CollisionData getNextCollision()
    {
        if (0 < collisionPairs.size())
        {
            int index = 0;
            CollisionTimer.calcCollTime(collisionPairs.get(index));
            Double colltime = collisionPairs.get(index).coll_time;
            collisionPairs.get(index).coll_time = colltime;
            int i = 1;
            while (i < collisionPairs.size())
            {
                if (colltime == null)
                {
                    CollisionTimer.calcCollTime(collisionPairs.get(i));
                    colltime = collisionPairs.get(i).coll_time;
                    collisionPairs.remove(index);
                    index = i - 1;
                    collisionPairs.get(index).coll_time = colltime;
                    continue;
                }
                
                CollisionData collPair = collisionPairs.get(i);
                if (collPair.min_time <= colltime && collPair.coll_time == null)
                {
                    CollisionTimer.calcCollTime(collPair);
                    Double colltime_i = collPair.coll_time;
                    if (colltime_i == null)
                    {
                        collisionPairs.remove(i);
                        continue;
                    }
                    collPair.coll_time = colltime_i;
                }
                if (collPair.coll_time != null && collPair.coll_time < colltime)
                {
                    index = i;
                    colltime = collPair.coll_time;
                }
                i++;
            }
            if (colltime == null)
            {
                collisionPairs.remove(index);
                return null;
            }
            else
                return collisionPairs.get(index);
        }
        return null;
    }
    
    private void clearCollPairs(ObjectProperties obj)
    {
        int i = 0;
        while (i < collisionPairs.size())
        {
            if (collisionPairs.get(i).obj1== obj || collisionPairs.get(i).obj2 == obj)
            {
                collisionPairs.remove(i);
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
                    ArrayList<Element> ops = objectFields.get(x).get(y);
                    for (int i = 0; i < ops.size(); i++)
                    {
                        if (ops.get(i).obj.getId() == obj.getId())
                        {
                            for (int j = 0; j < ops.size(); j++)
                            {
                                if (i != j)
                                {
                                    insertCollPair(ops.get(i), ops.get(j));
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
    
    private void insertCollPair(Element e1, Element e2)
    {
        if(e1.obj.isPinned && e2.obj.isPinned)
            return;
        if (e1.min_time < e2.max_time && e2.min_time < e1.max_time)
        {
            for (CollisionData collPair : collisionPairs)
            {
                if ((collPair.obj1 == e1.obj && collPair.obj2 == e2.obj) || (collPair.obj1 == e2.obj && collPair.obj2 == e1.obj))
                {
                    return;
                }
            }
            Double min_time;
            if(e1.obj.isPinned)
                min_time = e2.min_time;
            else if(e2.obj.isPinned)
                min_time = e1.min_time;
            else
                min_time = (e1.min_time < e2.min_time) ? e1.min_time : e2.min_time;
            Double max_time;
            if(e1.obj.isPinned)
                max_time = e2.max_time;
            else if(e2.obj.isPinned)
                max_time = e1.max_time;
            else
                max_time = (e1.max_time > e2.max_time) ? e1.max_time : e2.max_time;
            collisionPairs.add(new CollisionData(e1.obj, e2.obj, min_time, max_time));
        }
    }
}
