package de.engine.colldetect;

import de.engine.math.Util;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;

public class CollisionTimer {
	
	public static double getCirclesCollTime(Circle c1, Circle c2) {
		double distance = Util.minus(c1.getPosition(), c2.getPosition()).getLength();
		double min_distance = c1.getRadius() + c2.getRadius();
		double velocity = Util.minus(c1.velocity, c2.velocity).getLength();
		double coll_time = (distance - min_distance) / velocity;
		if((min_distance + 2) < Util.minus(c1.getPosition(coll_time), c2.getPosition(coll_time)).getLength())
		    return -1;
		else
		    return coll_time;
	}
	
	public static IntersectTimeInfo getSweepTimeInfo(ObjectProperties o1, ObjectProperties o2) {
	    IntersectTimeInfo iti = new IntersectTimeInfo();
        double distance = Util.minus(o1.getPosition(), o2.getPosition()).getLength();
        double min_distance = o1.getRadius() + o2.getRadius();
        double velocity = Util.minus(o1.velocity, o2.velocity).getLength();
        iti.in = (distance - min_distance) / velocity;
        iti.mid = distance / velocity;
        return iti;
	}
	
	public static class IntersectTimeInfo {
        double in, mid;
	}
}
