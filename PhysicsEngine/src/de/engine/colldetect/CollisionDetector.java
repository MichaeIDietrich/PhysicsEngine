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
    private Scene scene;
    
    public CollisionDetector(Scene scene)
    {
        grid = new Grid(scene);
        this.scene = scene;
    }
    
    public void checkScene()
    {
        grid.scanScene();
        Vector<Integer[]> collPairs = grid.getCollisionPairs();
        for (int i = 0; i < collPairs.size(); i++)
        {
            ObjectProperties o1 = grid.scene.getObject(collPairs.get(i)[0]);
            ObjectProperties o2 = grid.scene.getObject(collPairs.get(i)[1]);
            double coll_time = CollisionTimer.getCollTime(o1, o2, grid.coll_times.get(i)[0], grid.coll_times.get(i)[1]);
            if (-1 != coll_time)
            {
                if (o1 instanceof Circle && o2 instanceof Circle)
                {
                    PhysicsCalcer.calcCircles((Circle) o1, (Circle) o2, coll_time);
                }
                else if (o1 instanceof Circle && o2 instanceof Polygon)
                {
                    PhysicsCalcer.calcCirclePolygon((Circle) o1, (Polygon) o2, coll_time);
                }
                else if (o1 instanceof Polygon && o2 instanceof Circle)
                {
                    PhysicsCalcer.calcCirclePolygon((Circle) o2, (Polygon) o1, coll_time);
                }
                else if (o1 instanceof Polygon && o2 instanceof Polygon)
                {
                    PhysicsCalcer.calcPolygons((Polygon) o2, (Polygon) o1, coll_time);
                }
            }
        }
        
        objectGroundCollision();
    }
    
    
    public void objectGroundCollision() 
    {
        if (scene.getCount()>0 && scene.getObject(0)!=null && scene.getGround()!=null)
        {
            de.engine.math.Vector v = null;
            
            long time = System.currentTimeMillis();

            v = Util.solveNonLEQ( scene.getObject(0), scene.getGround() );
            scene.getObject(0).last_intersection = v.getX();
            
            System.out.println( System.currentTimeMillis() - time );
            
            System.out.println( "Schnittpunkt = "+ v.get(0).intValue());
        }
        
    }
}
