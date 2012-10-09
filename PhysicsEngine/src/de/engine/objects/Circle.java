package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Vector;


public class Circle extends ObjectProperties
{	
	
	public Circle( Vector position, int radius ) {
		
		this.id++;
		this.world_position = new Transformation(position, new Rotation(0));
		this.radius = radius;
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
