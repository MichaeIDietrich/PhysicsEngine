package de.engine.physics;

import de.engine.environment.EnvironmentProperties;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;

public class PhysicsCalcer {

	public static void calcCicles(Circle c1, Circle c2, double collTime) {
		Vector dist = Util.minus(c2.getPosition(), c1.getPosition());
		
		double alpha = Util.scalarProduct(c1.velocity, dist) / Util.scalarProduct(dist, dist);
		Vector c1_vp = dist.scale(alpha);
		Vector c1_vs = Util.minus(c1.velocity, c1_vp);

		double beta = Util.scalarProduct(c2.velocity, dist) / Util.scalarProduct(dist, dist);
		Vector c2_vp = dist.scale(beta);
		Vector c2_vs = Util.minus(c2.velocity, c2_vp);
		
		double afterCollTime = EnvironmentProperties.getInstance().deltaTime - collTime;
		
		c1.update(collTime);
		c2.update(collTime);
		if(c1.mass == c2.mass) {
			c1.velocity = Util.add(c1_vs, c2_vp);
			c2.velocity = Util.add(c2_vs, c1_vp);
		} else {
			Vector u = Util.add(c1_vp.scale(c1.mass),
					c2_vp.scale(c2.mass)).scale(2.0 / (c1.mass + c2.mass));
			c1.velocity = Util.minus(u, c1_vs);
			c2.velocity = Util.minus(u, c2_vs);
		}
		c1.update(afterCollTime);
		c2.update(afterCollTime);
	}
}