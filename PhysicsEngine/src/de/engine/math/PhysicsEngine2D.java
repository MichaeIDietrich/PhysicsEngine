package de.engine.math;

import de.engine.colldetect.CollisionDetector;
import de.engine.environment.EnvProps;
import de.engine.environment.Scene;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;

public class PhysicsEngine2D {
    
	private Scene scene;

	CollisionDetector collDetector;

	public PhysicsEngine2D() {
		this.collDetector = new CollisionDetector(scene);
		//
	}

	public void Rotation() {
		//
	}

	public void Translation() {
		//
	}

	public void setScene(Scene scene) {
		this.scene = scene;
		this.collDetector = new CollisionDetector(scene);
	}

	// here starts the entry point for all the physical calculation
	public void calculateNextFrame(double deltaTime) {
		EnvProps.deltaTime(deltaTime);
		double oldposition = 0;

		collDetector.checkScene();
		
		for (ObjectProperties obj : scene.getObjects()) {
			if (obj instanceof Circle) {
				// Collision detection
				
				obj.update();
				//oldposition = obj.getPosition().getY();
				//obj.world_position.translation = obj.getNextPosition();
				//obj.getPosition().setY(
					//	-9.81 / 2d * deltaTime + obj.velocity.getY()
						//		* deltaTime + obj.getPosition().getY());

				//obj.velocity.setY((obj.getPosition().getY() - oldposition) / deltaTime);
			}
		}
	}
}
