package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;

public class Main {
	
	private static String[][] scans = {{"x", "y", "z"}, {"x", "y", "z", "a", "b"}, {"a", "b", "c", "d"}, {"a", "c", "d"}, {"a", "b", "c"}, {"a", "c", "d"}, 
			{"a", "b", "c", "d"}, {"a", "c", "e"}, {"a", "c", "d"}, {"x", "y", "q"}, {"x", "y", "z"}, {"x", "y", "z"}};

	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String[]> scanList = UtilityMethods.convert2DArrayTo2DCollection(scans);
		
		PlaceSenseCore psc = new PlaceSenseCore(scanList);
		psc.executePlaceSense();

	}



}
