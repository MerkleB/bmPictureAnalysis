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

		int[] difference = getDifferenceRGB(valueArray);

		if(difference[0] > conRGBThreshold && difference[1] > conRGBThreshold && difference[2] > conRGBThreshold){
			return true;
		}else{
			return false;
		}
	}
	
	/*private boolean atLeastTwoDifferOverThreshold(int red, int green, int blue){
		boolean differenceDtected = false;
		
		if(
			red > conRGBThreshold && green > conRGBThreshold && blue > conRGBThreshold //all three differ
			|| (red > conRGBThreshold && green > conRGBThreshold)//red and green differ
			|| (red > conRGBThreshold && blue > conRGBThreshold)//red and blue differ
			|| (green > conRGBThreshold && blue > conRGBThreshold)//green and blue differ
		  )
		{
			differenceDtected = true;
		}
		
		return differenceDtected;
	}*/

	@Override
	public boolean pixelColorIsEqual(BufferedImage image, int x, int y, EN_direction direction) {
		int[] valueArray = this.getColorValues(image, x, y, direction);
		
		int[] difference = getDifferenceRGB(valueArray);
		
		if(difference[0] < conRGBThreshold && difference[1] < conRGBThreshold && difference[2] < conRGBThreshold){
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
	
	private int[] getDifferenceRGB(int[] valueArray){
		int[] resultArray = new int[3];
		
		Color colorOfCurrentPoint = new Color(valueArray[0]);
		Color colorOfNewPoint = new Color(valueArray[1]);
		
		int differenceRed = colorOfCurrentPoint.getRed() - colorOfNewPoint.getRed();
		int differenceGreen = colorOfCurrentPoint.getGreen() - colorOfNewPoint.getGreen();
		int differenceBlue = colorOfCurrentPoint.getBlue() - colorOfNewPoint.getBlue();
		
		if(differenceRed < 0) differenceRed = differenceRed * (-1);
		if(differenceGreen < 0) differenceGreen = differenceGreen * (-1);
		if(differenceBlue < 0) differenceBlue = differenceBlue * (-1);
		
		resultArray[0] = differenceRed;
		resultArray[1] = differenceGreen;
		resultArray[2] = differenceBlue;
		
		return resultArray;
	}

}
