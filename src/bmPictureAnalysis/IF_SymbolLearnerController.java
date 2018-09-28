package bmPictureAnalysis;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import bmDataTypes.*;

public interface IF_SymbolLearnerController {
	public DT_MusicalSymbol analyzeImage(BufferedImage image) throws IOException;
	public void saveNewSymbol(EN_MusicalSymbolName name, double avgX, double avgY) throws IOException;
	public DT_PointCloud getFoundPoints();
	public double getActualAvgX();
	public double getActualAvgY();
}
