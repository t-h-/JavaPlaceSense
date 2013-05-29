package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import de.tudarmstadt.wsn.placesense.utility.Scan;
import de.tudarmstadt.wsn.placesense.utility.ScanWindow;
import de.tudarmstadt.wsn.placesense.utility.State;
import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;

public class PlaceSenseCore {

	private int sMax = 3;
	private int tMax = 3;
	private double sampleRate = 0.1; // Hz
	private double windowSize = 40; // seconds
	// sampleRate*windowSize has to be a round integer!!!

	private boolean useOverlapping = false;
	private int numScansPerWindow = 1;

	private int sCur = 0;
	private int tCur = tMax;
	private double repThreshold = 0.7;
	private boolean isStationary = false;
	private HashMap<String, Double> fingerprint = new HashMap<String, Double>(); // guess  this will not be used
	private HashMap<String, Integer> history = new HashMap<String, Integer>(); // also fingerprint?
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

	public ArrayList<HashMap<String, Double>> executePlaceSense() {
		numScansPerWindow = (int) (sampleRate * windowSize);
		ArrayList<ScanWindow> scanWindowList = UtilityMethods.createScanWindowList(scanList, numScansPerWindow, useOverlapping);

		ArrayList<HashMap<String, Double>> fingerprintList = new ArrayList<HashMap<String, Double>>();
		// starting with i=1 because of need of comparison (= first window) 
		//comment here for real time hook
		for (int i = 0; i < scanWindowList.size(); i++) {
			if (isStationary) { // look for departure
				if (detectDeparture(scanWindowList.get(i))) {
					calculateFingerprint();
					fingerprintList.add(new HashMap<String, Double>(fingerprint));
					fingerprint.clear();
					history.clear();
					representativeSet.clear();
					sCur = 0;
					tCur = tMax;
					System.out.println("departure detected at scanwindow index: " + i);
					isStationary = false;
				}
			}
			if (!isStationary) { // look for entrance
				if (detectEntrance(scanWindowList.get(i))) {
					System.out.println("entrance detected at scanwindow index: " + i);
					determineRepresentativeBeacons();
					isStationary = true;
				}
			}
		}
		return fingerprintList;
	}

	private boolean detectEntrance(ScanWindow sW) {
		if (isSubsetOfHistory(sW) && !history.isEmpty()) {
			updateHistory(sW);
			sCur++;
		} else { // if(!isSubset(sW) || history.isEmpty())
			history.clear();
			// init of history here:
			HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(sW);
			for (String accP : curSeenAPs) {
				history.put(accP, 1);
			}
			sCur = 1;
		}
		return (sCur == sMax) ? true : false;
	}

	private boolean detectDeparture(ScanWindow scanWindow) {

		HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(scanWindow);
		boolean changeThroughRemoval = curSeenAPs.removeAll(representativeSet);
		if(!changeThroughRemoval & !curSeenAPs.isEmpty()){ // = if no AP from repSet is seen in current scanwindow
															// and new beacons are seen in scanwindow
			tCur--;											// decrease t
		} else {
			sCur++;  //detectDeparture gets called every time a new scan window comes in, thus incrementing here. //(sCur is also counter for determining how many scans since stay)
			updateHistory(scanWindow);
			tCur = tMax;
		}
		return (tCur == 0) ? true : false;
	}


	private boolean isSubsetOfHistory(ScanWindow sW) {
		boolean res = true;
		for (ArrayList<String> aL : sW.getScanWindow()) {
			if (!history.keySet().containsAll(aL)) {
				res = false;
				break;
			}
		}
		return res;
	}


	private HashSet<String> getUniqueAPsFromScanWindow(ScanWindow sW) {
		HashSet<String> resultSet = new HashSet<String>();
		for (ArrayList<String> scan : sW.getScanWindow()) {
			for (String accPoint : scan) {
				resultSet.add(accPoint);
			}
		}
		return resultSet;
	}

	private void updateHistory(ScanWindow scanWindow) {
		HashSet<String> tmpSet = getUniqueAPsFromScanWindow(scanWindow);
		for (String accP : tmpSet) {
			if (history.get(accP) == null) {
				history.put(accP, 1);
			} else {
				history.put(accP, history.get(accP) + 1);
			}
		}
	}

	private void determineRepresentativeBeacons() {
		calculateFingerprint();
		for(Entry<String, Double> accP : fingerprint.entrySet()){
			if(accP.getValue() >= repThreshold){
				representativeSet.add(accP.getKey());
			}
		}
	}
	
	private void calculateFingerprint(){
		for(Entry<String, Integer> accP : history.entrySet()){
			fingerprint.put(accP.getKey(), (double)(accP.getValue()) / sCur);
		}
	}
}
