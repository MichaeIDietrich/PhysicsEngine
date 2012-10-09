package de.engine.objects;

import de.engine.math.Point;
import de.engine.math.Util;
import de.engine.math.Vector;


public class Square extends Polygon
{
	public Square(Vector position, Point corner)
	{
		super(position);
		edges = new Edge[4];
		edges[0] = new Edge(new Vector(corner), new Vector(corner.x, -1 * corner.y));
		edges[1] = new Edge(new Vector(corner.x, -1 * corner.y), new Vector(-1 * corner.x, -1 * corner.y));
		edges[2] = new Edge(new Vector(-1 * corner.x, -1 * corner.y), new Vector(-1 * corner.x, corner.y));
		edges[3] = new Edge(new Vector(-1 * corner.x, corner.y), new Vector(corner));
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
