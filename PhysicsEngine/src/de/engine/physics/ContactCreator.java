package de.engine.physics;

import java.util.ArrayList;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;

public class ContactCreator
{
    public static class Contact
    {
        public Vector point;
        public Vector normal;
        public double penetration;
        
        public boolean from_second;
        
        public Contact(Vector point, Vector normal, double penetration)
        {
            this.point = point;
            this.normal = normal;
            this.penetration = penetration;
            this.from_second = true;
        }
        
        public Contact(Vector point, Vector normal, double penetration, boolean from_second)
        {
            this.point = point;
            this.normal = normal;
            this.penetration = penetration;
            this.from_second = from_second;
        }
    }
    
    public static Contact getCirclesContact(Circle o1, Circle o2, double time)
    {
        Vector pos_o2 = o2.getPosition(time);
        Vector pos_o1 = o1.getPosition(time);
        Vector dist = Util.minus(pos_o1, pos_o2);
        Vector normal = dist.getUnitVector();
        Vector coll_point = Util.add(pos_o2, dist.scale(o2.getRadius() / dist.getLength()));
        return new Contact(coll_point, normal, 0.1);
        
    }
    
    public static Contact getCirclePolygonContact(Circle o1, Polygon o2, double time)
    {
        Vector circle_pos = o1.getPosition(time);
        for (int i = 0; i < o2.points.length; i++)
        {
            int j = (i == (o2.points.length - 1)) ? 0 : i + 1;
            Vector point_pos = o2.getWorldPointPos(i, time);
            Vector edge_ray = Util.minus(o2.getWorldPointPos(j, time), point_pos);
            Vector pos_ray = edge_ray.getNormalVector().getUnitVector();
            
            Vector coll_point = Util.crossEdges(circle_pos, Util.scale(pos_ray, -1 * o1.getRadius()), point_pos, edge_ray);
            if (coll_point != null)
            {
                return new Contact(coll_point, pos_ray, o1.getRadius() - Util.distance(circle_pos, coll_point));
            } else {
                Vector point_col = Util.minus(point_pos, circle_pos);
                if(point_col.getLength() <= o1.getRadius()){
                    return new Contact(point_pos, point_col.getUnitVector(), o1.getRadius() - point_col.getLength(), false);
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Contact> getPolygonsContact(Polygon o1, Polygon o2, double time)
    {
        ArrayList<Contact> contacts = searchContact(o1, o2, time, true);
        contacts.addAll(searchContact(o2, o1, time, false));
        return contacts;
    }
    
    private static ArrayList<Contact> searchContact(Polygon o1, Polygon o2, double time, boolean from_second)
    {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < o1.points.length; i++)
        {
            Vector corner_o1 = o1.getWorldPointPos(i, time);
            for (int k = 0; k < o2.points.length; k++)
            {
                int l = (k == (o2.points.length - 1)) ? 0 : k + 1;
                Vector corner_o2 = o2.getWorldPointPos(k, time);
                //Vector dist = Util.minus(corner_o1, corner_o2);
                Vector edge = o2.getWorldPointPos(l, time).minus(corner_o2);
                Vector edge_normal = edge.getNormalVector().getUnitVector();
                //double distance = Util.scalarProduct(dist, ray_normal);
                Vector cross_point = Util.crossEdges(corner_o1, edge_normal, corner_o2, edge);
                if(cross_point != null)// && Util.scalarProduct(edge_normal, Util.minus(o1.getPosition(time), corner_o1)) > 0)
                {
                    contacts.add(new Contact(cross_point, edge_normal, Util.distance(cross_point, corner_o1), from_second));
                }
            }
        }
        return contacts;
    }
}
