package de.engineapp.visual;

import java.util.Collection;


/**
 * Interface to provide methods for decorable objects.
 * 
 * @author Micha
 */
public interface IDecorable
{
    public void putDecor(String key, IDrawable decor);
    public IDrawable getDecor(String key);
    public void removeDecor(String key);
    public Collection<IDrawable> getDecorSet();
}