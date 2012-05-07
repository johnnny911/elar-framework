package org.elar.acceleglove;
/**
 * 
 * @author Mark Henderson and James Neilan
 * @version	1.0.0
 * Thesis and Research Work.
 *
    Copyright (C) 2012	James Neilan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import static com.googlecode.javacv.cpp.opencv_core.*;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_ml.CvANN_MLP;
import com.googlecode.javacv.cpp.opencv_ml.*;

public class NeuralNet extends Algorithm{
	private CvANN_MLP ann;
        private boolean train = false;
	/**
         * Constructor for neural net.
         */
	public NeuralNet(){
		super("Neural Net");
		CvMat count = cvCreateMat(1, 2, CV_32SC1);
		count.put(0, GestureData.NUM_POINTS);
		count.put(1, GestureData.NUM_GESTURES);
		ann = new CvANN_MLP(count, CvANN_MLP.SIGMOID_SYM, 1.0);
	}//end constructor
	/**
         * Predict method that accepts unknown data array and matches to 
         * nearest known label
         * @param inputs    unknown symbol data array
         * @return          int value of symbol
         */
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
	}//end predict method
	/**
         * Accepts input and actual data arrays and trains object.
         * @param inputs    input symbol array
         * @param actual    known label that maps to input array
         */
	public void train(GestureData inputs, Gesture actual){
		ann.train(inputs.toCvMat(), actual.toCvMat(), null, null, 
                                                new CvANN_MLP_TrainParams(), 0);		
	}//end train method
        /**
         * Accepts input and actual data arrays and trains object.
         * @param data      CvMAt of input symbol
         * @param labels    CvMat of known labels
         */
        public void train(CvMat data, CvMat labels){
		if(!train){
			ann.train(data, labels, null, null, 
                                                new CvANN_MLP_TrainParams(), 0);
			train = true;
		}
	}//end train method
	/**
         * Save ann data to specified file.
         * @param filename 
         */
	public void save(String filename){
		ann.save(filename, "");
	}//end save method
        /**
         * Predict method that accepts unknown data array and matches to 
         * nearest known label
         * @param inputs    unknown symbol data array
         * @return          int value of nearest symbol
         */
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
	}//end predrict method
}//end NeuralNet class