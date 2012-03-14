package com.inca.acceleglove;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.idrt.Glove;
import com.idrt.GloveConnectionEvent;
import com.idrt.GloveConnectionListener;
import com.idrt.Handshape;
import com.inca.main.IncaDecision;
import com.inca.main.PerformanceMatrix;

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
		
		knn.train(frame.getKnnData("gloveset"), frame.getClassLabels());
		svm.train(frame.getKnnData("gloveset"), frame.getClassLabels());
		
		for(int i = 0; i < 51; i++){
			for(int j = 0; j < 10; j++){
				run = new IncaDecision(null, null, null, null, run.getPrefixName(j));
				hOut = new GestureData(getData("glovesetjhn", i));
				hArry = hOut.toCvMat();
				
				run.updatePM(pm1, ""+knn.predict(hArry));
				pm1.saveDatabase(pm1.getName()+".xml");
				
	    		hArry = hOut.toCvMat1();
				run.updatePM(pm2, ""+svm.predict(hArry));
				pm2.saveDatabase(pm2.getName()+".xml");
			}
		}
		
		run.outputPMs("GlKNNcv1");
		run.outputPMs("GlSVMcv1");		
	}
	
	private String getData(String filename, int indx)throws Exception{
		String out = new String();
		
		CvMat set = frame.getKnnData(filename);
		for(int i = 0; i < 18; i++){
			out = out.concat((int)set.get(indx, i)+" ");
		}
		
		return out;
	}
	
}//end GloveGrameTest class
