package de.engine.environment;

public class Timer {

	private static Timer instance;
	
	public double deltaTime;
	
	private Timer() {
		deltaTime = 0.1;
	}
	
	public static Timer getTimer() {
		if(null == instance)
			instance = new Timer();
		return instance;
	}
}
