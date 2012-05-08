/**
 * 
 * @author James Neilan
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
package org.elar.main;

import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.idrt.Glove;
import com.idrt.Handshape;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JTable;
import org.elar.acceleglove.*;
import org.elar.decision.*;
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
    private KNearest knn = new KNearest(5);
    private SupportVM svm = new SupportVM();
    private NeuralNet ann = new NeuralNet();
    private ConfidenceVector cv1, cv2, cv3;
    private List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
    /**
     * 
     * @param gestureTable 
     */
    public GloveRecog(JTable gestureTable){
        this.gestureTable = gestureTable;
        try {
            //train
            train(knn, svm, ann);
            doPause(10);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    /**
     * 
     * @param ensemble
     * @param glove
     * @param hand 
     */
    public void recognize(String ensemble, Glove glove, Handshape hand){
        PerformanceMatrix pm1 = new PerformanceMatrix("GlKNNcv1", ALPH_SIZE);
        PerformanceMatrix pm2 = new PerformanceMatrix("GlSVMcv1", ALPH_SIZE);
        PerformanceMatrix pm3 = new PerformanceMatrix("GlANNcv1", ALPH_SIZE);
        IncaDecision run = new IncaDecision();
        
        GestureData hOut;
        opencv_core.CvMat hArry;
        
        try{
            //hand = glove.captureHandshape();
            hOut = new GestureData(hand.toString());
            hArry = hOut.toCvMat();
            //get the component answers
            int guessKNN = knn.predict(hArry);
            CvMat hArry2 = hOut.toCvMat1();//need to fix this
            int guessSVM = svm.predict(hArry2);
            int guessANN = ann.predict(hArry2);
            runEnsemble(run, pm1, pm2, pm3, guessKNN, guessSVM, guessANN, ensemble);
            compAns[0] = Gesture.get(guessKNN).toString();
            compAns[1] = Gesture.get(guessSVM).toString();
            compAns[2] = Gesture.get(guessANN).toString();
            
    	}catch(Exception e){
            e.printStackTrace();
    	}	
    }//end recognize method
    /**
     * 
     * @param knn
     * @param svm
     * @param ann
     * @throws Exception 
     */
    private void train(KNearest knn, SupportVM svm, NeuralNet ann) throws Exception{
        //AlgFrame support = new AlgFrame();
        knn.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
	svm.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabels());
        ann.train(getKnnData(TRAIN_PATH+"set1.txt"), getClassLabelsANN());
    }
    /**
     * 
     * @return 
     */
    public String[] getCompAns(){
        return compAns;
    }
    /**
     * 
     * @return 
     */
    public String getEnsAns(){
        return ensAns;
    }
    /**
     * 
     * @param run
     * @param pm1
     * @param pm2
     * @param pm3
     * @param alg1Guess
     * @param alg2Guess
     * @param alg3Guess
     * @param ensemble 
     */
    private void runEnsemble(IncaDecision run, PerformanceMatrix pm1, PerformanceMatrix pm2,
            PerformanceMatrix pm3, int alg1Guess, int alg2Guess, int alg3Guess,  
                                                               String ensemble){
	cvSet.clear();
        pm1.getDatabase(TRAIN_PATH+pm1.getName()+".xml");
        pm2.getDatabase(TRAIN_PATH+pm2.getName()+".xml");
        pm3.getDatabase(TRAIN_PATH+pm3.getName()+".xml");
        //used for batch testing
        //IncaQuery q1 = new IncaQuery(""+al1.predict(knnDt), pm1, run.symboltable);
        //IncaQuery q2 = new IncaQuery(""+al2.predict(svmDt), pm2, run.symboltable);
        IncaQuery q1 = new IncaQuery(""+alg1Guess, pm1, run.symboltable);
        IncaQuery q2 = new IncaQuery(""+alg2Guess, pm2, run.symboltable);
        IncaQuery q3 = new IncaQuery(""+alg3Guess, pm3, run.symboltable);
        cv1 = q1.getCV();
        cv2 = q2.getCV();
        cv3 = q3.getCV(); 


        cvSet.add(cv1);
        cvSet.add(cv2);
        cvSet.add(cv3);

        //ensemble performance tracking
        //PerformanceMatrix pm5 = new PerformanceMatrix("ESMBLGLdsc", run.symboltable.getSize());
        Ensemble output = new Ensemble(cvSet, ensemble);
        //System.out.println(output.getDecision());
        ensAns = run.translateDecision(output.getDecision());
        //System.out.println(ensAns);
        //run.updatePM(pm6, ""+output.getDecision());
        //pm6.saveDatabase(pm6.getName()+".xml");
        //int out = output.getDecision();	
        //run.writeOutput("Choice: " + run.translateDecision(out)+" " +out+"\n");
        //System.out.println("Choice: " + run.translateDecision(output.getDecision()));
		
    }
    /**
     * 
     * @param filename
     * @return
     * @throws Exception 
     */
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
    	return data;
    }
    /**
     * 
     * @return 
     */
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
    	}
    	return labels;
    }
    /**
     * 
     * @return 
     */
    public CvMat getClassLabelsANN(){
        CvMat labels = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH,
                                        GestureData.NUM_GESTURES, CV_32FC1);
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
    	}
    	return labels;
    }
    /**
     * method from http://www.markstechstuff.com/2006/09/java-pause-method.html
     * Thanks Mark.
     * @param iTimeInSeconds 
     */
    public static void doPause(int iTimeInSeconds){
        long t0, t1;
        
        t0=System.currentTimeMillis( );
        t1=System.currentTimeMillis( )+(iTimeInSeconds*1000);
        do {
            t0=System.currentTimeMillis( );
        } while (t0 < t1);
    }//end doPause method
    /**
     * 
     * @return 
     */
    public List<ConfidenceVector> getCVs(){
        return cvSet;
    }
}//end GloveRecog class