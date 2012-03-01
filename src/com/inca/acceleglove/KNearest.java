package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.*;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_ml.CvKNearest;

public class KNearest extends Algorithm{
	private CvKNearest knn;
	
	KNearest(){
		super("K Nearest Neighbor");
		CvMat count = cvCreateMat(1, 2, CV_32SC1);
		count.put(0, GestureData.NUM_POINTS);
		count.put(1, GestureData.NUM_GESTURES);
		CvMat inputs = cvCreateMat(1, GestureData.NUM_POINTS, CV_32FC1);
		CvMat outputs = cvCreateMat(1, GestureData.NUM_POINTS, CV_32FC1);
		knn = new CvKNearest(inputs, outputs, null, false, 10);
	}
	
	public int predict(GestureData inputs){
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
		float f = knn.find_nearest(inputs.toCvMat(), 10, null, null, outputs, null);
		
		int max = 0;
		double maxConf = 0.0;
		for (int i=0; i<GestureData.NUM_GESTURES; i++){
			if (outputs.get(i) > maxConf){
				maxConf = outputs.get(i);
				max = i;
			}
		}
		return max;
	}
	
	public void train(GestureData inputs, Gesture actual){
		CvMat zero = cvCreateMat(1,1, CV_32SC1);
		zero.put(0, 0);
		knn.train(inputs.toCvMat(), actual.toCvMat(), zero, false, 32, false);
	}
}
