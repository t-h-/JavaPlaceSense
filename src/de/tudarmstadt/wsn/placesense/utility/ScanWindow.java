package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;

/**
 * Container class with not much functionallity. Makes the code more readable because the algorithm would else use 
 * "lists of lists of lists". here "lists of lists" get encapsulated.
 * @author Tomi
 *
 */
public class ScanWindow {
	private ArrayList<ArrayList<String>> scanWindow;

	public ScanWindow() {
		this.scanWindow = new ArrayList<ArrayList<String>>();
	}

	public ScanWindow(ArrayList<ArrayList<String>> scanWindow) {
		this.scanWindow = scanWindow;
	}

	public void addScan(ArrayList<String> arrayList) {
		scanWindow.add(arrayList);
	}

	public ArrayList<ArrayList<String>> getScanWindow() {
		return scanWindow;
	}

	public void setScanWindow(ArrayList<ArrayList<String>> scanWindow) {
		this.scanWindow = scanWindow;
	}

}
