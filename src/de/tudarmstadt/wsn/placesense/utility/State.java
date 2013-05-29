package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class State {

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
	private int scansSinceEntry = 0;
	private HashMap<String, Double> fingerprint = new HashMap<String, Double>(); //guess this will not be used
	private HashMap<String, Integer> history = new HashMap<String, Integer>(); //also fingerprint?
	private List<ArrayList<String>> scanList = new ArrayList<ArrayList<String>>();
	private HashSet<String> representativeSet = new HashSet<String>();
	
	public List<Scan> oldWindow;
	public List<Scan> newWindow;
	
	
	public void addNewScan(Scan scan) { 
		
	}
	
	public State() {
		// TODO Auto-generated constructor stub
	}

}
