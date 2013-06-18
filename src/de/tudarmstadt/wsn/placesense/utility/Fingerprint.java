package de.tudarmstadt.wsn.placesense.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Wrapper/container class for fingerprints. Mostly self speaking class methods. Fingerprint can get calculated by dividing
 * the number a specific AP was seen by the number of scans during the stay.
 * @author Tomi
 *
 */
public class Fingerprint {

	private int numScansDuringStay = 0;
	private HashMap<String, Double> fingerprint = new HashMap<String, Double>();
	private HashMap<String, Integer> history = new HashMap<String, Integer>();
	
	public Fingerprint() {
		super();
	}

	public Fingerprint(int numScans, HashMap<String, Double> fingerprint, HashMap<String, Integer> history) {
		super();
		this.numScansDuringStay = numScans;
		this.fingerprint = fingerprint;
		this.history = history;
	}
	
	/**
	 * Copy constructor
	 * @param fp
	 */
	public Fingerprint(Fingerprint fp){
		super();
		this.numScansDuringStay = fp.numScansDuringStay;
		this.fingerprint = new HashMap<String,Double>(fp.getFingerprint());
		this.history = new HashMap<String, Integer>(fp.getHistory());
	}

	public int getNumScans() {
		return numScansDuringStay;
	}

	public void setNumScans(int numScans) {
		this.numScansDuringStay = numScans;
	}

	public HashMap<String, Double> getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(HashMap<String, Double> fingerprint) {
		this.fingerprint = fingerprint;
	}

	public HashMap<String, Integer> getHistory() {
		return history;
	}

	public void setHistory(HashMap<String, Integer> history) {
		this.history = history;
	}
	
	/**
	 * Clears the current instance of the Fingerprint
	 */
	public void clear(){
		numScansDuringStay = 0;
		history.clear();
		fingerprint.clear();
	}
	
	/**
	 * Adds parameter map to history of fingerprint, called during merging.
	 * @param toAdd
	 */
	public void addToHistory(Map<String, Integer> toAdd){
		for(Entry<String, Integer> curAP : toAdd.entrySet()){
			Integer tmpEntry = history.get(curAP.getKey());
			history.put(curAP.getKey(), ((tmpEntry != null) ? tmpEntry : 0) + curAP.getValue());
		}
	}	
	
	/**
	 * When called during merging, it adds the argument number of scans to its cur number of scans during stay
	 * @param toAdd
	 */
	public void addToNumScansDuringScans(int toAdd){
		numScansDuringStay += toAdd;
	}

	/**
	 * Calculates fingerprint from history and number of scans during stay.
	 */
	public void calcFingerprint() {
		fingerprint.clear();
		for (Entry<String, Integer> accP : history.entrySet()) {
			fingerprint.put(accP.getKey(), (double) (accP.getValue()) / numScansDuringStay);
		}
	}
}
