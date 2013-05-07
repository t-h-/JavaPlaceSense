package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
		List<ArrayList<String>> scanList = convertToCollection(scans);
		PlaceSenseCore psc = new PlaceSenseCore(scanList);
		psc.executePlaceSense();

	}

	private static List<ArrayList<String>> convertToCollection(String[][] argScans) {
		List<ArrayList<String>> resultingList = new ArrayList<ArrayList<String>>();
		List<String[]> tmpList = Arrays.asList(argScans);
		for(String[] str : tmpList){
			resultingList.add(new ArrayList<String>(Arrays.asList(str)));
		}
		return resultingList;
	}

}
