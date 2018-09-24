package unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import bmDataTypes.DT_SymbolSimplified;
import bmDataTypes.EN_MusicalSymbolName;
import bmPictureAnalysis.IF_SymbolListManager;
import bmPictureAnalysis.MusicalSymbolListManager;

public class MusicalSymbolListManagerTest {
	
	private ArrayList<DT_SymbolSimplified> originalSymbolList = new ArrayList<DT_SymbolSimplified>();
	private ArrayList<DT_SymbolSimplified> testSymbolList = new ArrayList<DT_SymbolSimplified>();
	private boolean setupFailed = false;
	
	private void setup(){
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		try {
			this.originalSymbolList = manager.getSymbolList();
		} catch (IOException e) {
			setupFailed = true;
			fail("Setup failed (getSymbolList): " + e.getMessage());
		}
		
		testSymbolList.add(new DT_SymbolSimplified(EN_MusicalSymbolName.Takt, 0.76471, 0.4118));
		manager.setSymbolList(testSymbolList);
		try {
			manager.writeSymbolList();
		} catch (IOException e) {
			setupFailed = true;
			fail("Setup failed (writeSymbolList): " + e.getMessage());
		}
	}
	
	private void cleanUpAfterTest(){
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		manager.setSymbolList(originalSymbolList);
		testSymbolList.clear();
		try {
			manager.writeSymbolList();
			manager.setSymbolList(null);
		} catch (IOException e) {
			setupFailed = true;
			fail("Clean failed (writeSymbolList): " + e.getMessage());
		}
	}

	@Test
	public void testGetInstance() {
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		if(manager == null){
			fail("getInstance failed (1st call)");
		}
		
		IF_SymbolListManager manager2 = MusicalSymbolListManager.getInstance();
		if(manager2 == null){
			fail("getInstance failed (2nd call)");
		}
		
		if(manager != manager2){
			fail("Instances from call 1 and 2 should be the same");
		}
	}

	@Test
	public void testGetSymbolList() {
		setup();
		
		if(setupFailed == false){
			ArrayList<DT_SymbolSimplified> resultList = null;
			IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
			try {
				resultList = manager.getSymbolList();
			} catch (IOException e) {
				cleanUpAfterTest();
				fail("Test getSymbolList failed (exception)");
			}
			
			cleanUpAfterTest();
			for(int i=0; i<resultList.size(); i++){
				if(resultList.get(i).equals(testSymbolList.get(i)) == false){
					fail("Test getSymbolList failed because result is not equal to reference list.");
				}
			}
		}
	}

	@Test
	public void testSetSymbolList() {
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		testSymbolList.add(new DT_SymbolSimplified(EN_MusicalSymbolName.Takt, 0.76471, 0.4118));
		manager.setSymbolList(testSymbolList);
		ArrayList<DT_SymbolSimplified> resultList = null;
		try {
			resultList = manager.getSymbolList();
		} catch (IOException e) {
			testSymbolList.clear();
			fail("Test setSymbolList failed (exception)");
		}
		
		for(int i=0; i<resultList.size(); i++){
			if(resultList.get(i).equals(testSymbolList.get(i)) == false){
				testSymbolList.clear();
				fail("Test setSymbolList failed because result is not equal to reference list.");
			}
		}
		manager.setSymbolList(null);
		
	}

	@Test
	public void testWriteSymbolList() {
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		testSymbolList.add(new DT_SymbolSimplified(EN_MusicalSymbolName.Takt, 0.76471, 0.4118));
		
		try {
			originalSymbolList = manager.getSymbolList();
		} catch (IOException e) {
			testSymbolList.clear();
			fail("Test writeSymbolList failed (exception during get).");
		}
		
		manager.setSymbolList(testSymbolList);
		
		try {
			manager.writeSymbolList();
		} catch (IOException e) {
			testSymbolList.clear();
			fail("Test writeSymbolList failed (exception during write).");
		}
		
		manager.setSymbolList(null);
		ArrayList<DT_SymbolSimplified> resultList = null;
		
		try {
			resultList = manager.getSymbolList();
		} catch (IOException e) {
			testSymbolList.clear();
			fail("Test writeSymbolList failed (exception during get after write).");
		}
		
		for(int i=0; i<resultList.size(); i++){
			if(resultList.get(i).equalsToSymbol(testSymbolList.get(i)) == false){
				cleanUpAfterTest();				
				fail("Test writeSymbolList failed because result is not equal to reference list.");
			}
		}
		cleanUpAfterTest();	
		
	}

}
