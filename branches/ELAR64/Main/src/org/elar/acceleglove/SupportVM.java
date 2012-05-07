package org.elar.acceleglove;

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

	}
	
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
	}
	
	public void train(CvMat data, CvMat labels){
		
		if(!train){
			svmClassifier.train(data, labels, null, null, params);
			train = true;
		}
	}


	@Override
	public void train(GestureData inputs, Gesture actual) {
		// TODO Auto-generated method stub
		
	}

}//end SVM class

