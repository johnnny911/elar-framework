//IncaDecision.java
package com.inca.main;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.inca.algorithms.BPNNetRecg;
import com.inca.algorithms.ContourFeaturesRecg;
import com.inca.algorithms.KNearestNRecg;
import com.inca.algorithms.SVMRecg;
import com.inca.algorithms.TemplateMatching;

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
	private Alphabet symboltable;
	private final int SIZE = 10; //regression testing only
	private String unknownSymbol;
	private boolean incrLrn = false;
	
	/**
	 * Constructor.
	 */
	public IncaDecision(PerformanceMatrix pm1, PerformanceMatrix pm2, 
						PerformanceMatrix pm3, PerformanceMatrix pm4,
						String unknownSymbol){
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		this.unknownSymbol = unknownSymbol;
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
		System.out.println(guess);
		pm.recordRecNum(symboltable.getSymbolValue(parseIn(unknownSymbol)), 
			symboltable.getSymbolValue(getPrefixName(Integer.parseInt(guess))));
	}//end buildPerfmatrices method
	
	private String parseIn(String input){
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
		
		
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		//System.out.println("CF: "+getAlgorithmGuess(a1));
		//updatePM(pm1, getAlgorithmGuess(a1));
		//pm1.saveDatabase(pm1.getName()+".xml");
		//outputPMs();
		
		/*
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new KNearestNRecg(10,10,50));
		pm2 = new PerformanceMatrix("KNN", symboltable.getSize());
		//System.out.println("KNN: "+getAlgorithmGuess(a2));
		//updatePM(pm2, getAlgorithmGuess(a2));
		//pm2.saveDatabase(pm2.getName()+".xml");
		*/
		
		AlgorithmAdaptor a3 = new AlgorithmAdaptor(new BPNNetRecg());
		pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		//System.out.println("ANN: "+getAlgorithmGuess(a3));
	//	updatePM(pm3, getAlgorithmGuess(a3));
	//	pm3.saveDatabase(pm3.getName()+".xml");
		
		/*
		AlgorithmAdaptor a4 = new AlgorithmAdaptor(new TemplateMatching());
		pm4 = new PerformanceMatrix("TM", symboltable.getSize());
//		System.out.println("TM: "+getAlgorithmGuess(a4));
		//updatePM(pm4, getAlgorithmGuess(a4));
		//pm4.saveDatabase(pm4.getName()+".xml");
		
		AlgorithmAdaptor a5 = new AlgorithmAdaptor(new SVMRecg());
		pm5 = new PerformanceMatrix("SVM", symboltable.getSize());
//		System.out.println("SVM: "+getAlgorithmGuess(a5));
		//updatePM(pm5, getAlgorithmGuess(a5));
		//pm5.saveDatabase(pm5.getName()+".xml");
		
		IncaQuery q1 = new IncaQuery(getAlgorithmGuess(a1), pm1, symboltable);
	//	IncaQuery q2 = new IncaQuery(getAlgorithmGuess(a2), pm2, symboltable);
		IncaQuery q3 = new IncaQuery(getAlgorithmGuess(a3), pm3, symboltable);
	//	IncaQuery q4 = new IncaQuery(getAlgorithmGuess(a4), pm4, symboltable);
	//	IncaQuery q5 = new IncaQuery(getAlgorithmGuess(a5), pm5, symboltable);
		ConfidenceVector cv1 = q1.getCV();
	//	ConfidenceVector cv2 = q2.getCV();
		ConfidenceVector cv3 = q3.getCV();
	//	ConfidenceVector cv4 = q4.getCV();
	//	ConfidenceVector cv5 = q5.getCV();
		
		//outputCV(cv1);
		List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
		cvSet.add(cv1);
		//cvSet.add(cv2);
		cvSet.add(cv3);
		//cvSet.add(cv4);
		//cvSet.add(cv5);
		//ensemble performance tracking
		PerformanceMatrix pm6 = new PerformanceMatrix("ESMBL", symboltable.getSize());
		Ensemble output = new Ensemble(cvSet);
		//updatePM(pm6, ""+output.getDecision());
		//pm6.saveDatabase(pm6.getName()+".xml");
		System.out.println("Choice: " + translateDecision(output.getDecision()));
		*/

		//BSK
		//trainBKS(a1, a3);
		
	}//end getIncaResult method
	
	private void trainBKS(BKS bks){
		//BKS ------
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new BPNNetRecg());
		pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		String guesses = ""+getAlgorithmGuess(a1)+getAlgorithmGuess(a2);
		guesses = guesses.trim();
		//System.out.println(guesses);
		bks.addTuple(guesses, 2);
		//bks.addTuple("00", 2);
		//bks.addTuple("00", 2);
		//System.out.println("table2: "+bks.getTuple(guesses).toString());
		bks.trainSpace(guesses, parseIn(unknownSymbol));
	}//end trainBKS method
	
	private void testBKS(BKS bks){
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new BPNNetRecg());
		pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		String guesses = getAlgorithmGuess(a1)+getAlgorithmGuess(a2);
		System.out.println("Tuple: "+guesses);
		System.out.println("BKS: "+getBKSDecision(guesses, bks));
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
		/*
		if(i == 16) return "G";if(i == 17) return "H";if(i == 18) return "I";
		if(i == 19) return "J";if(i == 20) return "K";if(i == 21) return "L";
		if(i == 22) return "M";if(i == 23) return "N";if(i == 24) return "O";
		if(i == 25) return "P";if(i == 26) return "Q";if(i == 27) return "R";
		if(i == 28) return "S";if(i == 29) return "T";if(i == 30) return "U";
		if(i == 31) return "V";if(i == 32) return "W";if(i == 33) return "X";
		if(i == 34) return "Y";if(i == 35) return "Z";
		//lower case - English Alphabet
		if(i == 36) return "a";if(i == 37) return "b";if(i == 38) return "c";
		if(i == 39) return "d";if(i == 40) return "e";if(i == 41) return "f";
		if(i == 42) return "g";if(i == 43) return "h";if(i == 44) return "i";
		if(i == 45) return "j";if(i == 46) return "k";if(i == 47) return "l";
		if(i == 48) return "m";if(i == 49) return "n";if(i == 50) return "o";
		if(i == 51) return "p";if(i == 52) return "q";if(i == 53) return "r";
		if(i == 54) return "s";if(i == 55) return "t";if(i == 56) return "u";
		if(i == 57) return "v";if(i == 58) return "w";if(i == 59) return "x";
		if(i == 60) return "y";if(i == 61) return "z";
		*/
		return "null";
	}//end getPrefixName method
	
	private String translateDecision(int decision){
		String out = new String();
		out = getPrefixName(decision);
		return out;
	}//end translateDecision method
	/**
	 * Main method for regression testing.
	 * @param args
	 */
	public static void main(String[] args){
		IncaDecision test = null;
		test= new IncaDecision();
		BKS bks = new BKS();
	
		for(int i = 0; i < 1; i++){
			for(int numInst = 1; numInst < 11; numInst++){
				System.out.println("Instance: " + numInst);
				for(int numCat = 0; numCat < 10; numCat++){
					test = new IncaDecision(getPrefixName(numCat)+
											(numInst), false);
					//test.getIncaResult();
					test.trainBKS(bks);
				}
			}
		}
		
		for(int numInst = 1; numInst < 2; numInst++){
			System.out.println("Instance: " + numInst);
			for(int numCat = 0; numCat < 10; numCat++){
				test = new IncaDecision(getPrefixName(numCat)+
										(numInst), false);
				test.testBKS(bks);
			}
		}
		//test = new IncaDecision("two2", false);
		//test.testBKS();
		//System.out.println(test.getBKSDecision("00"));
		//test = new IncaDecision();
		//test.outputPMs("CF");
		//test.outputPMs("ANN");
		//test.outputPMs("TM");
		//test.outputPMs("KNN");
		//test.outputPMs("SVM");
		//test.outputPMs("ESMBL");
	}//end main method - regression testing
}//end IncaDecision class