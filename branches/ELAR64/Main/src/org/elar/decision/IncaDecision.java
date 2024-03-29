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
package org.elar.decision;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import org.elar.algorithms.BPNNetRecg;
import org.elar.algorithms.ContourFeaturesRecg;
import org.elar.algorithms.KNearestNRecg;
import org.elar.algorithms.SVMRecg;
import org.elar.algorithms.TemplateMatching;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.*;

/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * IncaDecision.java - Main class for testing the Inca framework.
 * for a symbol over the sample space.
    Copyright (C) 2011	James Neilan

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
public class IncaDecision {
	private PerformanceMatrix pm1, pm2, pm3, pm4, pm5;
	public Alphabet symboltable;
	private final int SIZE = 10; //regression testing only
	private String unknownSymbol;
	private boolean incrLrn = false;
	private int missedRec = 0;
        private final String DB_PATH = "databases\\";
        private String ensemble;
        private String[] compAns = new String[3];
        private String ensAns = new String();
        List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
	
	/**
	 * Constructor.
	 */
	public IncaDecision(PerformanceMatrix pm1, PerformanceMatrix pm2, 
						PerformanceMatrix pm3,PerformanceMatrix pm4,
						String unknownSymbol){
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		this.pm4 = pm4;
		this.unknownSymbol = unknownSymbol;
		buildSymbolTable();
	}//end default constructor
        public IncaDecision(PerformanceMatrix pm1, PerformanceMatrix pm2, 
				PerformanceMatrix pm3,PerformanceMatrix pm4,
				String unknownSymbol, String ensemble){
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		this.pm4 = pm4;
		this.unknownSymbol = unknownSymbol;
                this.ensemble = ensemble;
		buildSymbolTable();
	}//end default constructor
	/**
	 * 
	 * @param unknownSymbol
	 * @param incLrn
	 */
	public IncaDecision(String unknownSymbol, boolean incLrn){
		this.unknownSymbol = unknownSymbol;
		this.incrLrn = incLrn;
		buildSymbolTable();
	}//end constructor
	
	public IncaDecision(){
		buildSymbolTable();
	}
	public String[] getCompAns(){
            return compAns;
        }
        public String getEnsAns(){
            return ensAns;
        }
        
        /**
	 * Regression testing only.
	 */
	private void buildSymbolTable(){
		symboltable = new Alphabet(SIZE);
		for(int i = 0; i < SIZE; i++){
			symboltable.addSymbol(getPrefixName(i), new Integer(i));
		}
	}//end buildSymbolTable method
	/**
	 * regression testing only. use for independent algorithm testing. 
	 * The combiner will be responsible for updating each performance 
	 * matrix.
	 */
	public void updatePM(PerformanceMatrix pm, String guess){
		//System.out.println(guess);
		if(!guess.equalsIgnoreCase("No Such Tuple in Table.")){
			pm.recordRecNum(symboltable.getSymbolValue(parseIn(unknownSymbol)).intValue()
			 ,symboltable.getSymbolValue(getPrefixName(Integer.parseInt(guess))).intValue());
		}else{
			//for BKS analysis
			missedRec++;
		}
	}//end buildPerfmatrices method
	
	public String parseIn(String input){
		//System.out.println(input);
		if(input.substring(0, 3).equalsIgnoreCase("zer")) return "zero";
		if(input.substring(0, 3).equalsIgnoreCase("one")) return "one";
		if(input.substring(0, 3).equalsIgnoreCase("two")) return "two";
		if(input.substring(0, 3).equalsIgnoreCase("thr")) return "three";
		if(input.substring(0, 3).equalsIgnoreCase("fou")) return "four";
		if(input.substring(0, 3).equalsIgnoreCase("fiv")) return "five";
		if(input.substring(0, 3).equalsIgnoreCase("six")) return "six";
		if(input.substring(0, 3).equalsIgnoreCase("sev")) return "seven";
		if(input.substring(0, 3).equalsIgnoreCase("eig")) return "eight";
		if(input.substring(0, 3).equalsIgnoreCase("nin")) return "nine";
		return "null";
	}
	/**
	 * 
	 */
	public void getIncaResult(){
		IncaQuery q1, q2, q3, q4;
                ConfidenceVector cv1, cv2, cv3, cv4;
                
		cvSet.clear();
                //Contour Features
                if(pm1!=null){
                    AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
                    pm1.getDatabase(DB_PATH+pm1.getName()+".xml");
                    //pm1 = new PerformanceMatrix("CFcv11", symboltable.getSize());
                    //writeOutput("CF: "+getAlgorithmGuess(a1)+" ");
                    //System.out.println("CF: "+getAlgorithmGuess(a1));
                    //updatePM(pm1, getAlgorithmGuess(a1));
                    //pm1.saveDatabase(pm1.getName()+".xml");
                    compAns[0] = getAlgorithmGuess(a1);
                    q1 = new IncaQuery(compAns[0], pm1, symboltable);
                    cv1 = q1.getCV();
                    cvSet.add(cv1);
                }
                //K-Nearest Neighbors
                if(pm2!=null){
                    AlgorithmAdaptor a2 = new AlgorithmAdaptor(new KNearestNRecg(10,10,50));
                    pm2.getDatabase("KNN.xml");
                    //pm2 = new PerformanceMatrix("KNN", symboltable.getSize());
                    //System.out.println("KNN: "+getAlgorithmGuess(a2));
                    //updatePM(pm2, getAlgorithmGuess(a2));
                    //pm2.saveDatabase(pm2.getName()+".xml");
                    q2 = new IncaQuery(compAns[1], pm1, symboltable);
                    cv2 = q2.getCV();
                    cvSet.add(cv2);
                }
                //Artificial Neural Network
                if(pm3!=null){
		
                    AlgorithmAdaptor a3 = new AlgorithmAdaptor(new BPNNetRecg());
                    pm3.getDatabase(DB_PATH+pm3.getName()+".xml");
                    //pm3 = new PerformanceMatrix("ANNcv11", symboltable.getSize());
                    //writeOutput("ANN: "+getAlgorithmGuess(a1)+" ");
                    //System.out.println("ANN: "+getAlgorithmGuess(a3));
                    //updatePM(pm3, getAlgorithmGuess(a3));
                    //pm3.saveDatabase(pm3.getName()+".xml");
                    compAns[2] = getAlgorithmGuess(a3);
                    q3 = new IncaQuery(compAns[2], pm3, symboltable);
                    cv3 = q3.getCV();
                    cvSet.add(cv3);
                }
		if(pm4!=null){
                    AlgorithmAdaptor a4 = new AlgorithmAdaptor(new SVMRecg());
                    pm4 = new PerformanceMatrix("SVM", symboltable.getSize());
                    //System.out.println("SVM: "+getAlgorithmGuess(a5));
                    //updatePM(pm5, getAlgorithmGuess(a5));
                    //pm5.saveDatabase(pm5.getName()+".xml");
                    q4 = new IncaQuery(getAlgorithmGuess(a4), pm2, symboltable);
                    cv4 = q4.getCV();
                    cvSet.add(cv4);
                }
		
		//ensemble performance tracking
		//PerformanceMatrix pm6 = new PerformanceMatrix("ESMBLdsc", symboltable.getSize());
		Ensemble output = new Ensemble(cvSet, ensemble);
		//updatePM(pm6, ""+output.getDecision());
		//pm6.saveDatabase(pm6.getName()+".xml");
		//int out = output.getDecision();
		//writeOutput("Choice: " + translateDecision(out)+" " +out+"\n");
		//System.out.println("Choice: " + translateDecision(output.getDecision()));
		ensAns = translateDecision(output.getDecision());
		
	}//end getIncaResult method
	
	public void writeOutput(String guess){
		FileWriter stream;
		try {
			stream = new FileWriter("algensmbl.txt", true);
			BufferedWriter out = new BufferedWriter(stream);
			out.write(""+guess);
			out.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void trainBKS(BKS bks){
		//BKS ------
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		//pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		pm1.getDatabase("CFcv1.xml");
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new BPNNetRecg());
		//pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		pm3.getDatabase("ANNcv1.xml");
	
		String guesses = ""+getAlgorithmGuess(a1)+getAlgorithmGuess(a2);
		guesses = guesses.trim();
		bks.addTuple(guesses, 2);
		bks.trainSpace(guesses, parseIn(unknownSymbol));
	}//end trainBKS method
	
	private void testBKS(BKS bks){
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		//pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new BPNNetRecg());
		//pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		String guesses = getAlgorithmGuess(a1)+getAlgorithmGuess(a2);
		System.out.println("Tuple: "+guesses);
		//System.out.println("BKS: "+getBKSDecision(guesses, bks));
		
		updatePM(pm4, getBKSDecision(guesses,bks));
		pm4.saveDatabase(pm4.getName()+".xml");
	}//end testBKS method
	
	private String getBKSDecision(String tuple, BKS bks){
		
		return bks.getGuess(tuple);
	}
	private void saveBKS(BKS bks){
		bks.writeDatabase();
	}

	private String getAlgorithmGuess(AlgorithmAdaptor algorithm){
		String guess = "none";
		try {
			guess = algorithm.recognizeSymbol(unknownSymbol);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return guess;
	}//end getAlgorithmGuess
	
	//testing only-------
	private void outputCV(ConfidenceVector cv){
		for(int i = 0; i < cv.getSize(); i++){
			System.out.println(cv.getElement(i));
		}
	}//end outputCV method
	
	
	//testing only-------
	public void outputPMs(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				System.out.print("" + pm1.getRecNum(i, j)+" ");
			}
			System.out.println();
		}
	}//testing only------
	public void outputPMs(String out){
		CvMat disp = new CvMat(cvLoad(out+".xml"));
		System.out.println(out);
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				System.out.print("" + disp.get(i, j)+" ");
			}
			System.out.println();
		}
	}//testing only------
	
	public static String getPrefixName(int i){
		//single numerals
		if(i == 0) return "zero";if(i == 1) return "one";if(i == 2) return "two";
		if(i == 3) return "three";if(i == 4) return "four";if(i == 5) return "five";
		if(i == 6) return "six";if(i == 7) return "seven";if(i == 8) return "eight";
		if(i == 9) return "nine";
		//caps - English Alphabet
		if(i == 10) return "A";if(i == 11) return "B";if(i == 12) return "C";
		if(i == 13) return "D";if(i == 14) return "E";if(i == 15) return "F";
	
		return "null";
	}//end getPrefixName method
	
	public String translateDecision(int decision){
		String out = new String();
		out = getPrefixName(decision);
		return out;
	}//end translateDecision method
        
        public List<ConfidenceVector> getCVs(){
            return cvSet;
        }
	/**
	 * Main method for regression testing.
	 * @param args
	 */
        /*
	public static void main(String[] args){
		IncaDecision test = new IncaDecision();
		BKS bks = new BKS();
		PerformanceMatrix pm1 = new PerformanceMatrix("CFcv11", 10);
		PerformanceMatrix pm2 = new PerformanceMatrix("KNNcv11", 10);
		PerformanceMatrix pm3 = new PerformanceMatrix("ANNcv11", 10);
		PerformanceMatrix pm4 = new PerformanceMatrix("BKS2cv11", 10);
		
		for(int numInst = 1; numInst < 51; numInst++){
			System.out.println("Instance: " + numInst);
			for(int numCat = 0; numCat < 10; numCat++){
				test = new IncaDecision(pm1, pm2, pm3, pm4,
									getPrefixName(numCat)+(numInst));
				//test.writeOutput(getPrefixName(numCat)+"\n");
				//test = new IncaDecision(getPrefixName(numCat)+numInst, false);
				test.getIncaResult();
				//test.trainBKS(bks);
			}
		}
		
		//pm4.getDatabase("BKS.xml");
		/*
		for(int numInst = 1; numInst < 51; numInst++){
			System.out.println("Instance: " + numInst);
			for(int numCat = 0; numCat < 10; numCat++){
				test = new IncaDecision(pm1,pm2,pm3,pm4,getPrefixName(numCat)+
										(numInst));
				test.testBKS(bks);
			}
		}
		System.out.println("BKS Missed: "+missedRec);
		
		//test.outputPMs("CFcv1");
		//test.outputPMs("CFcv2");
		//test.outputPMs("CFcv3");
		//test.outputPMs("CFcv4");
		//test.outputPMs("CFcv5");
		//test.outputPMs("TM");
		//test.outputPMs("KNN");
		//test.outputPMs("SVM");
		//test.outputPMs("ESMBLdsc");
		//test.outputPMs("BKS2");
	}//end main method - regression testing
        * 
        */
}//end IncaDecision class