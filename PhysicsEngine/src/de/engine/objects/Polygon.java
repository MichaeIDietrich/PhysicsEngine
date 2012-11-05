package de.engine.objects;

import de.engine.environment.EnvProps;
import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Util;
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
	
	public Vector getWorldPointPos(int i, double time) {
	    Transformation tr = new Transformation(Util.add(world_position.translation, Util.scale(velocity, time)), new Rotation(world_position.rotation.getAngle() + angular_momentum * time));
        return tr.getPostion(points[i]);
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
    
    @Override
    public Vector[] getAABB(double time) {
        Vector aabb[] = new Vector[2];
        aabb[0] = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        aabb[1] = new Vector(Double.MIN_VALUE, Double.MIN_VALUE);
        for (int i = 0; i < points.length; i++)
        {
            Vector v = getWorldPointPos(i, time);
            if(v.getX() > aabb[1].getX()) {
                aabb[1].setX(v.getX());
            } else if(v.getX() < aabb[0].getX()) {
                aabb[0].setX(v.getX());
            }
            if(v.getY() > aabb[1].getY()) {
                aabb[1].setY(v.getY());
            } else if(v.getY() < aabb[0].getY()) {
                aabb[0].setY(v.getY());
            }
        }
        return aabb;
    }

    @Override
    public Vector[] getNextAABB() {
        Vector aabb[] = new Vector[2];
        aabb[0] = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        aabb[1] = new Vector(Double.MIN_VALUE, Double.MIN_VALUE);
        for (int i = 0; i < points.length; i++)
        {
            Vector v = getWorldPointPos(i, EnvProps.deltaTime());
            if(v.getX() > aabb[1].getX()) {
                aabb[1].setX(v.getX());
            } else if(v.getX() < aabb[0].getX()) {
                aabb[0].setX(v.getX());
            }
            if(v.getY() > aabb[1].getY()) {
                aabb[1].setY(v.getY());
            } else if(v.getY() < aabb[0].getY()) {
                aabb[0].setY(v.getY());
            }
        }
        return aabb;
    }

}
