package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.List;

public class PlaceSenseCore {
	
	private boolean isStationary = false;
	private List<ArrayList<String>> scanList = new ArrayList<ArrayList<String>>();
	private int sMax = 3;
	private int tMax = 3;
	private double sampleRate = 0.1; //Hz
	private double windowSize = 60; //seconds
	//sampleRate*windowSize has to be a round integer!!!
	private boolean useOverlapping = false;
	private int stepSize = 1;
	

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
		stepSize = (int) (useOverlapping ? 1 : sampleRate * windowSize);
		
		for(int i = 0; i < scanList.size(); i += stepSize){
			if(isStationary){ //look for departure
				detectDeparture(i);
			}
			if(!isStationary){ //look for entrance
				detectEntrance(i);
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
