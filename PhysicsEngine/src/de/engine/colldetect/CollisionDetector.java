package de.engine.colldetect;

import java.util.Vector;

import de.engine.environment.Scene;
import de.engine.math.Util;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;
import de.engine.objects.Polygon;
import de.engine.physics.PhysicsCalcer;

public class CollisionDetector
{
    
    private Grid grid;
    
    public CollisionDetector(Scene scene)
    {
        grid = new Grid(scene);
    }
    
    /**
     * Einfache Überprüfung ob Kollision zwischen zwei Objekten stattfinden könnte.
     * 
     * @param pp1
     * @param pp2
     * @return
     */
    public static boolean needCheck(ObjectProperties pp1, ObjectProperties pp2)
    {
        double distance = Util.distance(pp1.getPosition(), pp2.getPosition());
        double min_distance = pp1.getRadius() + pp2.getRadius();
        return (distance <= min_distance) ? true : false;
    }
    
    public void checkScene()
    {
        grid.scanScene();
        Vector<Integer[]> collPairs = grid.getCollisionPairs();
        for (int i = 0; i < collPairs.size(); i++)
        {
            // if (needCheck(grid.scene.getObject(ops[0]), grid.scene.getObject(ops[1]))) {
            ObjectProperties o1 = grid.scene.getObject(collPairs.get(i)[0]);
            ObjectProperties o2 = grid.scene.getObject(collPairs.get(i)[1]);
            double coll_time = CollisionTimer.getCollTime(o1, o2, grid.coll_times.get(i)[0], grid.coll_times.get(i)[1]);
            if (-1 != coll_time)
            {
                if (o1 instanceof Circle && o2 instanceof Circle) {
                    PhysicsCalcer.calcCicles((Circle) o1, (Circle) o2, coll_time);
                } else if(o1 instanceof Circle && o2 instanceof Polygon) {
                    PhysicsCalcer.calcCirclePolygon((Circle) o1, (Polygon) o2, coll_time);
                } else if(o1 instanceof Polygon && o2 instanceof Circle) {
                    PhysicsCalcer.calcCirclePolygon((Circle) o2, (Polygon) o1, coll_time);
                } else if(o1 instanceof Polygon && o2 instanceof Polygon) {
                    PhysicsCalcer.calcPolygons((Polygon) o2, (Polygon) o1, coll_time);
                }
            }
            // }
        }
    }
}
