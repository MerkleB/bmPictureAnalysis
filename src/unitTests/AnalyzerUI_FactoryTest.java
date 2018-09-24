package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import bmPictureAnalysis.AnalyzerUI_Factory;
import bmPictureAnalysis.EN_AnaylzerName;
import bmPictureAnalysis.IF_AnalyzerUI;
import bmPictureAnalysis.SymbolLearner;

public class AnalyzerUI_FactoryTest {

	@Test
	public void test() {
		IF_AnalyzerUI analyzer = AnalyzerUI_Factory.getAnalyzer(EN_AnaylzerName.SymbolLearner);
		if(!(analyzer instanceof SymbolLearner)){
			fail("Wrong Analyzer was returned");
		}
	}

}
