package bmPictureAnalysis;

import java.util.ArrayList;
import java.io.*;

import bmDataTypes.DT_SymbolSimplified;
import bmDataTypes.EN_MusicalSymbolName;

public class MusicalSymbolListManager implements IF_SymbolListManager {
	
	private static MusicalSymbolListManager Instance;
	private static final String ListFileURI = "./src/bmPictureAnalysis/MusicalSymbolList.csv";
	private ArrayList<DT_SymbolSimplified> SymbolList = null;
	
	
	public static IF_SymbolListManager getInstance(){
		if(Instance == null){
			Instance = new MusicalSymbolListManager();
		}
		return Instance;
	}
	
	
	@Override
	public ArrayList<DT_SymbolSimplified> getSymbolList() throws IOException {
		if(SymbolList == null){
			BufferedReader bufferedReader = readFileIntoBufferedReader();
			SymbolList = convertBufferedReaderIntoArrayList(bufferedReader);
		}
		return SymbolList;
	}

	@Override
	public void setSymbolList(ArrayList<DT_SymbolSimplified> symbolList) {
		SymbolList = symbolList;
	}

	@Override
	public void writeSymbolList() throws IOException {
		String csvString = convertSymbolListToCSV_String(SymbolList);
		File file = new File(ListFileURI);
		file.createNewFile();
		FileOutputStream fileWriter = new FileOutputStream(file);
		
		for(int i=0; i<csvString.length(); i++){
			fileWriter.write((byte)csvString.charAt(i));
		}
		fileWriter.close();
	}
	
	private BufferedReader readFileIntoBufferedReader() throws IOException{
		File file = new File(ListFileURI);
		BufferedReader bufferedReader = null;
		file.createNewFile();
		FileReader fileReader = new FileReader(ListFileURI);
		bufferedReader = new BufferedReader(fileReader);
		
		return bufferedReader;	
	}
	
	private ArrayList<DT_SymbolSimplified> convertBufferedReaderIntoArrayList(BufferedReader bufferedReader) throws IOException{
		ArrayList<DT_SymbolSimplified> symbolList = new ArrayList<DT_SymbolSimplified>();
		
		boolean endOfFile = false;
		int index = 0;
		String readString;
		String[] headArray = new String[10], dataArray;
		
		while(endOfFile == false){
			readString = bufferedReader.readLine();
			if(readString != null){
				if(index == 0){ //HeadLine
					 headArray = readString.split(";");
				}else{
					 dataArray = readString.split(";");
					 symbolList.add(convertArrayToSimplifiedSymbol(headArray, dataArray));
				}
				index++;
			}else{
				endOfFile = true;
				bufferedReader.close();
			}
		}
		
		return symbolList;
	}
	
	private DT_SymbolSimplified convertArrayToSimplifiedSymbol(String[] attributes, String[] values){
		DT_SymbolSimplified simplifiedSymbol = null;
		EN_MusicalSymbolName name = EN_MusicalSymbolName.Note;
		Double x = 0.0, y = 0.0;
		
		for(int i=0; i<attributes.length; i++){
			if(attributes[i].equals("Name")){
				name = EN_MusicalSymbolName.valueOf(values[i]);
			}else if(attributes[i].equals("X")){
				x = Double.parseDouble(values[i]);
			
			}else if(attributes[i].equals("Y")){
				y = Double.parseDouble(values[i]);
			}
					
		}
		simplifiedSymbol = new DT_SymbolSimplified(name, x, y);
		
		return simplifiedSymbol;
	}

	private String convertSymbolListToCSV_String(ArrayList<DT_SymbolSimplified> symbolList){
		String lineSeparator = "\n";
		String resultString = "Name;X;Y";
		DT_SymbolSimplified symbol = null;
		
		for(int i=0; i<symbolList.size(); i++){
			symbol = symbolList.get(i);
			resultString = resultString + lineSeparator + symbol.toString(); 
		}
		
		if(symbol == null){
			return "";
		}else return resultString;
	}
}