package de.tudarmstadt.wsn.placesense.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tudarmstadt.wsn.placesense.utility.UtilityMethods;

public class Main {
	
	private static String[][] scans1 = {{"x", "y", "z"}, {"x", "y", "z", "a", "b"}, {"a", "b", "c", "d"},
		{"a", "c", "d"}, {"a", "b", "c"}, {"a", "c", "d"}, 
		{"a", "c", "d"}, {"a", "b", "c"}, {"a", "c", "d"},{"a", "c", "d"}, {"a", "b", "c"}, {"a", "c", "d"},	
		{"a", "b", "c", "d"}, {"a", "c", "e"}, {"a", "c", "d"}, {"x", "y", "q"}, {"x", "y", "z"}, {"x", "y", "z"}};

	private static String[][] scans2 = 
		{
/*0*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*1*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, 
/*2*/		{"a", "b"}, {"a", "b"}, {"a", "b"}, {"a", "b"}, 
/*3*/		{"a"}, {"a"}, {"a"}, {"a"}, 
/*4*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, 
/*5*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*6*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*7*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*8*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*9*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*10*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, 
/*11*/		{"a", "b"}, {"a", "b"}, {"a", "b"}, {"a", "b"}, 
/*12*/		{"a"}, {"a"}, {"a"}, {"a"}, 
/*13*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, 
/*14*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*15*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*16*/		{"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*17*/		{"a", "b", "c"}, {"a", "b"}, {"a", "b"}, {"a", "b"},
/*18*/		{"a", "b", "b"}, {"a", "b"}, {"a", "b"}, {"a", "b"}, //x vs b
/*19*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"},
/*20*/		{"x", "y"}, {"x", "y"}, {"x"}, {"x", "y"},
/*21*/		{"x"}, {"x"}, {"x"}, {"x", "y"},
/*22*/		{"x", "y", "z"}, {"x", "y", "z"}, {  "z"}, {"x", "y", "z"},
/*23*/		{"x", "y"}, { "y", "z"}, {"x", "y", "z"}, {"x", "y", "z"},
/*24*/		{"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, {"a", "b", "c"}, 
/*25*/		{"a", "b"}, {"a", "b"}, {"a", "b"}, {"a", "b"}, 
/*26*/		{"a"}, {"a"}, {"a"}, {"a"} 

		};
	
	public Main() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<ArrayList<String>> scanList = UtilityMethods.convert2DArrayTo2DCollection(scans2);
		
		PlaceSenseCore psc = new PlaceSenseCore(scanList);
		ArrayList<HashMap<String, Double>> result = psc.executePlaceSense();

	}



}
