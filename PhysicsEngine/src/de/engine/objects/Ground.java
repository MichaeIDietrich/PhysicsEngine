package de.engine.objects;


public class Ground extends ObjectProperties
{
	int watermark;
	
	public Ground( int watermark ) 
	{
		this.id++;
		this.watermark = watermark;
	}
	
	@Override
	public void translation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
}
