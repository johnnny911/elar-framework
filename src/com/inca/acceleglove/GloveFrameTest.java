package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.idrt.Glove;
import com.idrt.GloveConnectionEvent;
import com.idrt.GloveConnectionListener;
import com.idrt.Handshape;
import com.inca.main.ConfidenceVector;
import com.inca.main.Ensemble;
import com.inca.main.IncaDecision;
import com.inca.main.IncaQuery;
import com.inca.main.PerformanceMatrix;

public class GloveFrameTest implements GloveConnectionListener{
	private AlgFrame frame;
	protected static final int ALPH_SIZE = 10;
	
	public GloveFrameTest()throws Exception{
		frame = new AlgFrame();
		frame.setVisible(true);
		//createGlove();
	}//end constructor method
	
	private void createGlove()throws Exception{
		Handshape hand = null ;
		Glove glove = new Glove();
		glove.addGloveConnectionListener(this);
		frame.setGestureObject(hand, glove);
	}//end createGlove method
	
	public static void main(String[] args){
		try {
			GloveFrameTest test = new GloveFrameTest();
			test.crossVal(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end main method

	@Override
	public void statusReceived(GloveConnectionEvent event) {
		Glove g = (Glove) event.getSource();
		g.close();
		System.out.println("The Glove is disconnected");
		System.exit(0);
	}
	
	public void crossVal(int rNum)throws Exception{
		PerformanceMatrix pm1 = new PerformanceMatrix("GlKNNcv"+rNum, ALPH_SIZE);
		PerformanceMatrix pm2 = new PerformanceMatrix("GlSVMcv"+rNum, ALPH_SIZE);
		IncaDecision run = new IncaDecision();
		KNearest knn = new KNearest(5);
		SupportVM svm = new SupportVM();
		GestureData hOut;
		CvMat hArry;
		String path = "C:\\Users\\borotech\\Documents\\Classes\\datasets\\4_fold_CV\\";
		
		knn.train(frame.getKnnData(path+"set"+rNum+".txt"), frame.getClassLabels());
		svm.train(frame.getKnnData(path+"set"+rNum+".txt"), frame.getClassLabels());
		
		
		for(int i = 0; i < 10; i++){//10
			for(int j = 0; j < 50; j++){//50
				run = new IncaDecision(null, null, null, null, run.getPrefixName(i));
				hOut = new GestureData(getData(path+"tst"+rNum+".txt", i*50+j));//i*50+j
				//knn
				hArry = hOut.toCvMat();
				run.updatePM(pm1, ""+knn.predict(hArry));
				pm1.saveDatabase(pm1.getName()+".xml");
				//runEnsemble(run, knn, pm1, hOut.toCvMat(), svm, pm2, hOut.toCvMat1());
				//svm
	    		hArry = hOut.toCvMat1();
				run.updatePM(pm2, ""+svm.predict(hArry));
				pm2.saveDatabase(pm2.getName()+".xml");
	    		
			}
		}
		
		run.outputPMs("GlKNNcv"+rNum);
		run.outputPMs("GlSVMcv"+rNum);
		//run.outputPMs("ESMBLGLMaxSum");
	}
	
	private String getData(String filename, int indx)throws Exception{
		String out = new String();
		
		CvMat set = frame.getKnnData(filename);
		for(int i = 0; i < 18; i++){
			out = out.concat((int)set.get(indx, i)+" ");
		}
		
		return out;
	}
	
	private void runEnsemble(IncaDecision run, Algorithm al1, PerformanceMatrix pm1, CvMat knnDt,
							Algorithm al2, PerformanceMatrix pm2, CvMat svmDt){
		pm1.getDatabase(pm1.getName()+".xml");
		pm2.getDatabase(pm2.getName()+".xml");
		IncaQuery q1 = new IncaQuery(""+al1.predict(knnDt), pm1, run.symboltable);
		IncaQuery q2 = new IncaQuery(""+al2.predict(svmDt), pm2, run.symboltable);
		
		ConfidenceVector cv1 = q1.getCV();
		ConfidenceVector cv2 = q2.getCV();
		
		List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
		cvSet.add(cv1);
		cvSet.add(cv2);
		
		//ensemble performance tracking
		PerformanceMatrix pm6 = new PerformanceMatrix("ESMBLGLMaxSum", run.symboltable.getSize());
		Ensemble output = new Ensemble(cvSet);
		run.updatePM(pm6, ""+output.getDecision());
		pm6.saveDatabase(pm6.getName()+".xml");
		//int out = output.getDecision();	
		//run.writeOutput("Choice: " + run.translateDecision(out)+" " +out+"\n");
		//System.out.println("Choice: " + run.translateDecision(output.getDecision()));
		
	}
	
}//end GloveGrameTest class
