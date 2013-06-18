package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing methods generally used to set up the needed data structure needed by the algorithm from "human writable"
 * input data
 * @author Tomi
 *
 */
public class UtilityMethods {

	/**
	 * Gets a 2-dimensional string array (which can easily be initialized) and returns its list representation.
	 * @param argScans
	 * @return
	 */
	public static List<ArrayList<String>> convert2DArrayTo2DCollection(String[][] argScans) {
		List<ArrayList<String>> resultingList = new ArrayList<ArrayList<String>>();
		List<String[]> tmpList = Arrays.asList(argScans);
		for(String[] strArr : tmpList){
			ArrayList<String> curList = new ArrayList<String>(Arrays.asList(strArr));
			resultingList.add(curList);
		}
		return resultingList;
	}
	
	
	/**
	 * Creates a list of scan windows from list of scans. Caution, cannot be used to dynamically create "sliding window" mode, just static.
	 * @param argScanList
	 * @param stepSize - this argument is basically the number of scans a scan window contains. hast to get calculated "outside"
	 * and passed as an argument.
	 * @param useOverlapping
	 * @return - the resulting list
	 */
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
