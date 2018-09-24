package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import bmDataTypes.DT_Point;
import bmPictureAnalysis.DirectionCalculator;
import bmPictureAnalysis.EN_direction;
import bmPictureAnalysis.IF_DirectionCalculator;

public class DirectionCalculatorTest {

	@Test
	public void testGetInstance() {
		IF_DirectionCalculator dir_calculator = DirectionCalculator.getInstance();
		
		if(dir_calculator == null){
			fail("No instance was retrieved (calculator)");
		}
		
		IF_DirectionCalculator dir_calculator2 = DirectionCalculator.getInstance();
		
		if(dir_calculator2 == null){
			fail("No instance was retrieved (calculator2)");
		}
		
		if(dir_calculator != dir_calculator2){
			fail("Two instances where retrieved but singleton should retrieve only one instance");
		}
	}

	@Test
	public void testCalculatePointFromCurrentPointUsingDirection() {
		IF_DirectionCalculator dir_calculator = DirectionCalculator.getInstance();
		
		DT_Point sourcePoint = new DT_Point(10, 10);
		DT_Point targetPoint = new DT_Point(10, 9);
		EN_direction testDirection = EN_direction.North;
		
		DT_Point resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(11, 9);
		testDirection = EN_direction.NorthEast;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(11, 10);
		testDirection = EN_direction.East;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(11, 11);
		testDirection = EN_direction.SouthEast;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(10, 11);
		testDirection = EN_direction.South;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(9, 11);
		testDirection = EN_direction.SouthWest;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(9, 10);
		testDirection = EN_direction.West;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
		
		targetPoint = new DT_Point(9, 9);
		testDirection = EN_direction.NorthWest;
		
		resultPoint = dir_calculator.calculatePointFromCurrentPointUsingDirection(sourcePoint.getX(), sourcePoint.getY(), testDirection);
		if(targetPoint.equals(resultPoint) == false){
			fail("The result of move with direction "+testDirection.name()+" is wrong!");
		}
	}

}
