package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.tudarmstadt.wsn.placesense.utility.ScanWindow;
import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;

public class PlaceSenseCore {
	
	private int sMax = 3;
	private int tMax = 3;
	private double sampleRate = 0.1; //Hz
	private double windowSize = 30; //seconds
	//sampleRate*windowSize has to be a round integer!!!
	private boolean useOverlapping = false;
	private int numScansPerWindow = 1;
	private int sCur = 0;
	private int tCur = tMax;
	
	private boolean isStationary = false;
	private HashSet<String> history = new HashSet<String>();
	private List<ArrayList<String>> scanList = new ArrayList<ArrayList<String>>();
	private HashSet<String> representativeSet = new HashSet<String>();
	
	public PlaceSenseCore(List<ArrayList<String>> scanList) {
		this.scanList = scanList;
	}
	public PlaceSenseCore(List<ArrayList<String>> scanList, int sMax, int tMax, double sampleRate, double windowSize, boolean useOverlapping) {
		this.scanList = scanList;
		this.sMax = sMax;
		this.tMax = tMax;
		this.sampleRate = sampleRate;
		this.windowSize = windowSize;
		this.useOverlapping = useOverlapping;
	}
	
	public void executePlaceSense(){
		numScansPerWindow = (int) (sampleRate * windowSize);
		
		ArrayList<ScanWindow> scanWindowList = UtilityMethods.createScanWindowList(scanList, numScansPerWindow, useOverlapping);
		
		//starting with i=1 because of need of comparison (= first window)
		for(int i = 0; i < scanWindowList.size(); i++){
			if(isStationary){ //look for departure
				if(detectDeparture(scanWindowList.get(i))){
					isStationary = false;
				}
			}
			if(!isStationary){ //look for entrance
				if(detectEntrance(scanWindowList.get(i))){
					isStationary = true;
				}
			}
		}
	}
	

	private boolean detectEntrance(ScanWindow sW) {
		if(isSubset(sW) && !history.isEmpty()){
			sCur++;
		}
		else { //  if(!isSubset(sW) || history.isEmpty())
			history.clear();
			history = getUniqueAPsFromScanWindow(sW);
			sCur = 0;
		}
		return (sCur == sMax) ? true : false;
	}
	
	
	private boolean detectDeparture(ScanWindow scanWindow) {
		return false;
	}
	
	private boolean isSubset(ScanWindow sW) {
		boolean res = true;
		for(ArrayList<String> aL : sW.getScanWindow()){
			if(!history.containsAll(aL)){
				res = false;
				break;
			}
		}
		return res;
	}
	
	private HashSet<String> getUniqueAPsFromScanWindow(ScanWindow sW){
		HashSet<String> resultSet = new HashSet<String>();
		for(ArrayList<String> aL : sW.getScanWindow()){
			resultSet.addAll(aL);
		}
		return resultSet;
	}

}
