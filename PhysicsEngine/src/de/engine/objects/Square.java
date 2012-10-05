package de.engine.objects;

import de.engine.math.Point;
import de.engine.math.Util;

public class Square extends PhysicalProperties
{
	private Point corner;

	public Square(Point position)
	{
		this.position = position;
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
		return Util.distanceToOrigin(corner);
	}

}
