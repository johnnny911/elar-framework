package org.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

public class Gesture {
	// Gestures
	private static Map<Integer, Gesture> gestures;
	/*
	public static final Gesture OK, 
								  PEACE,
								  ONE,
								  STOP,
								  THUMBSUP;
	*/
	public static final Gesture ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE;
	
	
	static{
		gestures = new HashMap<Integer, Gesture>();
		/*
		OK = new Gesture("OK", 0); gestures.put(OK.getKey(), OK);
		PEACE = new Gesture("PEACE", 1); gestures.put(PEACE.getKey(), PEACE);
		ONE = new Gesture("ONE", 2); gestures.put(ONE.getKey(), ONE);
		STOP = new Gesture("STOP", 3); gestures.put(STOP.getKey(), STOP);
		THUMBSUP = new Gesture("THUMBSUP", 4); gestures.put(THUMBSUP.getKey(), THUMBSUP);
		*/
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
	
	private Gesture(String name, int key){
		this.name = name;
		this.key = key;
	}
	
	public static Gesture get(int key){
		return gestures.containsKey(key) ? gestures.get(key) : new Gesture("Unknown", key);
	}
	
	public CvMat toCvMat(){
		CvMat outputs = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_POINTS, 
																		1, CV_32FC1);
		for (int i=0; i<GestureData.NUM_GESTURES; i++)
			outputs.put(i, i == key ? 1 : 0);
		
		return outputs;
	}
	
	private String name;
	public int key;
	
	public String getName(){ return name; }
	public int getKey() { return key; }
	public String toString() { return name + " (" + getKey() + ")"; }
	
	
}
