package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.*;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_ml.CvKNearest;

public class KNearest extends Algorithm{
	private CvKNearest knn;
	private int k;
	
	KNearest(int k){
		super("K Nearest Neighbor");
		//CvMat count = cvCreateMat(1, 2, CV_32SC1);
		//count.put(0, GestureData.NUM_POINTS);
		//count.put(1, GestureData.NUM_GESTURES);
		//int size = GestureData.NUM_GESTURES * GestureData.NUM_POINTS;
		this.k = k;
		//CvMat inputs = cvCreateMat(size, GestureData.NUM_POINTS, CV_32FC1);
		//CvMat outputs = cvCreateMat(size, 1, CV_32FC1);
		//knn = new CvKNearest(inputs, outputs, null, false, 10);
	}
	
	
	
	public int predict(CvMat inputs/*GestureData inputs*/){
		CvMat outputs = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_POINTS, 
																	k, CV_32FC1);
		float f = knn.find_nearest(inputs, k, null, null, outputs, null);
		
		int max = 0;
		double maxConf = 0.0;
		for (int i=0; i<GestureData.NUM_GESTURES; i++){
			if (outputs.get(i) > maxConf){
				maxConf = outputs.get(i);
				max = i;
			}
		}
		return (int)f;
	}
	
	public void train(GestureData inputs, Gesture actual){
		CvMat zero = cvCreateMat(1,1, CV_32SC1);
		zero.put(0, 0);
		//knn.train(inputs.toCvMat(), actual.toCvMat(), zero, false, 32, false);
		knn = new CvKNearest(inputs.toCvMat(), actual.toCvMat(), null, false, k);
	}
	
	public void train(CvMat data, CvMat labels){
		knn = new CvKNearest(data, labels, null, false, k);
	}
}
