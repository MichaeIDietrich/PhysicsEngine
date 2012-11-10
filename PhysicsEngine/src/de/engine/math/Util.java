package de.engine.math;

import de.engine.objects.Circle;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engine.objects.Polygon;
import de.engine.objects.Square;

public class Util 
{
    // needed for derivation
    private static final Double h = Math.pow( 10d, 10d );
    private static Vector function = new Vector();
    private static double m = 0;
    private static double n = 0;
    private static Double[] radius = new Double[3];
    
    
	public static double distance(Vector p1, Vector p2) 
	{
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return Math.hypot(x, y);
	}

	public static double distanceToOrigin(Vector p) 
	{
		return Math.hypot(p.getX(), p.getY());
	}
	
	public static Vector add(Vector vec1, Vector vec2) 
	{
		return new Vector(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
	}

	public static Vector minus(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
	}
	
	public static Vector scale(Vector v, double s)
    {
	    return new Vector(v.getX() * s,  v.getY() * s);
    }
	
	public static double scalarProduct(Vector vec1, Vector vec2) 
	{
		return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
	}
	
    public static double getAngle( Vector vec1, Vector vec2 )
    {
        return Math.atan2( vec2.getY()-vec1.getY(), vec2.getX()-vec1.getX() );
    }
    
    
    public static Double newtonIteration( ObjectProperties object, Ground ground )
    {
        // If the object has no velocity in x direction, return the objects coordinate.
        if (object.velocity.getX()==0) return object.getPosition().getX();
        
        Double xn = object.getPosition().getX();
        
        radius[0] = -object.getRadius();
        radius[1] = 0d;
        radius[2] = +object.getRadius();

        Double distance = Double.MAX_VALUE;
        Double old_distance;
        
        Double result = 0d;
        
        for(int n=0; n < 3; n++) 
        {
            old_distance = distance;
            
            for(int i=0; i < 20; i++) 
            {
                xn = xn - getFunctionsValue( xn, radius[n], object, ground) / derive1D( xn, radius[n], object, ground);
            }
            
            int y    = ground.function( ground.ACTUAL_FUNCTION, xn.intValue());
            distance = Math.sqrt( Math.pow(xn-object.getPosition().getX(), 2d) + Math.pow(y-object.getPosition().getY(), 2d));
            
            // returns the intersection coordinate with the shortest distance between circle an ground
            if (distance < old_distance) result = xn;
        }
        return result;
    }
    
    /**
     * Calculates the 1st derivation of the function given in <i>getFunctionsValue</i>.
     * @param x - determines the point of which the derivation is wanted
     * @param object - 
     * @param ground
     * @return
     */
    public static Double derive1D( Double x, Double radius, ObjectProperties object, Ground ground )
    {
        // df(x) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
        return (getFunctionsValue( x+h, radius, object, ground) - getFunctionsValue( x-h, radius, object, ground)) * 2d/h;
    }
    
    
    public static Double getFunctionsValue( Double x, Double radius, ObjectProperties object, Ground ground ) 
    {
        // m = slope, n = shift in y, linear function 
        m = object.velocity.getY()/object.velocity.getX();
        n = object.getPosition().getY();

        // for calculating a pair of tangents right and left beside the main vector of the sphere
        double alpha = Math.atan( m );
        double diff_x =  radius * Math.sin( alpha );
        double diff_y = -radius * Math.cos( alpha );
        
        function.set(   0,           m * (x-object.getPosition().getX()-diff_x) + n + diff_y); 
        function.set(   1,  (double) ground.function( ground.ACTUAL_FUNCTION, x.intValue()) );
        
        return function.get(1) - function.get(0);            
    }
    
    public static Vector[] getAxis(Circle c, Polygon p, double time) {
        Vector[] axis;
        int firstaxis = 0;
        if(p instanceof Square) {
            axis = new Vector[2 + p.points.length];
            axis[0] = Util.minus(p.getWorldPointPos(1, time), p.getWorldPointPos(0, time)).getNormalVector().getUnitVector();
            axis[1] = Util.minus(p.getWorldPointPos(2, time), p.getWorldPointPos(1, time)).getNormalVector().getUnitVector();
            firstaxis = 2;
        } else {
            axis = new Vector[p.points.length * 2];
            for (int i = 0; i < p.points.length; i++)
            {
                int j = (i == p.points.length - 1) ? 0 : i + 1;
                axis[i] = Util.minus(p.getWorldPointPos(j, time), p.getWorldPointPos(i, time)).getNormalVector().getUnitVector();
                firstaxis = i + 1;
            }
        }
        for (int i = 0; i < p.points.length; i++)
        {
            axis[firstaxis + i] = Util.minus(p.getWorldPointPos(i, time), c.getPosition(time)).getUnitVector();
        }
        return axis;
    }
    
    public static Vector[] getAxis(Polygon p1, Polygon p2, double time) {
        Vector[] axis;
        Vector[] a_p1 = getAxis(p1, time);
        Vector[] a_p2 = getAxis(p2, time);
        axis = new Vector[a_p1.length + a_p2.length];
        for (int i = 0; i < axis.length; i++)
        {
            if(i < a_p1.length)
                axis[i] = a_p1[i];
            else {
                axis[i] = a_p2[i - a_p1.length];
            }
        }
        return axis;
    }
    
    private static Vector[] getAxis(Polygon p, double time) {
        Vector[] axis;
        if(p instanceof Square) {
            axis = new Vector[2 + p.points.length];
            axis[0] = Util.minus(p.getWorldPointPos(0, time), p.getWorldPointPos(1, time)).getNormalVector().getUnitVector();
            axis[1] = Util.minus(p.getWorldPointPos(1, time), p.getWorldPointPos(2, time)).getNormalVector().getUnitVector();
        } else {
            axis = new Vector[p.points.length * 2];
            for (int i = 0; i < p.points.length; i++)
            {
                int j = (i == p.points.length - 1) ? 0 : i + 1;
                axis[i] = Util.minus(p.getWorldPointPos(i, time), p.getWorldPointPos(j, time)).getNormalVector().getUnitVector();
            }
        }
        return axis;
    }
}
