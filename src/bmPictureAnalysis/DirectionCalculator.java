package bmPictureAnalysis;

import bmDataTypes.DT_Point;

public class DirectionCalculator implements IF_DirectionCalculator {
	
	private static DirectionCalculator instance;
	
	public static IF_DirectionCalculator getInstance(){
		if(instance == null){
			instance = new DirectionCalculator();
		}
		return instance;
	}
	
	@Override
	public DT_Point calculatePointFromCurrentPointUsingDirection(int x, int y, EN_direction direction) {
		int newX; 
		int newY;
		switch (direction) {
		case North: 
					newX = x;
					newY = y - 1;
					break;
		case NorthEast: 
					newX = x + 1;
					newY = y - 1;
					break;
		case East: 
					newX = x + 1;
					newY = y;
					break;
		case SouthEast: 
					newX = x + 1;
					newY = y + 1;
					break;
		case South: 
					newX = x;
					newY = y + 1;
					break;
		case SouthWest: 
					newX = x - 1;
					newY = y + 1;
					break;
		case West: 
					newX = x - 1;
					newY = y;
					break;
		case NorthWest: 
					newX = x - 1;
					newY = y - 1;
					break;
		default://check with itself
					newX = x;
					newY = y;
					break; 
		}
		return new DT_Point(newX, newY);
	}

}
