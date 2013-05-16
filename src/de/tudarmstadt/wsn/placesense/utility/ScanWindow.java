package de.tudarmstadt.wsn.placesense.utility;

import java.util.ArrayList;

public class ScanWindow {
	private ArrayList<ArrayList<String>> scanWindow;

	public ScanWindow() {
		super();
		this.scanWindow = new ArrayList<ArrayList<String>>();
	}

	public ScanWindow(ArrayList<ArrayList<String>> scanWindow) {
		super();
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
