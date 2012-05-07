package org.elar.acceleglove;
import static com.googlecode.javacv.cpp.opencv_core.*;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_ml.CvANN_MLP;
import com.googlecode.javacv.cpp.opencv_ml.*;

public class NeuralNet extends Algorithm{
	private CvANN_MLP ann;
        private boolean train = false;
	
	public NeuralNet(){
		super("Neural Net");
		CvMat count = cvCreateMat(1, 2, CV_32SC1);
		count.put(0, GestureData.NUM_POINTS);
		count.put(1, GestureData.NUM_GESTURES);
		ann = new CvANN_MLP(count, CvANN_MLP.SIGMOID_SYM, 1.0);
	}
	
	public int predict(GestureData inputs){
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
		float f = ann.predict(inputs.toCvMat(), outputs);
		
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
		ann.train(inputs.toCvMat(), actual.toCvMat(), null, null, 
                                                new CvANN_MLP_TrainParams(), 0);		
	}
        public void train(CvMat data, CvMat labels){
		if(!train){
			ann.train(data, labels, null, null, 
                                                new CvANN_MLP_TrainParams(), 0);
			train = true;
		}
	}
	
	public void save(String filename){
		ann.save(filename, "");
	}

	@Override
	public int predict(CvMat inputs) {
		CvMat outputs = cvCreateMat(1, GestureData.NUM_GESTURES, CV_32FC1);
		float f = ann.predict(inputs, outputs);
		
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
}