package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

enum GesturePoint{
	THUMB_X, THUMB_Y, THUMB_Z,
	INDEX_X, INDEX_Y, INDEX_Z,
	MIDDLE_X, MIDDLE_Y, MIDDLE_Z,
	RING_X, RING_Y, RING_Z,
	PITCH_X, PITCH_Y, PITCH_Z,
	PA_X, PA_Y, PA_Z
	}

public class GestureData {
	public static final int NUM_POINTS = 18;
	public static final int NUM_GESTURES = 5;
	public static final int NUM_GESTURES_TRAIN_EACH = 50;
	public static final int NUM_GESTURES_TRAIN = NUM_GESTURES_TRAIN_EACH*2;
	private double[] data;

	public GestureData(String str){
		data = new double[NUM_POINTS];
		String[] split = str.split(" ");
		for (int i=0; i<split.length; i++)
			data[i] = Integer.parseInt(split[i]);
	}
	
	public double get(GesturePoint gp){
		return data[gp.ordinal()];
	}
	
	public double[] getData(){ 
		return data; 
	}
	
	public void set(int ix, double elem){
		data[ix] = elem;
	}
	
	public CvMat toCvMat(){
		CvMat inputs = cvCreateMat(NUM_POINTS*NUM_GESTURES, data.length, CV_32FC1);
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		return inputs;
	}
	public CvMat toCvMat1(){
		CvMat inputs = cvCreateMat(1, data.length, CV_32FC1);
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		return inputs;
	}
	
}