package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Vector;

public class Polygon extends ObjectProperties{

	public Vector[] points;
	
	public Polygon(Vector position) {
		this.world_position = new Transformation(position, new Rotation(0));
		radius = 0;
	}

	public Polygon(Vector position, double rotation) {
		this.world_position = new Transformation(position, new Rotation(rotation));
		radius = 0;
	}
	
	public Polygon(Vector position, Vector[] points) {
		this(position);
		this.points = points;
		calcRadius();
	}
	
	protected void calcRadius() {
		radius = 0;
		for (Vector point : points) {
			double corner_dist = point.getLength();
			radius = (corner_dist > radius) ? corner_dist : radius;
		}
	}
	
	public Vector getWorldPointPos(int i) {
		return world_position.getPostion(points[i]);
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

	@Override
	public double getRadius() {
		return radius;
	}
	
	@Override
	public void setRadius(double radius) {
	}

	@Override
	public ObjectProperties copy() {
		// TODO
		Polygon newPolygon = new Polygon(getPosition());
		return newPolygon;
	}

	
    @Override
    public boolean contains(double x, double y)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
