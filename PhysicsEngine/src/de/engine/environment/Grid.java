package de.engine.environment;

import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;

public class Grid {
	//orgin is (0,0)
	//Vector origin;
	Vector cellSize;
	
	java.util.Vector<GridField> usedFields;
	
	public Grid(Vector cellSize) {
		//origin = new Vector(0, 0);
		this.cellSize = cellSize;
		usedFields = new java.util.Vector<GridField>();
	}
	
	public void insertObject(ObjectProperties ob, int index) {
		Vector ob_pos = ob.getWorldTranslation();
		int minx, miny;
		int maxx, maxy;
		int[][] cell = new int[2][4];
		cell[0] = getField(Util.add(ob_pos, new Vector(ob.getRadius(), 0)));
		cell[1] = getField(Util.add(ob_pos, new Vector(-1 * ob.getRadius(), 0)));
		cell[2] = getField(Util.add(ob_pos, new Vector(0, ob.getRadius())));
		cell[3] = getField(Util.add(ob_pos, new Vector(0, -1 * ob.getRadius())));
		
		minx = cell[0][0];
		miny = cell[1][0];
		maxx = cell[0][0];
		maxy = cell[1][0];
		
		for (int i = 1; i < 4; i++) {
			if(cell[0][i] < minx)
				minx = cell[0][i];
			else if (cell[0][i] > maxx)
				maxx = cell[0][i];		
			if(cell[1][i] < miny)
				miny = cell[1][i];
			else if (cell[0][i] > maxy)
				maxy = cell[1][i];
		}
		
		for(int i=minx; i <=maxx; i++ ) {
			
		}
	}
	
	private int[] getField(Vector pos) {
		int[] cell = new int[2]; 
		cell[0] = (int) Math.ceil(pos.getX() / cellSize.getX()) + 1;
		cell[1] = (int) Math.ceil(pos.getY() / cellSize.getY()) + 1;
		return cell;
	}
}