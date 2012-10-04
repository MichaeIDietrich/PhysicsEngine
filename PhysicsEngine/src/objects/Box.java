package objects;

import math.Vector2d;

public class Box extends BaseObject{

	private Vector2d length_width;
	
	public Box() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getVolumne() {
		return 4 * length_width.getX() * length_width.getY();
	}

}
