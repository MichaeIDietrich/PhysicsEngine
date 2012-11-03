package de.engine.objects;

import de.engine.math.Util;
import de.engine.math.Vector;

public class Square extends Polygon {
	
	public Square(Vector position, Vector corner) {
		super(position);
		this.points = new Vector[4];
		this.points[0] = corner;
		this.points[1] = new Vector( corner.getX(), -corner.getY());
		this.points[2] = new Vector(-corner.getX(), -corner.getY());
		this.points[3] = new Vector(-corner.getX(),  corner.getY());
		this.radius = Util.distanceToOrigin(corner);
		
		this.velocity = new Vector();
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
