/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inca.main;

import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.idrt.Glove;
import com.idrt.Handshape;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JTable;
import org.inca.acceleglove.*;
import org.inca.decision.*;
import org.openide.util.Exceptions;

/**
 *
 * @author borotech
 */
public class GloveRecog {
    private String[] compAns = new String[3];
    private String ensAns = new String();
    private static final int ALPH_SIZE = 10;
    private static final String TRAIN_PATH = "databases\\";
    private JTable gestureTable;
    
    public GloveRecog(JTable gestureTable){
        this.gestureTable = gestureTable;
    }
    
    public void recognize(String ensemble, Glove glove, Handshape hand){
        PerformanceMatrix pm1 = new PerformanceMatrix("GlKNNcv1", ALPH_SIZE);
        PerformanceMatrix pm2 = new PerformanceMatrix("GlSVMcv1", ALPH_SIZE);
        IncaDecision run = new IncaDecision();
        KNearest knn = new KNearest(5);
        SupportVM svm = new SupportVM();
        NeuralNet ann = new NeuralNet();
        GestureData hOut;
        opencv_core.CvMat hArry;
        
        try {
            //train
            train(knn, svm, ann);
            //knn.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
            //svm.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        
        try{
            hand = glove.captureHandshape();
            hOut = new GestureData(hand.toString());
            hArry = hOut.toCvMat();
            for(int i = 0; i < hOut.NUM_POINTS; i++){
                //gestureTable.getModel().setValueAt(hArry.get(i), 0, i);
            }
                
            //get the component answers
            int guessKNN = knn.predict(hArry);
            CvMat hArry2 = hOut.toCvMat1();//need to fix this
            int guessSVM = svm.predict(hArry2);
            //int guessANN = ann.predict(hArry);
            runEnsemble(run, pm1, pm2, null, guessKNN, guessSVM, 0, ensemble);
            compAns[0] = Gesture.get(guessKNN).toString();
            compAns[1] = ""+0;//Gesture.get(guessANN).toString();
            compAns[2] = Gesture.get(guessSVM).toString();
            
    	}catch(Exception e){
            e.printStackTrace();
    	}	
    
        //get ensemble answer
        ensAns = run.getEnsAns();
    }
    
    private void train(KNearest knn, SupportVM svm, NeuralNet ann) throws Exception{
        //AlgFrame support = new AlgFrame();
        knn.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
	svm.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
        //ann.train(support.getKnnData(TRAIN_PATH+"set1.txt"), support.getClassLabels());
    }
    
    public String[] getCompAns(){
        return compAns;
    }
    public String getEnsAns(){
        return ensAns;
    }
    
    private void runEnsemble(IncaDecision run, PerformanceMatrix pm1, PerformanceMatrix pm2,
            PerformanceMatrix pm3, int alg1Guess, int alg2Guess, int alg3Guess,  
                                                               String ensemble){
		pm1.getDatabase(TRAIN_PATH+pm1.getName()+".xml");
		pm2.getDatabase(TRAIN_PATH+pm2.getName()+".xml");
                //used for batch testing
		//IncaQuery q1 = new IncaQuery(""+al1.predict(knnDt), pm1, run.symboltable);
		//IncaQuery q2 = new IncaQuery(""+al2.predict(svmDt), pm2, run.symboltable);
		IncaQuery q1 = new IncaQuery(""+alg1Guess, pm1, run.symboltable);
		IncaQuery q2 = new IncaQuery(""+alg2Guess, pm2, run.symboltable);
		ConfidenceVector cv1 = q1.getCV();
		ConfidenceVector cv2 = q2.getCV();
		
		List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
		cvSet.add(cv1);
		cvSet.add(cv2);
		
		//ensemble performance tracking
		//PerformanceMatrix pm5 = new PerformanceMatrix("ESMBLGLdsc", run.symboltable.getSize());
		Ensemble output = new Ensemble(cvSet, ensemble);
                //System.out.println(output.getDecision());
                ensAns = run.translateDecision(output.getDecision());
		//run.updatePM(pm6, ""+output.getDecision());
		//pm6.saveDatabase(pm6.getName()+".xml");
		//int out = output.getDecision();	
		//run.writeOutput("Choice: " + run.translateDecision(out)+" " +out+"\n");
		//System.out.println("Choice: " + run.translateDecision(output.getDecision()));
		
    }
    
    public opencv_core.CvMat getKnnData(String filename) throws Exception{
    	opencv_core.CvMat data = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH, 
    							GestureData.NUM_POINTS, CV_32FC1);
    	
    	File inFile = new File(filename);
		Scanner in = new Scanner(inFile);
		int line;
		
    	for(int i = 0; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i++){
    		for(int j = 0; j < GestureData.NUM_POINTS; j++){
    			if(in.hasNext()){
    				line = in.nextInt();
    				data.put(i, j, line);
    			}
    		}
    	}
    	in.close();
    	/*
    	inFile = new File("glovesetmark");
		in = new Scanner(inFile);
		line=0;
    	for(int i = GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH*2; i++){
    		for(int j = 0; j < GestureData.NUM_POINTS; j++){
    			if(in.hasNext()){
    				line = in.nextInt();
    				data.put(i, j, line);
    			}
    		}
    	}
    	in.close();
    	*/
    	return data;
    }
    
    public CvMat getClassLabels(){
    	CvMat labels = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH, 1, CV_32FC1);
    	for(int i = 0; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i++){
    		
    		if(i>=0 && i<149)	labels.put(i, Gesture.ZERO.getKey());
    		if(i>=149 && i<299)	labels.put(i, Gesture.ONE.getKey());
    		if(i>=299 && i<449)	labels.put(i, Gesture.TWO.getKey());
    		if(i>=449 && i<599)	labels.put(i, Gesture.THREE.getKey());
    		if(i>=599 && i<749)	labels.put(i, Gesture.FOUR.getKey());
    		if(i>=749 && i<899)	labels.put(i, Gesture.FIVE.getKey());
    		if(i>=899 && i<1049)	labels.put(i, Gesture.SIX.getKey());
    		if(i>=1049 && i<1199)	labels.put(i, Gesture.SEVEN.getKey());
    		if(i>=1199 && i<1349)	labels.put(i, Gesture.EIGHT.getKey());	
    		if(i>=1349 && i<1499)	labels.put(i, Gesture.NINE.getKey());
    		
    		/* out for cv testing
    		if(i>=0 && i<49)	labels.put(i, Gesture.ZERO.getKey());
    		if(i>=49 && i<99)	labels.put(i, Gesture.ONE.getKey());
    		if(i>=99 && i<149)	labels.put(i, Gesture.TWO.getKey());
    		if(i>=149 && i<199)	labels.put(i, Gesture.THREE.getKey());
    		if(i>=199 && i<249)	labels.put(i, Gesture.FOUR.getKey());
    		if(i>=249 && i<299)	labels.put(i, Gesture.FIVE.getKey());
    		if(i>=299 && i<349)	labels.put(i, Gesture.SIX.getKey());
    		if(i>=349 && i<399)	labels.put(i, Gesture.SEVEN.getKey());
    		if(i>=399 && i<449)	labels.put(i, Gesture.EIGHT.getKey());	
    		if(i>=449 && i<499)	labels.put(i, Gesture.NINE.getKey());*/
    	}
    	return labels;
    }
    
}//end GloveRecog class