package de.engine.math;

public class Transformation {
    
    public static class Rotation {
        
        private double angle, cos, sin;

        public Rotation(double angle) {
            setAngle(angle);
        }
        
        public double getAngle() {
            return angle;
        }
        
        public void setAngle(double angle) {
            this.angle = angle;
            cos = Math.cos(angle);
            sin = Math.sin(angle);
        }
        
        public Matrix getMatrix() {
            return new Matrix(cos, -1 * sin, sin, cos);
        }
    }
	
	public Vector translation;
	public Rotation rotation;
	
	public Transformation(Vector trans, double angle) {
        translation = trans;
        rotation = new Rotation(angle);
    }
	
	public Transformation(Vector trans, Rotation rot) {
		translation = trans;
		rotation = rot;
	}
	
	public Vector getPostion(Vector vec) {
		Vector rot = rotation.getMatrix().multVector(vec);
		return new Vector(rot.getX() + translation.getX(),
				rot.getY() + translation.getY());
	}
}
