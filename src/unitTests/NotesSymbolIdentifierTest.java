package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import bmDataTypes.*;
import bmPictureAnalysis.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class NotesSymbolIdentifierTest {
		
	@Test
	public void testGetInstance() {
		IF_SymbolIdentifier identifier = NotesSymbolIdentifier.getInstance(null);
		if(identifier == null){
			fail("Instance was not created");
		}
	}

	@Test
	public void testGetListOfPointsFound_learnMode(){ 
		IF_SymbolIdentifier identifier = NotesSymbolIdentifier.getInstance(null);
		((NotesSymbolIdentifier) identifier).setLearnMode(true);
		try {
			Field field = NotesSymbolIdentifier.class.getDeclaredField("listOfPointsFound_learnMode");
			field.setAccessible(true);
			
			ArrayList<DT_Point> testPoints = new ArrayList<DT_Point>();
			testPoints.add(new DT_Point(1, -1));
			testPoints.add(new DT_Point(2, 0));
			testPoints.add(new DT_Point(3, -1));
			field.set(identifier, testPoints);
			
			ArrayList<DT_Point> resultPoints = ((NotesSymbolIdentifier) identifier).getListOfPointsFound_learnMode();
			
			if(resultPoints.size() != testPoints.size()){
				fail("Different number of points retrieved");
			}
			
			for(int i=0; i<resultPoints.size(); i++){
				if(resultPoints.get(i).equals(testPoints.get(i)) == false){
					fail("The list of Points is not equal");
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error with reflection: "+e.getMessage());
		}
	}

	@Test
	public void testGetAvgXofFoundSymbol_learnMode() {
		IF_SymbolIdentifier identifier = NotesSymbolIdentifier.getInstance(null);
		((NotesSymbolIdentifier) identifier).setLearnMode(true);
		
		Field field;
		try {
			field = NotesSymbolIdentifier.class.getDeclaredField("avgXofFoundSymbol_learnMode");
			field.setAccessible(true);
			double testAvgX = 0.76471;
			field.set(identifier, testAvgX);
			double resultAvgX = ((NotesSymbolIdentifier) identifier).getAvgXofFoundSymbol_learnMode();
			
			if(resultAvgX != testAvgX){
				fail("Value of returned X average was not expected");
			}
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error with reflection: "+e.getMessage());
		}
	}

	@Test
	public void testGetAvgYofFoundSymbol_learnMode() {
		IF_SymbolIdentifier identifier = NotesSymbolIdentifier.getInstance(null);
		((NotesSymbolIdentifier) identifier).setLearnMode(true);
		
		Field field;
		try {
			field = NotesSymbolIdentifier.class.getDeclaredField("avgYofFoundSymbol_learnMode");
			field.setAccessible(true);
			double testAvgY = 0.76471;
			field.set(identifier, testAvgY);
			double resultAvgY = ((NotesSymbolIdentifier) identifier).getAvgYofFoundSymbol_learnMode();
			
			if(resultAvgY != testAvgY){
				fail("Value of returned Y average was not expected");
			}
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error with reflection: "+e.getMessage());
		}
	}

	@Test
	public void testIdentifySymbol() {
		ArrayList<DT_SymbolSimplified> testSymbols = new ArrayList<DT_SymbolSimplified>();
		testSymbols.add(new DT_SymbolSimplified(EN_MusicalSymbolName.Fuenf, -0.23529411764, -0.588235294));
		
		IF_SymbolIdentifier identifier = NotesSymbolIdentifier.getInstance(testSymbols);
		DT_MusicalSymbol testSymbol = new DT_MusicalSymbol(EN_MusicalSymbolName.Fuenf);
		
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("./src/unitTests/testResources/test7x11_simpleFive_N0-23529411764705882_N0-5882352941176471.jpg"));
		}catch(IOException e){
			fail("Reading of test image failed: "+e.getMessage());
		}
		
		DT_MusicalSymbol resultSymbol = identifier.identifySymbol(img, 1, 1);
		
		if(resultSymbol == null){
			fail("There was no MusicalSymbol identified");
		}
		
		if(resultSymbol.getName().compareTo(testSymbol.getName()) != 0){
			fail("The returned MusicalSymbol does not match the expected");
		}
		
	}

}
