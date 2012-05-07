package org.elar.acceleglove;
/**
 * 
 * @author Mark Henderson and James Neilan
 * @version	1.0.0
 * Thesis and Research Work.
 *
    Copyright (C) 2012	James Neilan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

public class Gesture {
	// Gestures
	private static Map<Integer, Gesture> gestures;
	public static final Gesture ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE;
	
	//static block for gesture set up and mapping.
	static{
		gestures = new HashMap<Integer, Gesture>();
		ZERO = new Gesture("ZERO", 0); gestures.put(ZERO.getKey(), ZERO);
		ONE = new Gesture("ONE", 1); gestures.put(ONE.getKey(), ONE);
		TWO = new Gesture("TWO", 2); gestures.put(TWO.getKey(), TWO);
		THREE = new Gesture("THREE", 3); gestures.put(THREE.getKey(), THREE);
		FOUR = new Gesture("FOUR", 4); gestures.put(FOUR.getKey(), FOUR);
		FIVE = new Gesture("FIVE", 5); gestures.put(FIVE.getKey(), FIVE);
		SIX = new Gesture("SIX", 6); gestures.put(SIX.getKey(), SIX);
		SEVEN = new Gesture("SEVEN", 7); gestures.put(SEVEN.getKey(), SEVEN);
		EIGHT = new Gesture("EIGHT", 8); gestures.put(EIGHT.getKey(), EIGHT);
		NINE = new Gesture("NINE", 9); gestures.put(NINE.getKey(), NINE);
	}
	/**
         * Constructor. Accepts name and key value for ampping.
         * @param name  name of gesture
         * @param key   key for key/value mapping
         */
	private Gesture(String name, int key){
		this.name = name;
		this.key = key;
	}//end constructor method
	/**
         * Returns value of gey object in gesture map.
         * @param key   key in key/value pair
         * @return object value in map
         */
	public static Gesture get(int key){
		return gestures.containsKey(key) ? gestures.get(key) : new Gesture("Unknown", key);
	}//end get method
	/**
         * Returns a CvMAt object for rec. algorithms
         * @return outputs  CvMAt object for rec. algorithms.
         */
	public CvMat toCvMat(){
		CvMat outputs = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_POINTS, 
																		1, CV_32FC1);
		for (int i=0; i<GestureData.NUM_GESTURES; i++)
			outputs.put(i, i == key ? 1 : 0);
		
		return outputs;
	}//end toCvMat method
	//class variables
	private String name;
	public int key;
	
	public String getName(){ return name; }
	public int getKey() { return key; }
	public String toString() { return name + " (" + getKey() + ")"; }
}//end Gesture class
