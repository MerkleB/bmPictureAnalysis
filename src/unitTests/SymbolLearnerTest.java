package unitTests;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.junit.Test;

import bmPictureAnalysis.IF_AnalyzerUI;
import bmPictureAnalysis.SymbolLearner;

public class SymbolLearnerTest {

	@Test
	public void testGetInstance() {
		IF_AnalyzerUI analyzer1 = SymbolLearner.getInstance();
		
		if(analyzer1 == null){
			fail("There was no instance retrieved");
		}
		
		IF_AnalyzerUI analyzer2 = SymbolLearner.getInstance();
		
		if(analyzer2 == null){
			fail("There was no 2n instance retrieved");
		}
		
		if(analyzer1 != analyzer2){
			fail("There where to different instances retrieved which");
		}
	}

	@Test
	public void testGetAnalyzerUI() {
		IF_AnalyzerUI analyzer = SymbolLearner.getInstance();
		JFrame frame = analyzer.getAnalyzerUI();
		if(frame == null){
			fail("No frame was retrieved");
		}
	}

}
