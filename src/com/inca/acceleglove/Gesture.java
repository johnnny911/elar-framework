package com.inca.acceleglove;


public class Gesture {
	public static final int NUM_POINTS = 18;
	private int[] data;
	
	enum GesturePoint{
		THUMB_X, THUMB_Y, THUMB_Z,
		INDEX_X, INDEX_Y, INDEX_Z,
		MIDDLE_X, MIDDLE_Y, MIDDLE_Z,
		RING_X, RING_Y, RING_Z,
		PITCH_X, PITCH_Y, PITCH_Z,
		PA_X, PA_Y, PA_Z
		}
	
	public Gesture(String str){
		String[] split = str.split(",");
		for (int i=0; i<split.length; i++)
			data[i] = Integer.parseInt(split[i]);
	}
	
	public int get(GesturePoint gp){
		return data[gp.ordinal()];
	}
	
	public int[] getData(){ 
		return data; 
	}
}
