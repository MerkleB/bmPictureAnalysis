package bmPictureAnalysis;

import java.awt.image.BufferedImage;

import bmDataTypes.*;

public interface IF_SymbolIdentifier {
	public DT_MusicalSymbol identifySymbol(BufferedImage image, int x, int y);
	public DT_Point[] getFoundExtremePoints();
	public DT_PointCloud getListOfPointsFound();
	public double getAvgXofFoundSymbol();
	public double getAvgYofFoundSymbol();
}
