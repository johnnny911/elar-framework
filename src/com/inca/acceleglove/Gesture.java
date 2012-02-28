package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

public class Gesture {
	// Gestures
	private static Map<Integer, Gesture> gestures;
	public static final Gesture FU, 
								THUMBSUP;
	
	static{
		gestures = new HashMap<Integer, Gesture>();
		
		FU = new Gesture("FU", 0); gestures.put(FU.getKey(), FU);
		THUMBSUP = new Gesture("THUMBSUP", 1); gestures.put(THUMBSUP.getKey(), THUMBSUP);
	}
	
	private Gesture(String name, int key){
		this.name = name;
		this.key = key;
	}
	
	public static Gesture get(int key){
		return gestures.containsKey(key) ? gestures.get(key) : new Gesture("Unknown", key);
	}
	
	public CvMat toCvMat(){
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
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
