package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.tudarmstadt.wsn.placesense.utility.Fingerprint;
import de.tudarmstadt.wsn.placesense.utility.ScanWindow;

/**
 * Class containing all methods to execute the algorithm, i.e. the logic and several helper methods the algorithm directly
 * needs.
 * Note that there is the possibility  to start entrance detection one window earlier by commenting/uncommenting two places
 * in the code. Hinted on by "todo" marks
 * @author Tomi
 *
 */
public class PlaceSenseCore {

	// parameters: initialized with a standard set. can be overwritten by constructor
	private int sMax = 3;
	private int tMax = 3;
	private double repThreshold = 0.7;
	private static double SMILILARITY_THRESHOLD = 0.68;

	// state: these field variables constitute the state of the system. they get accessed or modified by the class methods
	private int sCur = 0;
	private int tCur = tMax;
	private boolean isStationary = false;
	private Fingerprint curFingerprint = new Fingerprint();
	//next variable: very important class variable. HashMap of unique access points and the number they were seen during a stay
	private HashMap<String, Integer> curHistory = new HashMap<String, Integer>(); 
	private HashSet<String> representativeSet = new HashSet<String>();
	private ArrayList<ScanWindow> scanWindowList = new ArrayList<ScanWindow>();
	private ArrayList<Fingerprint> knownPlaces = new ArrayList<Fingerprint>();

	/**
	 * Basic constructor. Constructs instance with "standard" parameters and only takes a list of scan windows the algorithm
	 * needs to iterate over
	 * @param scanWindowList
	 */
	public PlaceSenseCore(ArrayList<ScanWindow> scanWindowList) {
		this.scanWindowList = scanWindowList;
	}

	/**
	 * Advanced constructor. Also takes parameters of the algorithm as arguments
	 * @param scanWindowList
	 * @param sMax
	 * @param tMax
	 * @param repThreshold
	 */
	public PlaceSenseCore(ArrayList<ScanWindow> scanWindowList, int sMax, int tMax, double repThreshold) {
		this.scanWindowList = scanWindowList;
		this.sMax = sMax;
		this.tMax = tMax;
		this.repThreshold = repThreshold;
	}

	/**
	 * This is the method to call when wanting to execute the algorithm. It contains the state-machine-like logic to look
	 * for entrance or departure. In order to call it correctly, an instance of PlaceSenseCore has to be created with all
	 * necessary parameters and the scanWindowList of input data.
	 */
	public void executePlaceSense() {

		for (int i = 0; i < scanWindowList.size(); i++) {
			if (isStationary) { // look for departure
				if (detectDeparture(scanWindowList.get(i))) {
					System.out.println("departure detected at scanwindow index: " + (i+1));
					calculateCurFingerprint();
					
					int bestIndex = getIndexOfBestMatchingPlace();
					if(bestIndex != -1){
						mergeCurFingerprintWith(bestIndex);
						System.out.println("merging "  + (i+1) + " to knownPlaces at: " + bestIndex);
					} else {
						knownPlaces.add(new Fingerprint(curFingerprint)); 
						System.out.println("adding " + (i+1));
					}
					
					curFingerprint.clear();
					curHistory.clear();
					representativeSet.clear();
					sCur = 0;
					tCur = tMax;
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
	}


	/**
	 * When system is in mobile mode (isStationary == false) this method gets called and compares the new scan window to curHistory
	 * @param sW
	 * @return - true when sCur == sMax
	 */
	private boolean detectEntrance(ScanWindow sW) {
		if (isSubsetOfHistory(sW)) {
			updateHistory(sW);
			sCur++;
		} else { // if(!isSubset(sW) || history.isEmpty())
			curHistory.clear();
			// TODO init of history here: uncomment following lines to use more
			// efficient variant. (plus comment an additional line further below, also commented)
//			HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(sW);	//
//			for (String accP : curSeenAPs) {								//
//				curHistory.put(accP, 1);									//
//			}																//
//			sCur = 1;														//
			sCur = 0;
		}
		return (sCur == sMax) ? true : false;
	}

	/**
	 * When system is in stationary mode (isStationary == true) this method gets called. Again, it compares the new incoming
	 * scan window to curHistory.
	 * @param scanWindow - current scan window
	 * @return - true when tCur == tMax
	 */
	private boolean detectDeparture(ScanWindow scanWindow) {
		HashSet<String> curSeenAPs = getUniqueAPsFromScanWindow(scanWindow);
		boolean changeThroughRemoval = curSeenAPs.removeAll(representativeSet);
		if (!changeThroughRemoval & !curSeenAPs.isEmpty()) { 	// = if no AP from repSet is seen in current scanwindow
																// and new beacons are seen in scanwindow
			tCur--; // decrease t
		} else {
			sCur++; // detectDeparture gets called every time a new scan window comes in, thus incrementing sCur here. 
					// (sCur is also counter for determining how many scans since stay)
			updateHistory(scanWindow);
			tCur = tMax;
		}
		return (tCur == 0) ? true : false;
	}

	/**
	 * Returns true, if the argument scan window is a subset of curHistory or also if curHistory is empty (in the less efficient
	 * version).
	 * @param sW
	 * @return 
	 */
	private boolean isSubsetOfHistory(ScanWindow sW) {
		if (curHistory.isEmpty()) return true; // TODO just comment out this condition for efficient variant.
		boolean res = true;
		for (ArrayList<String> aL : sW.getScanWindow()) {
			if (!curHistory.keySet().containsAll(aL)) {
				res = false;
				break;
			}
		}
		return res;
	}

	/**
	 * Returns the uniquely seen access points of scan window.
	 * @param sW - scan window to examine
	 * @return - HashSet of strings of access points
	 */
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
	 * Gets called from detectDeparture and detectEntrance methods. Also can be used to "initialize" a curHistory for the first time. 
	 * When called, it updates the curHistory field variable, which keeps track of seen APs and how often they were seen during a stay
	 * at a place.
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

	/**
	 * This method gets called, when a place is entered. From the first sMax scan windows, it infers the representative beacons,
	 * i.e. beacons with a response rate higher than repThreshold
	 */
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

	/**
	 * From curHistory and sCur (which functions a the counter) it calculates the curFingerprint
	 */
	private void calculateCurFingerprint() {
		curFingerprint.setHistory(new HashMap<String, Integer>(curHistory));
		curFingerprint.setNumScans(sCur);
		for (Entry<String, Integer> accP : curHistory.entrySet()) {
			curFingerprint.getFingerprint().put(accP.getKey(), (double) (accP.getValue()) / sCur);
		}
	}

	/**
	 * Returns index of the best fitting place in knownPlaces
	 * @return
	 */
	private int getIndexOfBestMatchingPlace() {
		int bestIndex = -1;
		double bestMatchingCoefficient = 0;
		
		if(knownPlaces.isEmpty()){
			return bestIndex;
		}
		for(int i = 0; i < knownPlaces.size(); i++){
			double curCoeff = getSimilarityCoefficient(knownPlaces.get(i));
			if(curCoeff >= SMILILARITY_THRESHOLD && curCoeff > bestMatchingCoefficient){
				bestIndex = i;
				bestMatchingCoefficient = curCoeff;
			}
		}
		return bestIndex;
	}
	

	/**
	 * Calculates the similarity of Fingerprint given in the argument to curFingerprint.
	 * @param tmpFP
	 * @return
	 */
	private double getSimilarityCoefficient(Fingerprint tmpFP){
		HashSet<String> seenAPs = new HashSet<String>();
		double maxedValues = 0;
		double minnedValues = 0;
		for(Entry<String, Double> tmpAP : tmpFP.getFingerprint().entrySet()){
			Double curFPValue = curFingerprint.getFingerprint().get(tmpAP.getKey());
			maxedValues += Math.max(tmpAP.getValue(), (curFPValue == null) ? 0 : curFPValue);
			minnedValues += Math.min(tmpAP.getValue(), (curFPValue == null) ? 0 : curFPValue);
			seenAPs.add(tmpAP.getKey());
		}
		for(Entry<String, Double> tmpAP : curFingerprint.getFingerprint().entrySet()){
			if(!seenAPs.contains(tmpAP.getKey())){
				maxedValues += tmpAP.getValue();
			}
		}
		
		return (minnedValues / maxedValues);
	}
	
	/**
	 * When called, merges curFingerprint with Fingerprint from knownPlaces at index i
	 * @param bestIndex
	 */
	private void mergeCurFingerprintWith(int bestIndex) {
		Fingerprint fpFromKnownPlaces = knownPlaces.get(bestIndex);
		fpFromKnownPlaces.addToHistory(curHistory);
		fpFromKnownPlaces.addToNumScansDuringScans(sCur);
		fpFromKnownPlaces.calcFingerprint();
	}

}
