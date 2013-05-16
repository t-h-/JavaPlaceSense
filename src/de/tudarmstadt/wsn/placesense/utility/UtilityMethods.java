package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilityMethods {

	public static List<ArrayList<String>> convert2DArrayTo2DCollection(String[][] argScans) {
		List<ArrayList<String>> resultingList = new ArrayList<ArrayList<String>>();
		List<String[]> tmpList = Arrays.asList(argScans);
		for(String[] strArr : tmpList){
			ArrayList<String> curList = new ArrayList<String>(Arrays.asList(strArr));
			resultingList.add(curList);
		}
		return resultingList;
	}
	
	
	public static ArrayList<ScanWindow> createScanWindowList(List<ArrayList<String>> argScanList, int stepSize, boolean useOverlapping) {
		ArrayList<ScanWindow> resultScanWindowList = new ArrayList<ScanWindow>();
		ScanWindow curScanWindow = new ScanWindow();
		if(!useOverlapping){
			int wIndex = 1;
			for(int i = 0; i < argScanList.size(); i++){
				
				curScanWindow.addScan(argScanList.get(i));
	
				if(wIndex == stepSize){
					resultScanWindowList.add(curScanWindow);
					curScanWindow = new ScanWindow();
					wIndex = 0;
				}
				
				wIndex++;
				
			}
		}
		else{
			for(int i = 0; i < argScanList.size() - stepSize; i++){
				for(int j = 0; j < stepSize; j++){
						curScanWindow.addScan(argScanList.get(i+j));
				}
				resultScanWindowList.add(curScanWindow);
				curScanWindow = new ScanWindow();
			}
		}
		return resultScanWindowList;
	}
}
