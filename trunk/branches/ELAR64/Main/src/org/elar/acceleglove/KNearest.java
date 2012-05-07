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
import com.googlecode.javacv.cpp.opencv_ml.CvKNearest;

public class KNearest extends Algorithm{
	private CvKNearest knn;
	private int k;
	private boolean train = false;
	/**
         * Contructor for algorithm
         * @param k k value for k nearest neighbor
         */
	public KNearest(int k){
		super("K Nearest Neighbor");
		this.k = k;
	}//end constructor mwthod
	
	/**
         * Predict method that accepts unknown data array and matches to 
         * nearest known label
         * @param inputs    unknown symbol data array
         * @return          int value of nearest symbol
         */
	@Override
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
	}//end predict method
	/**
         * Accepts input and actual data arrays and trains object.
         * @param inputs    input symbol array
         * @param actual    known label that maps to input array
         */
	public void train(GestureData inputs, Gesture actual){
		CvMat zero = cvCreateMat(1,1, CV_32SC1);
		zero.put(0, 0);
		//knn.train(inputs.toCvMat(), actual.toCvMat(), zero, false, 32, false);
		knn = new CvKNearest(inputs.toCvMat(), actual.toCvMat(), null, false, k);
	}//end train method
	/**
         * Accepts input and actual data arrays and trains object.
         * @param data      CvMAt of input symbol
         * @param labels    CvMat of known labels
         */
	public void train(CvMat data, CvMat labels){
		if(!train){
			knn = new CvKNearest(data, labels, null, false, k);
			train = true;
		}

	}//end train method
}//end KNearest class
