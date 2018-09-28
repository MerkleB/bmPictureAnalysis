package bmPictureAnalysis;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import bmDataTypes.*;

public class SymbolLearnerController implements IF_SymbolLearnerController {
	
	private static SymbolLearnerController instance = null;
	private IF_SymbolIdentifier symbolIdentifier = null;
	
	public static IF_SymbolLearnerController getInstance() {
		
		instance = new SymbolLearnerController();
		
		return instance;
	}
	
	@Override
	public DT_MusicalSymbol analyzeImage(BufferedImage image) throws IOException {
		
		DT_MusicalSymbol foundSymbol = null;
		
		if(symbolIdentifier == null){
			IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
			
			ArrayList<DT_SymbolSimplified> simpleSymbols = null;
			try {
				simpleSymbols = manager.getSymbolList();
			}catch (IOException e){
				e.printStackTrace();
				throw e;
			}
			
			symbolIdentifier = NotesSymbolIdentifier.getInstance(simpleSymbols);
		}
		
		IF_PixelAnalyzer analyser = PixelAnalyzer.getInstance();
		
		LoopOverImage:
		for(int y=0; y<image.getHeight(); y++){
			for(int x=0; x<image.getWidth()-1; x++){
				if(analyser.pixelColorChanges(image, x, y, EN_direction.East)){
					foundSymbol = symbolIdentifier.identifySymbol(image, x+1, y);
					if(foundSymbol != null){
						break LoopOverImage;
					}else{
						foundSymbol = new DT_MusicalSymbol(EN_MusicalSymbolName.unknown);
						break LoopOverImage;
					}
				}
			}
		}
		
		return foundSymbol;
	}

	@Override
	public void saveNewSymbol(EN_MusicalSymbolName name, double avgX, double avgY) throws IOException {
		IF_SymbolListManager manager = MusicalSymbolListManager.getInstance();
		ArrayList<DT_SymbolSimplified> symbolList = null;
		try {
			symbolList = manager.getSymbolList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		if(symbolList != null){
			symbolList.add(new DT_SymbolSimplified(name, avgX, avgY));
		}else{
			symbolList = new ArrayList<DT_SymbolSimplified>();
			symbolList.add(new DT_SymbolSimplified(name, avgX, avgY));
		}
		
		manager.setSymbolList(symbolList);
		
		try {
			manager.writeSymbolList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public DT_PointCloud getFoundPoints() {
		DT_PointCloud points = symbolIdentifier.getListOfPointsFound();
		return points;
	}

	@Override
	public double getActualAvgX() {
		return symbolIdentifier.getAvgXofFoundSymbol();
	}

	@Override
	public double getActualAvgY() {
		return symbolIdentifier.getAvgYofFoundSymbol();
	}

}
