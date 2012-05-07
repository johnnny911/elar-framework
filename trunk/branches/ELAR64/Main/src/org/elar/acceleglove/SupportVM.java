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
import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.CV_32SC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvTermCriteria;
import com.googlecode.javacv.cpp.opencv_ml.CvKNearest;
import com.googlecode.javacv.cpp.opencv_ml.CvSVM;
import com.googlecode.javacv.cpp.opencv_ml.CvSVMParams;

public class SupportVM extends Algorithm{
	private CvSVM svmClassifier;
	private boolean train = false;
	private CvSVMParams params;
	/**
         * Constructor for SVM
         */
	public SupportVM(){
		super("SVM");
		CvTermCriteria crit = new CvTermCriteria(1);
		crit.max_iter(100);
		crit.epsilon(0.0001);
		crit.type(1);
		params = new CvSVMParams();
		params.kernel_type(CvSVM.LINEAR);
		params.svm_type(CvSVM.C_SVC);
		params.C(1);
		params.term_crit(crit);
		svmClassifier = new CvSVM();
	}//end constructor method
	/**
         * Predict method that accepts unknown data array and matches to 
         * nearest known label
         * @param inputs    unknown symbol data array
         * @return          int value of nearest symbol
         */
	@Override
	public int predict(CvMat inputs){
		CvMat outputs = null;
		//find closest match
		float f = svmClassifier.predict(inputs, false);
		
		int max = 0;
		double maxConf = 0.0;
		for (int i=0; i<GestureData.NUM_GESTURES; i++){
			//if (outputs.get(i) > maxConf){
			//	maxConf = outputs.get(i);
			//	max = i;
			//}
		}
		return (int)f;
	}//end predict method
	/**
         * Accepts input and actual data arrays and trains object.
         * @param data      CvMAt of input symbol
         * @param labels    CvMat of known labels
         */
	public void train(CvMat data, CvMat labels){
		
		if(!train){
			svmClassifier.train(data, labels, null, null, params);
			train = true;
		}
	}//end train emthod
        
	@Override
	public void train(GestureData inputs, Gesture actual) {
		// TODO Auto-generated method stub
		
	}//end train method
}//end SVM class

