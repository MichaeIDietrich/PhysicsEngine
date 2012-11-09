package de.engine.colldetect;

import java.util.ArrayList;

import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;

public class CollisionData
{
    
    public ObjectProperties o1, o2;
    
    public ArrayList<Contact> contacts;
    public double time;
    
    public CollisionData(ObjectProperties o1, ObjectProperties o2, double time)
    {
        this.o1 = o1;
        this.o2 = o2;
        this.time = time;
        contacts = new ArrayList<>();
    }
    
    public static class Contact
    {
        public Vector point;
        public Vector normal;
        
        // double penetration;
        
        public Contact(Vector point, Vector normal)
        {
            this.point = point;
            this.normal = normal;
        }
    }
}
