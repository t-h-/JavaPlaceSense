package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.wsn.placesense.utility.ScanWindow;
import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;

public class PlaceSenseCore {
	
	private boolean isStationary = false;
	private List<String[]> scanList = new ArrayList<String[]>();
	private int sMax = 3;
	private int tMax = 3;
	private double sampleRate = 0.1; //Hz
	private double windowSize = 60; //seconds
	//sampleRate*windowSize has to be a round integer!!!
	private boolean useOverlapping = true;
	private int numScansPerWindow = 1;
	private ArrayList<String> history = new ArrayList<String>();

	public PlaceSenseCore(List<String[]> scanList) {
		this.scanList = scanList;
	}
	public PlaceSenseCore(List<String[]> scanList, int sMax, int tMax, double sampleRate, double windowSize, boolean useOverlapping) {
		this.scanList = scanList;
		this.sMax = sMax;
		this.tMax = tMax;
		this.sampleRate = sampleRate;
		this.windowSize = windowSize;
		this.useOverlapping = useOverlapping;
	}
	
	public void executePlaceSense(){
		numScansPerWindow = (int) (sampleRate * windowSize);
		int sCur = 0;
		
		//create list of scan windows from scanList, könnte auch arraylist<arraylist> sein...
		ArrayList<ArrayList<String[]>> scanWindowList = UtilityMethods.createScanWindowList(scanList, numScansPerWindow, useOverlapping);
		
		//starting with i=1 because of need of comparison (= first window)
		for(int i = 1; i < scanWindowList.size(); i++){
			if(isStationary){ //look for departure
				boolean tmp = detectDeparture(i);
			}
			if(!isStationary){ //look for entrance
				boolean tmp = detectEntrance(i);
				if(tmp){
					
				}
			}
		}
	}
	

	private boolean detectEntrance(int i) {
		return false;
	}
	
	private boolean detectDeparture(int i) {
		return false;
	}
	


}
