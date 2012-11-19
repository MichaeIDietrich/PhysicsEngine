/*
Copyright (C) 2012 Michael Dietrich, Carsten Krahl, Johannes Hackel

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.engine.environment;

public class EnvProps
{
    
    static EnvProps instance;
    
    private Scene scene;
    
    public double deltaTime;
    
    public double default_gravitational_acceleration = -9.80665; // m/s²
    
    private EnvProps()
    {
        deltaTime = 0.033;
        scene = null;
    }
    
    public static EnvProps getInstance()
    {
        if (instance == null)
            instance = new EnvProps();
        return instance;
    }
    
    public static void setScene(Scene scene)
    {
        getInstance().scene = scene;
    }
    
    public static double deltaTime()
    {
        return getInstance().deltaTime;
    }
    
    public static void deltaTime(double deltaTime)
    {
        getInstance().deltaTime = deltaTime;
    }
    
    public static double grav_acc()
    {
        if (getInstance().scene != null)
            return getInstance().scene.gravitational_acceleration;
        else
            return getInstance().default_gravitational_acceleration;
    }
}