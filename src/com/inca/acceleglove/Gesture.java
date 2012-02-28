package com.inca.acceleglove;

import java.util.HashMap;
import java.util.Map;

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
		return gestures.get(key);
	}
	
	private String name;
	public int key;
	
	public String getName(){ return name; }
	public int getKey() { return key; }
	public String toString() { return name + " (" + getKey() + ")"; }
	
	
}
