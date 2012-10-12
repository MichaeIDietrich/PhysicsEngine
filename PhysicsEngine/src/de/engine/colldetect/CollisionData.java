package de.engine.colldetect;

import de.engine.math.Vector;

public class CollisionData {
	
	Contact contacts[];
	
	
	public class Contact {
		Vector point;
		Vector normal;
		double penetration;
	}
}
