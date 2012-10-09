package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Vector;

public class Polygon extends ObjectProperties{

	public Edge[] edges;
	
	protected Polygon(Vector position) {
		this.world_position = new Transformation(position, new Rotation(0));
		radius = 0;
	}
	
	public Polygon(Vector position, Edge[] edges) {
		this(position);
		this.edges = edges;
		calcRadius();
	}
	
	protected void calcRadius() {
		radius = 0;
		for (Edge edge : edges) {
			double corner_dist = edge.origin.getLength();
			radius = (corner_dist > radius) ? corner_dist : radius;
		}
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

}
