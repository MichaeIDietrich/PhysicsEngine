package math;

/**
 * Vector2d
 * 2 -> zweidimenssional
 * d -> double
 *
 */
public class Vector2d {

	private double x, y, length;
	
	public Vector2d() {
		x = 0.0d;
		y = 0.0d;
		length = 0.0d;
	}
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
		
		calcLength();
	}
	
	public void setX(double x) {
		this.x = x;
		calcLength();
	}
	
	public void setY(double y) {
		this.y = y;
		calcLength();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getLength() {
		return length;
	}
	
	private void calcLength() {
		length = Math.sqrt((x * x) + (y * y));
	}
	
	public Vector2d add(Vector2d v) {
		return new Vector2d(this.x + v.getX(), this.y + v.getY());
	}
	
	public Vector2d add(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}

	public void addVector(Vector2d v) {
		this.x =+ v.getX();
		this.y =+ v.getY();
		calcLength();
	}
	
	public Vector2d subtract(Vector2d v) {
		return new Vector2d(this.x - v.getX(), this.y - v.getY());
	}
	
	public Vector2d subtract(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}
	
	public void subtractVector(Vector2d v) {
		this.x =- v.getX();
		this.y =- v.getY();
		calcLength();
	}
}
