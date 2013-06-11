package de.tudarmstadt.wsn.placesense.utility;

import java.util.HashMap;

public class Fingerprint {

	private int numScans = 0;
	private HashMap<String, Double> fingerprint = new HashMap<String, Double>();
	private HashMap<String, Integer> history = new HashMap<String, Integer>();
	
	public Fingerprint() {
		
	}

	public Fingerprint(int numScans, HashMap<String, Double> fingerprint, HashMap<String, Integer> history) {
		super();
		this.numScans = numScans;
		this.fingerprint = fingerprint;
		this.history = history;
	}

	public int getNumScans() {
		return numScans;
	}

	public void setNumScans(int numScans) {
		this.numScans = numScans;
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

}
