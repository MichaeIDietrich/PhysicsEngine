package de.engine.physics;

import de.engine.colldetect.CollisionData;
import de.engine.colldetect.ContactCreator;
import de.engine.environment.EnvProps;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Polygon;

public class PhysicsCalcer
{
    
    public static void calcCircles(Circle c1, Circle c2, double collTime)
    {
        Vector dist = Util.minus(c2.getPosition(), c1.getPosition());
        
        double alpha = Util.scalarProduct(c1.velocity, dist) / Util.scalarProduct(dist, dist);
        Vector c1_vp = Util.scale(dist, alpha);
        Vector c1_vs = Util.minus(c1.velocity, c1_vp);
        
        double beta = Util.scalarProduct(c2.velocity, dist) / Util.scalarProduct(dist, dist);
        Vector c2_vp = Util.scale(dist, beta);
        Vector c2_vs = Util.minus(c2.velocity, c2_vp);
        
        double afterCollTime = EnvProps.deltaTime() - collTime;
        
        c1.update(collTime);
        c2.update(collTime);
        if (c1.getMass() == c2.getMass())
        {
            c1.velocity = c1_vs.add(c2_vp);
            c2.velocity = c2_vs.add(c1_vp);
        }
        else
        {
            Vector u = Util.scale(c1_vp, c1.getMass()).add(Util.scale(c2_vp, c2.getMass())).scale(2 / (c1.getMass() + c2.getMass()));
            c1.velocity = Util.add(Util.minus(u, c1_vp), c1_vs);
            c2.velocity = Util.add(Util.minus(u, c2_vp), c2_vs);
        }
        c1.update(afterCollTime);
        c2.update(afterCollTime);
    }
    
    public static void calcCirclePolygon(Circle o1, Polygon o2, double collTime)
    {
        CollisionData cd = ContactCreator.getCirclePolygonContact(o1, o2, collTime);
        if (cd.contacts.size() > 0)
        {
            Vector coll_point = cd.contacts.get(0).point;
            Vector coll_normal = cd.contacts.get(0).normal;
            
            Vector r_o1 = Util.minus(coll_point, o1.getPosition(collTime));
            Vector r_o2 = Util.minus(coll_point, o2.getPosition(collTime));
            Vector v_o1 = new Vector(-1 * o1.angular_velocity * r_o1.getX(), o1.angular_velocity * r_o1.getY()).add(o1.velocity);
            Vector v_o2 = new Vector(-1 * o2.angular_velocity * r_o2.getX(), o2.angular_velocity * r_o2.getY()).add(o2.velocity);
            Vector v_rel = Util.minus(v_o1, v_o2);
            
            double j_z = Util.scalarProduct(Util.scale(v_rel, -(1 + (o1.surface.elasticity() + o2.surface.elasticity()) / 2)), coll_normal);
            double r_o1_cross_n = (r_o1.getX() * coll_normal.getY()) - (r_o1.getY() * coll_normal.getX());
            double r_o2_cross_n = (r_o2.getX() * coll_normal.getY()) - (r_o2.getY() * coll_normal.getX());
            double j_n = (1 / o1.getMass()) + (1 / o2.getMass()) + (r_o1_cross_n * r_o1_cross_n) / o1.moment_of_inertia + (r_o2_cross_n * r_o2_cross_n) / o2.moment_of_inertia;
            double j = j_z / j_n;
            
            Vector j_normal = Util.scale(coll_normal, j);
            
            Vector v_o1_n = Util.add(o1.velocity, Util.scale(j_normal, 1 / o1.getMass()));
            Vector v_o2_n = Util.add(o2.velocity, Util.scale(j_normal, 1 / o2.getMass()));
            
            double ang_v_o1_n = o1.angular_velocity + (r_o1.getX() * j_normal.getY() - r_o1.getY() * j_normal.getX()) / o1.moment_of_inertia;
            double ang_v_o2_n = o2.angular_velocity + (r_o2.getX() * j_normal.getY() - r_o2.getY() * j_normal.getX()) / o2.moment_of_inertia;
            
            o1.update(collTime);
            o2.update(collTime);
            
            o1.velocity = v_o1_n;
            o2.velocity = v_o2_n;
            
            o1.angular_velocity = ang_v_o1_n;
            o2.angular_velocity = ang_v_o2_n;
            
            double afterCollTime = EnvProps.deltaTime() - collTime;

            o1.update(afterCollTime);
            o2.update(afterCollTime);
        }
    }
    
    public static void calcPolygons(Polygon o1, Polygon o2, double collTime)
    {
    }
}
