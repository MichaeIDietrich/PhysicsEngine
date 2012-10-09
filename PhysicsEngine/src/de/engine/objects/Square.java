package de.engine.objects;

import de.engine.math.Point;
import de.engine.math.Util;
import de.engine.math.Vector;


public class Square extends Polygon
{
	public Square(Vector position, Point corner)
	{
		super(position);
		points = new Vector[4];
		points[0] = new Vector(corner);
		points[1] = new Vector(corner.x, -1 * corner.y);
		points[2] = new Vector(-1 * corner.x, -1 * corner.y);
		points[3] = new Vector(-1 * corner.x, corner.y);
		radius = Util.distanceToOrigin(corner);
	}
	
	@Override
	public void translation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
