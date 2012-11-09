package de.engine.colldetect;

import java.util.ArrayList;

import de.engine.colldetect.CollisionData.Contact;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;

public class ContactCreator
{
    public static CollisionData getCirclesContact(Circle o1, Circle o2, double time)
    {
        CollisionData cd = new CollisionData(o1, o2, time);
        
        Vector pos_o2 = o2.getPosition(time);
        Vector pos_o1 = o1.getPosition(time);
        Vector dist = Util.minus(pos_o2, pos_o1);
        Vector normal = dist.getNormalVector().getUnitVector();
        Vector coll_point = Util.add(pos_o1, dist.scale(o1.getRadius() / dist.getLength()));
        cd.contacts.add(new Contact(coll_point, normal));
        
        return cd;
    }
    
    public static CollisionData getCirclePolygonContact(Circle o1, Polygon o2, double time)
    {
        CollisionData cd = new CollisionData(o1, o2, time);
        
        Vector circle_pos = o1.getPosition(time);
        for (int i = 0; i < o2.points.length; i++)
        {
            int j = (i == (o2.points.length - 1)) ? 0 : i + 1;
            Vector point_pos = o2.getWorldPointPos(i, time);
            Vector edge_ray = Util.minus(o2.getWorldPointPos(j, time), point_pos);
            Vector pos_ray = edge_ray.getNormalVector().getUnitVector();
            
            double d1xd2y = pos_ray.getX() * edge_ray.getY();
            double d1yd2x = pos_ray.getY() * edge_ray.getX();
            double s2 = (point_pos.getX() - circle_pos.getX()) / (pos_ray.getX() - (d1xd2y / d1yd2x)) + ((circle_pos.getY() - point_pos.getY()) * edge_ray.getX()) / (d1xd2y - d1yd2x);
            // double s1 = (pol_pos.getY() - point_pos.getY() + s2 * pos_ray.getY()) / edge_ray.getY();
            if (s2 >= 0 && s2 <= 1)
            {
                Vector coll_point = new Vector(point_pos.getX() + s2 * edge_ray.getX(), point_pos.getY() + s2 * edge_ray.getY());
                Vector normal = pos_ray.getNormalVector().getUnitVector();
                cd.contacts.add(new Contact(coll_point, normal));
            }
        }
        
        return cd;
    }
    
    public static CollisionData getPolygonsContact(Polygon o1, Polygon o2, double time)
    {
        CollisionData cd = new CollisionData(o1, o2, time);
        
        ArrayList<Contact> contacts = searchContact(o1, o2, time);
        for (Contact contact : contacts)
        {
            cd.contacts.add(contact);
        }
        contacts = searchContact(o2, o1, time);
        for (Contact contact : contacts)
        {
            cd.contacts.add(contact);
        }
        
        return cd;
    }
    
    private static ArrayList<Contact> searchContact(Polygon o1, Polygon o2, double time)
    {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < o1.points.length; i++)
        {
            for (int j = 0; j < o2.points.length; j++)
            {
                int k = (j == (o2.points.length - 1)) ? 0 : j + 1;
                Vector corner_o1 = o1.getWorldPointPos(i, time);
                Vector corner_o2 = o2.getWorldPointPos(j, time);
                Vector dist = Util.minus(corner_o1, corner_o2);
                Vector ray_normal = o2.getWorldPointPos(k, time).minus(corner_o2).getNormalVector().getUnitVector();
                double distance = Util.scalarProduct(dist, ray_normal);
                if (distance < 0)
                {
                    contacts.add(new Contact(corner_o1, ray_normal));
                }
            }
        }
        return contacts;
    }
}
