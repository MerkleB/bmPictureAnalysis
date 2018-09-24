package bmPictureAnalysis;

public class AnalyzerUI_Factory {
	public static IF_AnalyzerUI getAnalyzer(EN_AnaylzerName name){
		switch (name){
			case SymbolLearner: return SymbolLearner.getInstance(); 
			default: return SymbolLearner.getInstance();	  
		}
	}
}
