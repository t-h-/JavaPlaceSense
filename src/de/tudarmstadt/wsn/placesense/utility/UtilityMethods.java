package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilityMethods {

	public static List<String[]> convert2DArrayTo2DCollection(String[][] argScans) {
		List<String[]> resultingList = new ArrayList<String[]>();
		List<String[]> tmpList = Arrays.asList(argScans);
		for(String[] strArr : tmpList){
			resultingList.add(strArr);
		}
		return resultingList;
	}
	
	
	public static ArrayList<ArrayList<String[]>> createScanWindowList(List<String[]> argScanList, int stepSize, boolean useOverlapping) {
		ArrayList<ArrayList<String[]>> resultScanWindowList = new ArrayList<ArrayList<String[]>>();
		ArrayList<String[]> curScanWindow = new ArrayList<String[]>();
		if(!useOverlapping){
			int windex = 1;
			for(int i = 0; i < argScanList.size(); i++){
				
				curScanWindow.add(argScanList.get(i));
	
				if(windex == stepSize){
					resultScanWindowList.add(curScanWindow);
					curScanWindow = new ArrayList<String[]>();
					windex = 0;
				}
				
				windex++;
				
			}
		}
		else{
			for(int i = 0; i < argScanList.size() - stepSize; i++){
				for(int j = 0; j < stepSize; j++){
						curScanWindow.add(argScanList.get(i+j));
				}
				resultScanWindowList.add(curScanWindow);
				curScanWindow = new ArrayList<String[]>();
			}
		}
		return resultScanWindowList;
	}
}
