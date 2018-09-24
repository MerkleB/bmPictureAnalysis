package bmPictureAnalysis;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bmDataTypes.*;

public class NotesSymbolIdentifier implements IF_SymbolIdentifier {

	private static final double upperInstabilityFactor = 1.1;
	private static final double underInstabilityFactor = 0.9;
	private ArrayList<DT_SymbolSimplified> simplifiedSymbolList;
	private IF_PixelAnalyzer pixelAnalyzer;
	private IF_DirectionCalculator directionCalculator;
	private EN_direction[] orderedDirections = {EN_direction.East
			,EN_direction.SouthEast
			,EN_direction.South
			,EN_direction.SouthWest
			,EN_direction.West
			,EN_direction.NorthWest
			,EN_direction.North
			,EN_direction.NorthEast};
	private boolean learnMode = false;
	private ArrayList<DT_Point> listOfPointsFound_learnMode = null;
	private double avgXofFoundSymbol_learnMode = 0;
	private double avgYofFoundSymbol_learnMode = 0;
	
	public static IF_SymbolIdentifier getInstance(ArrayList<DT_SymbolSimplified> simpleSymbols){
		NotesSymbolIdentifier instance = new NotesSymbolIdentifier();
		instance.simplifiedSymbolList = simpleSymbols;
		instance.pixelAnalyzer = PixelAnalyzer.getInstance();
		instance.directionCalculator = DirectionCalculator.getInstance();
		return instance;
	}
	
	public void setLearnMode(boolean useInLearnMode){
		this.learnMode = useInLearnMode;
	}
	
	public ArrayList<DT_Point> getListOfPointsFound_learnMode() {
		return listOfPointsFound_learnMode;
	}

	public double getAvgXofFoundSymbol_learnMode() {
		return avgXofFoundSymbol_learnMode;
	}

	public double getAvgYofFoundSymbol_learnMode() {
		return avgYofFoundSymbol_learnMode;
	}
	
	@Override
	public DT_MusicalSymbol identifySymbol(BufferedImage image, int x, int y) {
		DT_PointCloud points = findPointsWhichBelongToSymbol(image, x, y);
		double[] averageXandY = calculateAverageXandY(points);
		
		DT_SymbolSimplified matchingSymbol = findMatchingSymbol(averageXandY[0], averageXandY[1]);
		DT_MusicalSymbol identifiedMusicalSymbol;
		
		if(matchingSymbol == null){
			identifiedMusicalSymbol = null;
		}else{
			identifiedMusicalSymbol = new DT_MusicalSymbol(matchingSymbol.getName());
		}
		if(learnMode){
			this.listOfPointsFound_learnMode = points.getListOfPoints();
			this.avgXofFoundSymbol_learnMode = averageXandY[0];
			this.avgYofFoundSymbol_learnMode = averageXandY[1];
		}
		
		return identifiedMusicalSymbol;
	}
	
	private DT_PointCloud findPointsWhichBelongToSymbol(BufferedImage image, int x, int y){
		ArrayList<DT_Point> listOfPointWhichBelongToSymbol = new ArrayList<DT_Point>();
		DT_Point firstPoint = new DT_Point(x, y);
		listOfPointWhichBelongToSymbol.add(firstPoint);
		
		boolean pointWasFound = true;
		DT_Point lastPoint = firstPoint;
		DT_Point currentPoint = firstPoint;
		int indexOfWatchPoint = -1;
		int minX = firstPoint.getX(), maxX = firstPoint.getX(), minY = firstPoint.getY(), maxY = firstPoint.getY();
		
		while(pointWasFound){
			lastPoint = currentPoint;
			pointWasFound = false;
			for(int i=0; i<8; i++){//Check each direction as long no point was found
				
				currentPoint = getNeighbourPixelWhichBelongToSymbol(image, lastPoint.getX(), lastPoint.getY(), orderedDirections[i]);
				
				if(currentPoint != null){
					if(checkIfPointInList(currentPoint, listOfPointWhichBelongToSymbol) == false){
						listOfPointWhichBelongToSymbol.add(currentPoint);
						indexOfWatchPoint++;
						pointWasFound = true;
						if(currentPoint.getX() > maxX) maxX = currentPoint.getX();
						if(currentPoint.getY() > maxY) maxY = currentPoint.getY();
						if(currentPoint.getX() < minX) minX = currentPoint.getX();
						if(currentPoint.getY() < minY) minY = currentPoint.getY();
						break;
					}
				}
			}
			if(pointWasFound == false){
				DT_Point watchPoint;
				LoopOverKnownPointsToFindPointsWhichAlsoBelongToTheSymbol:
				for(int j = indexOfWatchPoint; j>0; j--){
					watchPoint = listOfPointWhichBelongToSymbol.get(j);
					for(int k = 7; k>0; k--){
						currentPoint = getNeighbourPixelWhichBelongToSymbol(image, watchPoint.getX(), watchPoint.getY(), orderedDirections[k]);
						if(currentPoint != null){
							if(checkIfPointInList(currentPoint, listOfPointWhichBelongToSymbol) == false){
								listOfPointWhichBelongToSymbol.add(currentPoint);
								indexOfWatchPoint++;
								pointWasFound = true;
								if(currentPoint.getX() > maxX) maxX = currentPoint.getX();
								if(currentPoint.getY() > maxY) maxY = currentPoint.getY();
								if(currentPoint.getX() < minX) minX = currentPoint.getX();
								if(currentPoint.getY() < minY) minY = currentPoint.getY();
								break LoopOverKnownPointsToFindPointsWhichAlsoBelongToTheSymbol;
							}
						}
					}
				}
			}
		}
		DT_PointCloud allPointsWithExtremValues = new DT_PointCloud(listOfPointWhichBelongToSymbol, minX, maxX, minY, maxY);
		return allPointsWithExtremValues;
    }
	
	private DT_Point getNeighbourPixelWhichBelongToSymbol(BufferedImage image, int x, int y, EN_direction direction){
		boolean neighbourFound = pixelAnalyzer.pixelColorIsEqual(image, x, y, direction);
		DT_Point neighbour;
		if(neighbourFound){
			neighbour = directionCalculator.calculatePointFromCurrentPointUsingDirection(x, y, direction);
		}else{
			
			return null;
		}
		return neighbour;
	}
	
	private boolean checkIfPointInList(DT_Point point, ArrayList<DT_Point> checkList){
		boolean result = false;
		for(int i=checkList.size()-1; i>=0; i--){
			if(point.equals(checkList.get(i))){
				result = true;
				return result;
			}
		}
		return result;
	}
	
	private double[] calculateAverageXandY(DT_PointCloud points){
		int middleX, middleY, sumX = 0, sumY = 0, relativeToMiddleX, relativeToMiddleY;
		middleX = (points.getMaxX() + points.getMinX()) / 2;
		
		middleY = (points.getMaxY() + points.getMinY()) / 2;
		for(int i=0; i < points.getListOfPoints().size(); i++){
			relativeToMiddleX = points.getListOfPoints().get(i).getX() - middleX;
			relativeToMiddleY = points.getListOfPoints().get(i).getY() - middleY;
			
			sumX = sumX + relativeToMiddleX;
			sumY = sumY + relativeToMiddleY;
		}
		double[] avgArray = new double[2]; 
		avgArray[0] = ((double) sumX) / points.getListOfPoints().size();
		avgArray[1] = ((double) sumY) / points.getListOfPoints().size();
		
		return avgArray;
	}
	
	private DT_SymbolSimplified findMatchingSymbol(double avgX, double avgY){
		DT_SymbolSimplified foundSymbol = null;
		
		for(int i=0; i<this.simplifiedSymbolList.size(); i++){
			double simpleSymbolPositionX = simplifiedSymbolList.get(i).getPosition()[0];
			double simpleSymbolPositionY = simplifiedSymbolList.get(i).getPosition()[1];
			double underThresholdX, upperThresholdX, underThresholdY, upperThresholdY;
			
			if(simpleSymbolPositionX > 0){
				underThresholdX = simpleSymbolPositionX * underInstabilityFactor;
				upperThresholdX = simpleSymbolPositionX * upperInstabilityFactor;
			}else{
				underThresholdX = simpleSymbolPositionX * upperInstabilityFactor;
				upperThresholdX = simpleSymbolPositionX * underInstabilityFactor;
			}
			
			if(simpleSymbolPositionY > 0){
				underThresholdY = simpleSymbolPositionY * underInstabilityFactor;
				upperThresholdY = simpleSymbolPositionY * upperInstabilityFactor;
			}else{
				underThresholdY = simpleSymbolPositionY * upperInstabilityFactor;
				upperThresholdY = simpleSymbolPositionY * underInstabilityFactor;
			}
			
			if((underThresholdX < avgX && upperThresholdX > avgX) &&(underThresholdY < avgY && upperThresholdY > avgY)){
				foundSymbol = simplifiedSymbolList.get(i);
			}
		}
		return foundSymbol;
	}

}
