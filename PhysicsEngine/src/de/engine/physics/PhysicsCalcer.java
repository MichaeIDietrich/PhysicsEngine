package de.engine.physics;

import java.util.ArrayList;

import de.engine.environment.EnvProps;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;
import de.engine.physics.ContactCreator.Contact;
import de.engine.physics.colldetect.CollisionData;

public class PhysicsCalcer
{
    
    public static CollisionData run(CollisionData collPair)
    {
        if (collPair.obj1 instanceof Circle && collPair.obj2 instanceof Circle)
        {
            return PhysicsCalcer.calcCircles(collPair);
        }
        else if (collPair.obj1 instanceof Polygon && collPair.obj2 instanceof Polygon)
        {
            return PhysicsCalcer.calcPolygons(collPair);
        }
        else
        {
            return PhysicsCalcer.calcCirclePolygon(collPair);
        }
    }
    
    private static CollisionData calcCircles(CollisionData collPair)
    {
        Contact contact = ContactCreator.getCirclesContact((Circle) collPair.obj1, (Circle) collPair.obj2, collPair.coll_time);
        return resolveContact(collPair, contact);
    }
    
    private static CollisionData calcCirclePolygon(CollisionData collPair)
    {
        Contact contact = null;
        if (collPair.obj1 instanceof Circle && collPair.obj2 instanceof Polygon)
            contact = ContactCreator.getCirclePolygonContact((Circle) collPair.obj1, (Polygon) collPair.obj2, collPair.coll_time);
        else if (collPair.obj1 instanceof Polygon && collPair.obj2 instanceof Circle)
        {
            contact = ContactCreator.getCirclePolygonContact((Circle) collPair.obj2, (Polygon) collPair.obj1, collPair.coll_time);
            if(contact != null)
                contact.normal.scale(-1.0);
        }
        return resolveContact(collPair, contact);
    }
    
    private static CollisionData calcPolygons(CollisionData collPair)
    {
        ArrayList<Contact> contacts = ContactCreator.getPolygonsContact((Polygon) collPair.obj1, (Polygon) collPair.obj2, collPair.coll_time);
        
        if (contacts.size() == 1)
        {
            return resolveContact(collPair, contacts.get(0));
        }
        else if (contacts.size() == 2)
        {
            if (contacts.get(0).normal.getX() == contacts.get(1).normal.getX() && contacts.get(0).normal.getY() == contacts.get(1).normal.getY())
            {
                Contact c = new Contact(Util.add(contacts.get(0).point, contacts.get(1).point).scale(0.5), contacts.get(0).normal);
                return resolveContact(collPair, c);
            }
            else
            {
                resolveContact(collPair, contacts.get(0));
                return resolveContact(collPair, contacts.get(1));
            }
        }
        else if (contacts.size() == 4)
        {
            
            Contact c = new Contact(Util.add(contacts.get(0).point, contacts.get(1).point).scale(0.5), Util.add(contacts.get(0).normal, contacts.get(1).normal).getUnitVector());
            return resolveContact(collPair, c);
        }
        return null;
    }
    
    // Quelle: http://www.myphysicslab.com/collision.html
    private static CollisionData resolveContact(CollisionData collPair, Contact contact)
    {
        if (contact == null)
            return null;
        
        Vector coll_point = contact.point;
        Vector coll_normal = contact.normal;
        
        Vector r_o1 = Util.minus(coll_point, collPair.obj1.getPosition(collPair.coll_time));
        Vector r_o2 = Util.minus(coll_point, collPair.obj2.getPosition(collPair.coll_time));
        Vector v_o1 = new Vector();
        if (!collPair.obj1.isPinned)
            v_o1 = new Vector(collPair.obj1.angular_velocity * r_o1.getX(), collPair.obj1.angular_velocity * r_o1.getY()).add(collPair.obj1.velocity);
        Vector v_o2 = new Vector();
        if (!collPair.obj2.isPinned)
            v_o2 = new Vector(collPair.obj2.angular_velocity * r_o2.getX(), collPair.obj2.angular_velocity * r_o2.getY()).add(collPair.obj2.velocity);
        
        double r_o1_cross_n = Util.crossProduct(r_o1, coll_normal);
        double r_o2_cross_n = Util.crossProduct(r_o2, coll_normal);
        
        Vector v_rel = Util.minus(v_o1, v_o2);
        double j_z = Util.scalarProduct(v_rel, coll_normal) * -(1 + (collPair.obj1.surface.elasticity() + collPair.obj2.surface.elasticity()) / 2);
        double j_n;
        
        if (collPair.obj1.isPinned)
            j_n = (1 / collPair.obj2.getMass()) + (r_o2_cross_n * r_o2_cross_n) / collPair.obj2.moment_of_inertia;
        else if (collPair.obj2.isPinned)
            j_n = (1 / collPair.obj1.getMass()) + (r_o1_cross_n * r_o1_cross_n) / collPair.obj1.moment_of_inertia;
        else
            j_n = (1 / collPair.obj1.getMass()) + (1 / collPair.obj2.getMass()) + (r_o1_cross_n * r_o1_cross_n) / collPair.obj1.moment_of_inertia + (r_o2_cross_n * r_o2_cross_n) / collPair.obj2.moment_of_inertia;
        
        double j = j_z / j_n;
        
        // weird hack, but solves a problem in square_n_circles.scnx, will check this later again
        //if(j < 0 && !collPair.obj1.isPinned && !collPair.obj2.isPinned) j *= -1.0;
        //if (j < 0 && (collPair.obj1 instanceof Polygon || collPair.obj2 instanceof Polygon)) j *= -1.0;
        
        Vector j_normal = Util.scale(coll_normal, j);
        
        double min_v = 1.0;
        
        if (!collPair.obj1.isPinned)
        {
            if (collPair.obj1.sleeps())
                collPair.obj1.wakeUp();
            collPair.obj1.update(collPair.coll_time);
            
            collPair.obj1.velocity.add(Util.scale(j_normal, 1 / collPair.obj1.getMass()));
            collPair.obj1.angular_velocity += Util.crossProduct(r_o1, j_normal) / collPair.obj1.moment_of_inertia;
            
            if ((collPair.obj2.isPinned || collPair.obj2.sleeps()) && ((-1 * min_v < collPair.obj1.velocity.getX() && collPair.obj1.velocity.getX() < min_v) && (-1 * min_v < collPair.obj1.velocity.getY() && collPair.obj1.velocity.getY() < min_v)))
            {
                collPair.obj1.velocity = new Vector();
                collPair.obj1.fallAsleep(collPair.obj2);
            }
            
        }
        
        if (!collPair.obj2.isPinned)
        {
            if (collPair.obj2.sleeps())
                collPair.obj2.wakeUp();
            collPair.obj2.update(collPair.coll_time);
            
            collPair.obj2.velocity.minus(Util.scale(j_normal, 1 / collPair.obj2.getMass()));
            collPair.obj2.angular_velocity -= Util.crossProduct(r_o2, j_normal) / collPair.obj2.moment_of_inertia;
            
            if ((collPair.obj1.isPinned || collPair.obj1.sleeps()) && ((-1 * min_v < collPair.obj2.velocity.getX() && collPair.obj2.velocity.getX() < min_v) && (-1 * min_v < collPair.obj2.velocity.getY() && collPair.obj2.velocity.getY() < min_v)))
            {
                collPair.obj2.velocity = new Vector();
                collPair.obj2.fallAsleep(collPair.obj1);
            }
            
        }
        
        Vector obj1_comp = Util.getVectorComponents(collPair.obj1.velocity, coll_normal, coll_normal.getNormalVector());
        Vector obj2_comp = Util.getVectorComponents(collPair.obj2.velocity, coll_normal, coll_normal.getNormalVector());
        
        if (((-1 * min_v < obj1_comp.getX() * coll_normal.getX() && obj1_comp.getX() * coll_normal.getX() < min_v) && (-1 * min_v < obj1_comp.getX() * coll_normal.getY() && obj1_comp.getX() * coll_normal.getY() < min_v)) && 
                ((-1 * min_v < obj2_comp.getX() * coll_normal.getX() && obj2_comp.getX() * coll_normal.getX() < min_v) && (-1 * min_v < obj2_comp.getX() * coll_normal.getY() && obj2_comp.getX() * coll_normal.getY() < min_v)))
        {
            CollisionData restingContact = new CollisionData(collPair.obj1, collPair.obj2, 0.0, EnvProps.deltaTime());
            restingContact.coll_time = 0.0;
            restingContact.calc_time = 0.0;
            return restingContact;
        }
        
        return null;
    }
}
