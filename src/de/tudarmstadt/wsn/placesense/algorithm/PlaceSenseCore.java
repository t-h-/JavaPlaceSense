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

	public PlaceSenseCore(ArrayList<ScanWindow> scanWindowList, int sMax, int tMax, double repThreshold) {
		this.scanWindowList = scanWindowList;
		this.sMax = sMax;
		this.tMax = tMax;
		this.repThreshold = repThreshold;
	}

	public ArrayList<HashMap<String, Double>> executePlaceSense() {

		for (int i = 0; i < scanWindowList.size(); i++) {
			if (isStationary) { // look for departure
				if (detectDeparture(scanWindowList.get(i))) {
					calculateCurFingerprint();
					
					int bestIndex = getIndexOfBestMatchingPlace();
					if(bestIndex != -1){
						mergeCurFingerprintWith(bestIndex);
						System.out.println("merging "  + (i+1) + " to " + bestIndex);
					} else {
						knownPlaces.add(new Fingerprint(curFingerprint)); 
						System.out.println("adding " + (i+1));
					}
					
					curFingerprint.clear();
					curHistory.clear();
					representativeSet.clear();
					sCur = 0;
					tCur = tMax;
					System.out.println("departure detected at scanwindow index: " + (i+1));
					isStationary = false;
				}
			}
			else if (!isStationary) { // look for entrance
				if (detectEntrance(scanWindowList.get(i))) {
					System.out.println("entrance detected at scanwindow index: " + (i+1));
					determineRepresentativeBeacons();
					isStationary = true;
				}
			}
		}
		
		return null;
	}



	private boolean detectEntrance(ScanWindow sW) {
		if (isSubsetOfHistory(sW)) {
			updateHistory(sW);
			sCur++;
		} else { // if(!isSubset(sW) || history.isEmpty())
			curHistory.clear();
			// init of history here: uncomment following lines to use more
			// efficient variant. (plus comment an additional line further below, see comments)
//			HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(sW);	//
//			for (String accP : curSeenAPs) {								//
//				curHistory.put(accP, 1);									//
//			}																//
//			sCur = 1;														//
			sCur = 0;
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
		if (curHistory.isEmpty()) return true; // just comment out this condition for efficient variant.
		boolean res = true;
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
		double maxResponseRate = 0;
		String apWithMaxResponseRate = null;
		calculateCurFingerprint();
		for (Entry<String, Double> accP : curFingerprint.getFingerprint().entrySet()) {
			double curResponseRate = accP.getValue();
			if (curResponseRate >= repThreshold) {
				representativeSet.add(accP.getKey());
			}
			if (curResponseRate > maxResponseRate){
				apWithMaxResponseRate = accP.getKey();
			}
		}
		if(representativeSet.isEmpty()){
			representativeSet.add(apWithMaxResponseRate);
		}
	}

	private void calculateCurFingerprint() {
		curFingerprint.setHistory(new HashMap<String, Integer>(curHistory));
		curFingerprint.setNumScans(sCur);
		for (Entry<String, Integer> accP : curHistory.entrySet()) {
			curFingerprint.getFingerprint().put(accP.getKey(), (double) (accP.getValue()) / sCur);
		}
	}

	private int getIndexOfBestMatchingPlace() {
		int bestIndex = -1;
		double bestMatchingCoefficient = 0;
		
		if(knownPlaces.isEmpty()){
			return bestIndex;
		}
		
		for(int i = 0; i < knownPlaces.size(); i++){
			double curCoeff = getSimilarityCoefficient(knownPlaces.get(i));
			if(curCoeff >= 0.68 && curCoeff > bestMatchingCoefficient){
				bestIndex = i;
				bestMatchingCoefficient = curCoeff;
			}
		}
		return bestIndex;
	}
	
	//TODO extend to uncovered APs... remember already iterated places...
	private double getSimilarityCoefficient(Fingerprint tmpFP){
		double maxedValues = 0;
		double minnedValues = 0;
		for(Entry<String, Double> tmpAP : tmpFP.getFingerprint().entrySet()){
			Double curFPValue = curFingerprint.getFingerprint().get(tmpAP.getKey());
			maxedValues += Math.max(tmpAP.getValue(), (curFPValue == null) ? 0 : curFPValue);
			minnedValues += Math.min(tmpAP.getValue(), (curFPValue == null) ? Double.MAX_VALUE : curFPValue);
		}
		return (minnedValues / maxedValues);
	}
	
	private void mergeCurFingerprintWith(int bestIndex) {
		// TODO Auto-generated method stub
		Fingerprint fpFromKnownPlaces = knownPlaces.get(bestIndex);
		fpFromKnownPlaces.addToHistory(curHistory);
		fpFromKnownPlaces.addToNumScansDuringScans(sCur);
		fpFromKnownPlaces.calcFingerprint();
	}

}
