package bmPictureAnalysis;

import java.io.IOException;
import java.util.ArrayList;

import bmDataTypes.DT_SymbolSimplified;

public interface IF_SymbolListManager {
	public ArrayList<DT_SymbolSimplified> getSymbolList() throws IOException;
	public void setSymbolList(ArrayList<DT_SymbolSimplified> symbolList);
	public void writeSymbolList() throws IOException;
}
