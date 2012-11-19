package de.engine.physics;

import java.util.ArrayList;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;
import de.engine.objects.Polygon;
import de.engine.physics.ContactCreator.Contact;

public class PhysicsCalcer
{
    
    public static void run(ObjectProperties obj1, ObjectProperties obj2, double collTime)
    {
        if (obj1 instanceof Circle && obj2 instanceof Circle)
        {
            PhysicsCalcer.calcCircles((Circle) obj1, (Circle) obj2, collTime);
        }
        else if (obj1 instanceof Circle && obj2 instanceof Polygon)
        {
            PhysicsCalcer.calcCirclePolygon((Circle) obj1, (Polygon) obj2, collTime);
        }
        else if (obj1 instanceof Polygon && obj2 instanceof Circle)
        {
            PhysicsCalcer.calcCirclePolygon((Circle) obj2, (Polygon) obj1, collTime);
        }
        else if (obj1 instanceof Polygon && obj2 instanceof Polygon)
        {
            PhysicsCalcer.calcPolygons((Polygon) obj2, (Polygon) obj1, collTime);
        }
    }
    
    private static void calcCircles(Circle c1, Circle c2, double collTime)
    {
        Contact contact = ContactCreator.getCirclesContact(c1, c2, collTime);
        resolveContact(c1, c2, collTime, contact);
    }
    
    private static void calcCirclePolygon(Circle o1, Polygon o2, double collTime)
    {
        Contact contact = ContactCreator.getCirclePolygonContact(o1, o2, collTime);
        resolveContact(o1, o2, collTime, contact);
    }
    
    private static void calcPolygons(Polygon o1, Polygon o2, double collTime)
    {
        ArrayList<Contact> contacts = ContactCreator.getPolygonsContact(o1, o2, collTime);
        
        if (contacts.size() == 1)
        {
            resolveContact(o1, o2, collTime, contacts.get(0));
        }
        else if (contacts.size() == 2)
        {
            if (contacts.get(0).normal.getX() == contacts.get(1).normal.getX() && contacts.get(0).normal.getY() == contacts.get(1).normal.getY())
            {
                Contact c = new Contact(Util.add(contacts.get(0).point, contacts.get(1).point).scale(0.5), contacts.get(0).normal);
                resolveContact(o1, o2, collTime, c);
            }
            else
            {
                resolveContact(o1, o2, collTime, contacts.get(0));
                resolveContact(o1, o2, collTime, contacts.get(1));
            }
        }
        else if (contacts.size() == 4)
        {
            
            Contact c = new Contact(Util.add(contacts.get(0).point, contacts.get(1).point).scale(0.5), Util.add(contacts.get(0).normal, contacts.get(1).normal).getUnitVector());
            resolveContact(o1, o2, collTime, c);
        }
    }
    
    // Quelle: http://www.myphysicslab.com/collision.html
    private static void resolveContact(ObjectProperties o1, ObjectProperties o2, double collTime, Contact contact)
    {
        Vector coll_point = contact.point;
        Vector coll_normal = contact.normal;
        
        Vector r_o1 = Util.minus(coll_point, o1.getPosition(collTime));
        Vector r_o2 = Util.minus(coll_point, o2.getPosition(collTime));
        Vector v_o1 = new Vector();
        if (!o1.isPinned)
            v_o1 = new Vector(o1.angular_velocity * r_o1.getX(), o1.angular_velocity * r_o1.getY()).add(o1.velocity);
        Vector v_o2 = new Vector();
        if (!o2.isPinned)
            v_o2 = new Vector(o2.angular_velocity * r_o2.getX(), o2.angular_velocity * r_o2.getY()).add(o2.velocity);
        
        double r_o1_cross_n = Util.crossProduct(r_o1, coll_normal);
        double r_o2_cross_n = Util.crossProduct(r_o2, coll_normal);
        
        Vector v_rel = Util.minus(v_o1, v_o2);
        double j_z = Util.scalarProduct(v_rel, coll_normal) * -(1 + (o1.surface.elasticity() + o2.surface.elasticity()) / 2);
        double j_n;
        
        if (o1.isPinned)
            j_n = (1 / o2.getMass()) + (r_o2_cross_n * r_o2_cross_n) / o2.moment_of_inertia;
        else if (o2.isPinned)
            j_n = (1 / o1.getMass()) + (r_o1_cross_n * r_o1_cross_n) / o1.moment_of_inertia;
        else
            j_n = (1 / o1.getMass()) + (1 / o2.getMass()) + (r_o1_cross_n * r_o1_cross_n) / o1.moment_of_inertia + (r_o2_cross_n * r_o2_cross_n) / o2.moment_of_inertia;
        
        double j = j_z / j_n;
        
        //weird hack, but solves a problem in square_n_circles.scnx, will check this later again
        if(j < 0 && !o1.isPinned && !o2.isPinned) j *= -1.0;
        
        Vector j_normal = Util.scale(coll_normal, j);
        
        if (!o1.isPinned)
        {
            o1.update(collTime);
            
            o1.velocity.add(Util.scale(j_normal, 1 / o1.getMass()));
            o1.angular_velocity += Util.crossProduct(r_o1, j_normal) / o1.moment_of_inertia;
        }
        
        if (!o2.isPinned)
        {
            o2.update(collTime);
            
            o2.velocity.minus(Util.scale(j_normal, 1 / o2.getMass()));
            o2.angular_velocity -= Util.crossProduct(r_o2, j_normal) / o2.moment_of_inertia;
        }
    }
}
