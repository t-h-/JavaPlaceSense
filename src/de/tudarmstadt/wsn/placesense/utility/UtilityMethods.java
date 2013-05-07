package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilityMethods {

	public static List<ArrayList<String>> convert2DArrayTo2DCollection(String[][] argScans) {
		List<ArrayList<String>> resultingList = new ArrayList<ArrayList<String>>();
		List<String[]> tmpList = Arrays.asList(argScans);
		for(String[] str : tmpList){
			resultingList.add(new ArrayList<String>(Arrays.asList(str)));
		}
		return resultingList;
	}
	
	public static List<ScanWindow> createScanWindowsCollection(List<ArrayList<String>> scanList){
		return null;
	}
	
}
