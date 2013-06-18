package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tudarmstadt.wsn.placesense.utility.ScanWindow;
import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;


/**
 * Main class to execute the algorithm
 * @author Tomi
 *
 */
public class Main {

	private static String[][] scans1 = { { "x", "y", "z" }, { "x", "y", "z", "a", "b" }, { "a", "b", "c", "d" }, { "a", "c", "d" },
			{ "a", "b", "c" }, { "a", "c", "d" }, { "a", "c", "d" }, { "a", "b", "c" }, { "a", "c", "d" }, { "a", "c", "d" }, { "a", "b", "c" },
			{ "a", "c", "d" }, { "a", "b", "c", "d" }, { "a", "c", "e" }, { "a", "c", "d" }, { "x", "y", "q" }, { "x", "y", "z" }, { "x", "y", "z" } };

	private static String[][] scans2 = {
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "za" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b" }, { "a", "b" }, { "a", "b" }, { "a", "b" },
		{ "a" }, { "a" }, { "a" }, { "a" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y" }, { "x", "y"}, { "x", "y" }, { "x", "y" },
		{ "x", "z" }, { "x",  "z" }, { "x", "z" }, { "x", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "z" }, { "x", "z" }, { "x","z" }, { "x", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b" }, { "a", "b" }, { "a", "b" }, { "a", "b" },
		{ "a" }, { "a" }, { "a" }, { "a" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b"}, { "a", "b" }, { "a", "b"}, { "a", "b" },
		{ "a" }, { "a" }, { "a"}, { "a"},
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "a", "b", "c" }, { "a", "b" }, { "a", "b" }, { "a", "b" },
		{ "a", "b", "b" }, { "a", "b" }, { "a", "b" }, { "a", "b" }, // x vs b
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "x", "y" }, { "x", "y" }, { "x" }, { "x", "y" },
		{ "x" }, { "x" }, { "x" }, { "x", "y" },
		{ "x", "y", "z" }, { "x", "y", "z" }, { "z" }, { "x", "y", "z" },
		{ "x", "y" }, { "y", "z" }, { "x", "y", "z" }, { "x", "y", "z" },
		{ "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" }, { "a", "b", "c" },
		{ "a", "b" }, { "a", "b" }, { "a", "b" }, { "a", "b" },
		{ "a" }, { "a" }, { "a" }, { "a" }

	};

	public Main() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int sMax = 3;
		int tMax = 3;
		double sampleRate = 0.1;
		int windowSize = 40;
		int numScansPerWindow = (int) (sampleRate * windowSize);
		boolean useOverlapping = false;
		double repThreshold = 0.7;
		List<ArrayList<String>> scanList = UtilityMethods.convert2DArrayTo2DCollection(scans2);
		ArrayList<ScanWindow> scanWindowList = UtilityMethods.createScanWindowList(scanList, numScansPerWindow, useOverlapping);

		PlaceSenseCore psc = new PlaceSenseCore(scanWindowList, sMax, tMax, repThreshold);
		psc.executePlaceSense();

	}

}
