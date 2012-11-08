package de.engine.math;

import de.engine.environment.Scene;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class Util 
{
    // needed for derive
    private static final Double h = Math.pow( 10d, 12d );
    private static Vector function = new Vector();
    private static double m = 0;
    private static double n = 0;
    
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
        
        Double[] radius = new Double[3];
        radius[0] = -object.getRadius();
        radius[1] = 0d;
        radius[2] = +object.getRadius();

        Double distance = Double.MAX_VALUE;
        Double old_distance;
        
        Double result = 0d;
        
        for(int n=0; n < 3; n++) 
        {
            old_distance = distance;
            
            for(int i=0; i < 10; i++) 
            {
                xn = xn - getFunctionsValue( xn, object, ground) / derive1D( xn, object, ground);
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
    public static Double derive1D( Double x, ObjectProperties object, Ground ground )
    {
        // df(x) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
        return (getFunctionsValue( x+h, object, ground) - getFunctionsValue( x-h, object, ground)) * 2d/h;
    }
    
    
    public static Double getFunctionsValue( Double x, ObjectProperties object, Ground ground ) 
    {
        // m = slope, n = shift in y, linear function 
        m = object.velocity.getY()/object.velocity.getX();
        n = object.getPosition().getY();

        // for calculating a pair of tangents right and left beside the main vector of the sphere
        double alpha = Math.atan( m );
        double diff_x =  object.getRadius() * Math.sin( alpha );
        double diff_y = -object.getRadius() * Math.cos( alpha );
        
        function.set(   0,           m * (x-object.getPosition().getX()-diff_x) + n + diff_y); 
        function.set(   1,  (double) ground.function( ground.ACTUAL_FUNCTION, x.intValue()) );
        
        return function.get(1) - function.get(0);            
    }
}
