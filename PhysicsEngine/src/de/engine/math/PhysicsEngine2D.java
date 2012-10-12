package de.engine.math;

import de.engine.colldetect.CollisionDetector;
import de.engine.environment.Scene;
import de.engine.environment.Timer;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;

public class PhysicsEngine2D implements Runnable {
	private Scene scene;
	public boolean semaphore = true;

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

	@Override
	public void run() {
		// Will be changed soon!
		while (true) {
			System.out.println("running...");

			while (semaphore) {
				try {
					// do a break for 1/30 second
					Thread.sleep(33);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Checks collision between circles and ground
				if (scene.getGround() != null) {
					// System.out.println( scene.getGround().function(
					// scene.getGround().DOWNHILL, e.getLocation().x ) +" | "+
					// e.getLocation().y );
				}
			}

			try {
				// do another break for 1/30 second
				Thread.sleep(33);
			}

			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// here starts the entry point for all the physical calculation
	public void calculateNextFrame(double deltaTime) {
		Timer.getTimer().deltaTime = deltaTime;
		double oldposition = 0;

		collDetector.checkScene();
		
		for (ObjectProperties obj : scene.getObjects()) {
			if (obj instanceof Circle) {
				// Collision detection

				oldposition = obj.getPosition().getY();
				obj.world_position.translation = obj.getNextPosition();
				//obj.getPosition().setY(
					//	-9.81 / 2d * deltaTime + obj.velocity.getY()
						//		* deltaTime + obj.getPosition().getY());

				obj.velocity.setY((obj.getPosition().getY() - oldposition)
						/ deltaTime);
			}
		}
	}
}
