package bmPictureAnalysis;

import javax.swing.JFrame;

public class PictureAnalyzer {
	
	public static void main(String[] args) {
		IF_AnalyzerUI analyzer = AnalyzerUI_Factory.getAnalyzer(EN_AnaylzerName.SymbolLearner);
		JFrame ui = analyzer.getAnalyzerUI();
		ui.setVisible(true);
	}

}
