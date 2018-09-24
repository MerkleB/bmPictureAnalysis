package bmPictureAnalysis;

import java.awt.Color;
import java.awt.image.BufferedImage;

import bmDataTypes.DT_Point;

public class PixelAnalyzer implements IF_PixelAnalyzer {
	
	private static final int conRGBThreshold = 50;
	private static PixelAnalyzer instance;
	
	public static IF_PixelAnalyzer getInstance(){
		if(instance == null){
			instance = new PixelAnalyzer();
		}
		return instance;
	}
	
	@Override
	public boolean pixelColorChanges(BufferedImage image, int x, int y, EN_direction direction) {
		int[] valueArray = this.getColorValues(image, x, y, direction);
		int difference = valueArray[0] - valueArray[1];
		if(difference < 0){
			difference = difference * (-1);
		}
		if(difference > conRGBThreshold){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean pixelColorIsEqual(BufferedImage image, int x, int y, EN_direction direction) {
		int[] valueArray = this.getColorValues(image, x, y, direction);
		Color colorOfCurrentPoint = new Color(valueArray[0]);
		Color colorOfNewPoint = new Color(valueArray[1]);
		
		int differenceRed = colorOfCurrentPoint.getRed() - colorOfNewPoint.getRed();
		int differenceGreen = colorOfCurrentPoint.getGreen() - colorOfNewPoint.getGreen();
		int differenceBlue = colorOfCurrentPoint.getBlue() - colorOfNewPoint.getBlue();
		
		if(differenceRed < 0) differenceRed = differenceRed * (-1);
		if(differenceGreen < 0) differenceGreen = differenceGreen * (-1);
		if(differenceBlue < 0) differenceBlue = differenceBlue * (-1);
		
		if(differenceRed < conRGBThreshold && differenceGreen < conRGBThreshold && differenceBlue < conRGBThreshold){
			return true;
		}else{
			return false;
		}
	}
	
	private int[] getColorValues(BufferedImage image, int x, int y, EN_direction direction){
		int colorValueNow = image.getRGB(x, y);
		
		IF_DirectionCalculator directionCalculator = DirectionCalculator.getInstance();
		
		DT_Point newPoint = directionCalculator.calculatePointFromCurrentPointUsingDirection(x, y, direction);
		
		int colorValueOfNeighbour = image.getRGB(newPoint.getX(), newPoint.getY());
		int[] colorValueArray = {colorValueNow, colorValueOfNeighbour};
		return colorValueArray;
	}

}
