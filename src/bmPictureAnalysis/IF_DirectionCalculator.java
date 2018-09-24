package bmPictureAnalysis;
import bmDataTypes.*;

public interface IF_DirectionCalculator {
	public DT_Point calculatePointFromCurrentPointUsingDirection(int x, int y, EN_direction direction);
}
