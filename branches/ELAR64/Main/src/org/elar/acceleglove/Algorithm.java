package org.elar.acceleglove;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

public abstract class Algorithm {
	private String name;
	
	Algorithm(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract int predict(CvMat inputs);
	public abstract void train(GestureData inputs, Gesture actual);

}
