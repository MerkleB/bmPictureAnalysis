package unitTests;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Test;

import bmDataTypes.DT_MusicalSymbol;
import bmDataTypes.DT_Point;
import bmDataTypes.DT_SymbolSimplified;
import bmDataTypes.EN_MusicalSymbolName;
import bmPictureAnalysis.IF_SymbolLearnerController;
import bmPictureAnalysis.IF_SymbolListManager;
import bmPictureAnalysis.MusicalSymbolListManager;
import bmPictureAnalysis.SymbolLearnerController;

public class SymbolLearnerControllerTest {

	@Test
	public void testGetInstance() {
		IF_SymbolLearnerController controller1 = SymbolLearnerController.getInstance();
		
		if(controller1 == null){
			fail("No instance was retrieved (controller1");
		}
		
		IF_SymbolLearnerController controller2 = SymbolLearnerController.getInstance();
		
		if(controller2 == null){
			fail("No instance was retrieved (controller2)");
		}
		
		if(controller1 == controller2 ){
			fail("Two different controllers should be retrieved");
		}
		
	}

	@Test
	public void testAnalyzeImage() {
		IF_SymbolLearnerController controller = SymbolLearnerController.getInstance();
		
		ArrayList<DT_SymbolSimplified> testSymbols = new ArrayList<DT_SymbolSimplified>();
		testSymbols.add(new DT_SymbolSimplified(EN_MusicalSymbolName.Fuenf, -0.23529411764, -0.588235294));
		
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		manager.setSymbolList(testSymbols);
		
		DT_MusicalSymbol testSymbol = new DT_MusicalSymbol(EN_MusicalSymbolName.Fuenf);
		
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("./src/unitTests/testResources/test7x11_simpleFive_N0-23529411764705882_N0-5882352941176471.jpg"));
		}catch(IOException e){
			fail("Reading of test image failed: "+e.getMessage());
		}
		
		DT_MusicalSymbol resultSymbol = null;
		try {
			resultSymbol = controller.analyzeImage(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Error during Unit test: "+ e.getMessage());
		}
		
		if(resultSymbol == null){
			fail("There was no MusicalSymbol identified");
		}
		
		if(resultSymbol.getName().compareTo(testSymbol.getName()) != 0){
			fail("The returned MusicalSymbol does not match the expected");
		}
		
	}

	@Test
	public void testSaveNewSymbol() {
		//Get old Symbol List
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		ArrayList<DT_SymbolSimplified> oldList = null, newList = null;
		IF_SymbolLearnerController controller = null;
		
		DT_SymbolSimplified testSymbol = new DT_SymbolSimplified(EN_MusicalSymbolName.B, 0.0, 0.0);
		
		try {
			oldList = moveListItems(manager.getSymbolList());
			
			controller = SymbolLearnerController.getInstance();
			
			controller.saveNewSymbol(testSymbol.getName(), testSymbol.getPosition()[0], testSymbol.getPosition()[1]);
			
			newList = manager.getSymbolList();
		} catch (IOException e) {
			fail("Error during Unit test: "+ e.getMessage());			
		}
		
		//Change the list back
		manager.setSymbolList(oldList);
		try {
			manager.writeSymbolList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Error during Unit test (undo test changes in persistence): "+ e.getMessage());	
		}
		
		boolean found = false;
		for(int i=0; i<newList.size(); i++){
			if(newList.get(i).equalsToSymbol(testSymbol)) found = true;
		}
		
		if(!found){
			fail("The Symbol List was not written correctly");
		}
		
		
		
	}

	@Test
	public void testGetFoundPoints() {
		IF_SymbolLearnerController controller = SymbolLearnerController.getInstance();
		
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("./src/unitTests/testResources/test7x11_simpleFive_N0-23529411764705882_N0-5882352941176471.jpg"));
		}catch(IOException e){
			fail("Reading of test image failed: "+e.getMessage());
		}
		
		try {
			controller.analyzeImage(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Error during Unit test: "+ e.getMessage());
		}
		
		ArrayList<DT_Point> points = controller.getFoundPoints().getListOfPoints();
		
		if(points == null || points.size() == 0){
			fail("There where no points found");
		}
	}
	
	private ArrayList<DT_SymbolSimplified> moveListItems(ArrayList<DT_SymbolSimplified> list){
		ArrayList<DT_SymbolSimplified> resultArray = new ArrayList<DT_SymbolSimplified>(); 
		
		for(int i=0; i<list.size(); i++){
			resultArray.add(list.get(i));
		}
		
		return resultArray;
	}

}
