package bmPictureAnalysis;

import java.awt.image.BufferedImage;
import bmDataTypes.DT_MusicalSymbol;

public interface IF_SymbolIdentifier {
	public DT_MusicalSymbol identifySymbol(BufferedImage image, int x, int y);
}
