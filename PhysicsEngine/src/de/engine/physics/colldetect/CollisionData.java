package de.engine.physics.colldetect;

import de.engine.objects.ObjectProperties;

public class CollisionData
{
    
    public ObjectProperties obj1, obj2;
    public Double min_time;
    public Double max_time;
    
    public Double coll_time;
    
    public CollisionData(ObjectProperties o1, ObjectProperties o2, Double min_time, Double max_time)
    {
        this.obj1 = o1;
        this.obj2 = o2;
        this.min_time = min_time;
        this.max_time = max_time;
        this.coll_time = null;
    }
}
