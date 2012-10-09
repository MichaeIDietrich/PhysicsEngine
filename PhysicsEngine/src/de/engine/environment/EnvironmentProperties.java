package de.engine.environment;

import java.util.ArrayList;

import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public abstract class EnvironmentProperties
{
    public double gravitational_acceleration = 9.80665; // m/s²
    
    // ground is unique thats why it has it's own property
    protected Ground ground;
    
    protected ArrayList<ObjectProperties> objects;
}
