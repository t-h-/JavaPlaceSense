package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.tudarmstadt.wsn.placesense.utility.Fingerprint;
import de.tudarmstadt.wsn.placesense.utility.ScanWindow;

public class PlaceSenseCore {

	// parameters
	private int sMax = 3;
	private int tMax = 3;
	private double sampleRate = 0.1; // Hz
	private double windowSize = 40; // seconds
	private double repThreshold = 0.7;
	// sampleRate*windowSize has to be a round integer!!!

	// state
	private int sCur = 0;
	private int tCur = tMax;
	private boolean isStationary = false;
	private Fingerprint curFingerprint = new Fingerprint();
	private HashMap<String, Integer> curHistory = new HashMap<String, Integer>();
	private HashSet<String> representativeSet = new HashSet<String>();
	private ArrayList<ScanWindow> scanWindowList = new ArrayList<ScanWindow>();
	private ArrayList<Fingerprint> knownPlaces = new ArrayList<Fingerprint>();

	public PlaceSenseCore(ArrayList<ScanWindow> scanWindowList) {
		this.scanWindowList = scanWindowList;
	}

	public PlaceSenseCore(ArrayList<ScanWindow> scanWindowList, int sMax, int tMax, double sampleRate, double windowSize, double repThreshold) {
		this.scanWindowList = scanWindowList;
		this.sMax = sMax;
		this.tMax = tMax;
		this.sampleRate = sampleRate;
		this.windowSize = windowSize;
		this.repThreshold = repThreshold;
	}

	public ArrayList<HashMap<String, Double>> executePlaceSense() {

		ArrayList<HashMap<String, Double>> fingerprintList = new ArrayList<HashMap<String, Double>>();
		for (int i = 0; i < scanWindowList.size(); i++) {
			if (isStationary) { // look for departure
				if (detectDeparture(scanWindowList.get(i))) {
					calculateCurFingerprint();
					// fingerprintList.add(new HashMap<String, Double>(fingerprint));

					// fingerprint.clear();
					curHistory.clear();
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
		if (isSubsetOfHistory(sW)) {
			updateHistory(sW);
			sCur++;
		} else { // if(!isSubset(sW) || history.isEmpty())
			curHistory.clear();
			// init of history here: uncomment following lines to use more
			// efficient variant.
			// HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(sW);
			// for (String accP : curSeenAPs) {
			// history.put(accP, 1);
			// }
			sCur = 1;
		}
		return (sCur == sMax) ? true : false;
	}

	private boolean detectDeparture(ScanWindow scanWindow) {

		HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(scanWindow);
		boolean changeThroughRemoval = curSeenAPs.removeAll(representativeSet);
		if (!changeThroughRemoval & !curSeenAPs.isEmpty()) { 	// = if no AP from repSet is seen in current scanwindow
																// and new beacons are seen in scanwindow
			tCur--; // decrease t
		} else {
			sCur++; // detectDeparture gets called every time a new scan window
					// comes in, thus incrementing here. //(sCur is also counter
					// for determining how many scans since stay)
			updateHistory(scanWindow);
			tCur = tMax;
		}
		return (tCur == 0) ? true : false;
	}

	private boolean isSubsetOfHistory(ScanWindow sW) {
		boolean res = true;
		if (curHistory.isEmpty()) return true; // just comment out this condition for efficient variant.
		for (ArrayList<String> aL : sW.getScanWindow()) {
			if (!curHistory.keySet().containsAll(aL)) {
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

	/**
	 * 
	 * @param scanWindow
	 */
	private void updateHistory(ScanWindow scanWindow) {
		HashSet<String> tmpSet = getUniqueAPsFromScanWindow(scanWindow);
		for (String accP : tmpSet) {
			if (curHistory.get(accP) == null) {
				curHistory.put(accP, 1);
			} else {
				curHistory.put(accP, curHistory.get(accP) + 1);
			}
		}
	}

	private void determineRepresentativeBeacons() {
		calculateCurFingerprint();
		for (Entry<String, Double> accP : curFingerprint.getFingerprint().entrySet()) {
			if (accP.getValue() >= repThreshold) {
				representativeSet.add(accP.getKey());
			}
		}
	}

	private void calculateCurFingerprint() {
		for (Entry<String, Integer> accP : curHistory.entrySet()) {
			curFingerprint.getFingerprint().put(accP.getKey(), (double) (accP.getValue()) / sCur);
		}
	}

	private void fingerprintIsInKnownPlaces() {

	}

	private void beaconPrintCompare() {

	}

	private void beaconPringMerge() {

	}
}
