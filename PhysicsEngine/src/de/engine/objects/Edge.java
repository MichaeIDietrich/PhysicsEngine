package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Vector;

public class Edge {
	
	Vector origin, end;
	
	public Edge(Vector origin, Vector end) {
		this.origin = origin;
		this.end = end;
	}
	
	private Vector getEdgeVec() {
		Vector vec = new Vector(end.getX() - origin.getX(), end.getY() - origin.getY());
		return vec;
	}
	
	public double getLength() {
		return getEdgeVec().getLength();
	}
	
	public Vector getNormal() {
		return new Rotation(90).getMatrix().multVector(getEdgeVec());
	}
}
