package com.inca.acceleglove;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_NN;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import com.googlecode.javacv.cpp.opencv_ml.CvANN_MLP;
import com.googlecode.javacv.cpp.opencv_ml.*;

public class NeuralNet{
	private CvANN_MLP ann;
	
	public NeuralNet(){
		CvMat count = cvCreateMat(1, 2, CV_32SC1);
		count.put(0, GestureData.NUM_POINTS);
		count.put(1, GestureData.NUM_GESTURES);
		ann = new CvANN_MLP(count, CvANN_MLP.SIGMOID_SYM, 1.0);
	}
	
	public int predict(GestureData g){
		double[] data = g.getData();
		CvMat inputs = cvCreateMat(1, data.length, CV_32FC1);
		
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
		ann.predict(inputs, outputs);
		
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
	
	public void train(GestureData g, Gesture actual){
		double[] data = g.getData();
		CvMat inputs = cvCreateMat(1, data.length, CV_32FC1);
		
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
		for (int i=0; i<GestureData.NUM_GESTURES; i++)
			outputs.put(i, i == actual.getKey() ? 1 : 0);
		
		ann.train(inputs, outputs, null, null, new CvANN_MLP_TrainParams(), 0);		
	}
	
	public void save(String filename){
		ann.save(filename, "");
	}
}