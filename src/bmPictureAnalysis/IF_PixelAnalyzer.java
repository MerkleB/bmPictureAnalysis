package bmPictureAnalysis;

import java.awt.image.BufferedImage;

public interface IF_PixelAnalyzer {
	public boolean pixelColorChanges(BufferedImage image, int x, int y, EN_direction direction);
	public boolean pixelColorIsEqual(BufferedImage image, int x, int y, EN_direction direction);
}
