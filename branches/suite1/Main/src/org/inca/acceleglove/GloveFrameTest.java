package org.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.idrt.Glove;
import com.idrt.GloveConnectionEvent;
import com.idrt.GloveConnectionListener;
import com.idrt.Handshape;
import org.inca.algorithms.BPNNetRecg;
import org.inca.algorithms.ContourFeaturesRecg;
import org.inca.decision.AlgorithmAdaptor;
import org.inca.decision.BKS;
import org.inca.decision.ConfidenceVector;
import org.inca.decision.Ensemble;
import org.inca.decision.IncaDecision;
import org.inca.decision.IncaQuery;
import org.inca.decision.PerformanceMatrix;

public class GloveFrameTest implements GloveConnectionListener{
	private AlgFrame frame;
	protected static final int ALPH_SIZE = 10;
	
	public GloveFrameTest()throws Exception{
		frame = new AlgFrame();
		frame.setVisible(true);
		createGlove();
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
			//test.crossVal(4);
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
		BKS bks = new BKS();
		
		String path = "C:\\Users\\borotech\\Documents\\Classes\\datasets\\4_fold_GL\\";
		
		knn.train(frame.getKnnData(path+"set"+rNum+".txt"), frame.getClassLabels());
		svm.train(frame.getKnnData(path+"set"+rNum+".txt"), frame.getClassLabels());
		
		for(int i = 0; i < 10; i++){//10
			for(int j = 0; j < 50; j++){//50
				run = new IncaDecision(null, null, null, null, run.getPrefixName(i));
				hOut = new GestureData(getData(path+"tst"+rNum+".txt", i*50+j));//i*50+j
				//knn
				hArry = hOut.toCvMat();
				//System.out.println(knn.predict(hArry));
				//run.updatePM(pm1, ""+knn.predict(hArry));
				//pm1.saveDatabase(pm1.getName()+".xml");
				//ensemble
				//runEnsemble(run, knn, pm1, hOut.toCvMat(), svm, pm2, hOut.toCvMat1());
				//svm
	    		//hArry = hOut.toCvMat1();
				//run.updatePM(pm2, ""+svm.predict(hArry));
				//pm2.saveDatabase(pm2.getName()+".xml");
	    		//System.out.println(svm.predict(hArry));
				//bks
				trainBKS(run, bks, knn, pm1, hOut.toCvMat(), svm, pm2, hOut.toCvMat1(),
						run.getPrefixName(i));
			}
		}
		
		//run.outputPMs("GlKNNcv"+rNum);
		//run.outputPMs("GlSVMcv"+rNum);
		//run.outputPMs("ESMBLGLdsc");
		
		
		
		for(int i = 1; i < 10; i++){
			for(int j = 0; j < 50; j++){
				run = new IncaDecision(null, null, null, null, run.getPrefixName(i));
				hOut = new GestureData(getData(path+"tst"+rNum+".txt", i*50+j));
			
				hArry = hOut.toCvMat();
				testBKS(run, bks, knn, pm1, hOut.toCvMat(), svm, pm2, hOut.toCvMat1());
			}
		}
		
		run.outputPMs("ESMBLGLbks");
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
		PerformanceMatrix pm6 = new PerformanceMatrix("ESMBLGLdsc", run.symboltable.getSize());
		Ensemble output = new Ensemble(cvSet);
		run.updatePM(pm6, ""+output.getDecision());
		pm6.saveDatabase(pm6.getName()+".xml");
		//int out = output.getDecision();	
		//run.writeOutput("Choice: " + run.translateDecision(out)+" " +out+"\n");
		//System.out.println("Choice: " + run.translateDecision(output.getDecision()));
		
	}
	
	private void trainBKS(IncaDecision run, BKS bks, Algorithm knn, PerformanceMatrix pm1, CvMat knnDt,
			Algorithm svm, PerformanceMatrix pm2, CvMat svmDt, String symbol){

		pm1.getDatabase("GlKNNcv1.xml");
		pm2.getDatabase("GlSVMcv1.xml");
	
		String guesses = ""+knn.predict(knnDt)+svm.predict(svmDt);
		guesses = guesses.trim();
		bks.addTuple(guesses, 2);
		bks.trainSpace(guesses, run.parseIn(symbol));
	}//end trainBKS method
	
	private void testBKS(IncaDecision run, BKS bks, Algorithm knn, PerformanceMatrix pm1, CvMat knnDt,
			Algorithm svm, PerformanceMatrix pm2, CvMat svmDt){
		
		String guesses = ""+knn.predict(knnDt)+svm.predict(svmDt);
		//System.out.println("Tuple: "+guesses);
		//System.out.println("BKS: "+getBKSDecision(guesses, bks));
		PerformanceMatrix pm4 = new PerformanceMatrix("ESMBLGLbks", ALPH_SIZE);
		pm4.getDatabase("BKS.xml");
		run.updatePM(pm4, getBKSDecision(guesses,bks));
		pm4.saveDatabase(pm4.getName()+".xml");
	}//end testBKS method
	
	private String getBKSDecision(String tuple, BKS bks){
		
		return bks.getGuess(tuple);
	}

}//end GloveGrameTest class
