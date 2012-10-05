package de.engine.objects;

import de.engine.math.Vector;


public class Circle extends ObjectProperties
{
	private double radius;
	
	
	public Circle( Vector position, int radius ) {
		
		this.id++;
		this.position = position;
		this.radius   = radius;
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
