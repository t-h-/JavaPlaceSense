package de.tudarmstadt.wsn.placesense.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public void clear(){
		numScansDuringStay = 0;
		history.clear();
		fingerprint.clear();
	}
	
	public void addToHistory(Map<String, Integer> toAdd){
		for(Entry<String, Integer> curAP : toAdd.entrySet()){
			Integer tmpEntry = history.get(curAP.getKey());
			history.put(curAP.getKey(), ((tmpEntry != null) ? tmpEntry : 0) + curAP.getValue());
			//TODO TEST
		}
	}	
	
	public void addToNumScansDuringScans(int toAdd){
		numScansDuringStay += toAdd;
	}

	public void calcFingerprint() {
		// TODO Auto-generated method stub
		fingerprint.clear();
		for (Entry<String, Integer> accP : history.entrySet()) {
			fingerprint.put(accP.getKey(), (double) (accP.getValue()) / numScansDuringStay);
		}
	}
}
