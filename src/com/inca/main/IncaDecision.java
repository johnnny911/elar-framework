//IncaDecision.java
package com.inca.main;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.inca.algorithms.BPNNetRecg;
import com.inca.algorithms.ContourFeaturesRecg;
import com.inca.algorithms.KNearestNRecg;
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
	private PerformanceMatrix pm1, pm2, pm3;
	private Alphabet symboltable;
	private final int SIZE = 10; //regression testing only
	private String unknownSymbol;
	private boolean incrLrn = false;
	
	/**
	 * Constructor.
	 */
	public IncaDecision(PerformanceMatrix pm1, PerformanceMatrix pm2, 
						PerformanceMatrix pm3, String unknownSymbol){
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		this.unknownSymbol = unknownSymbol;
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
	 * regression testing only.
	 */
	private void updatePM(PerformanceMatrix pm, String guess){
		pm.recordRecNum(symboltable.getSymbolValue(parseIn(unknownSymbol)), 
			symboltable.getSymbolValue(getPrefixName(Integer.parseInt(guess))));
	}//end buildPerfmatrices method
	
	private String parseIn(String input){
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
		/*
		IncaQuery q1 = new IncaQuery("4", pm1, symboltable);
		IncaQuery q2 = new IncaQuery("3", pm2, symboltable);
		IncaQuery q3 = new IncaQuery("3", pm3, symboltable);
		*/
		/*
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg());
		pm1 = new PerformanceMatrix("CF", symboltable.getSize());
		updatePM(pm1, getAlgorithmGuess(a1));
		pm1.saveDatabase(pm1.getName()+".xml");
		*/
		
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new KNearestNRecg());
		pm2 = new PerformanceMatrix("KNN", symboltable.getSize());
		updatePM(pm2, getAlgorithmGuess(a2));
		pm2.saveDatabase(pm2.getName()+".xml");
		
		/*
		AlgorithmAdaptor a3 = new AlgorithmAdaptor(new BPNNetRecg());
		pm3 = new PerformanceMatrix("ANN", symboltable.getSize());
		updatePM(pm3, getAlgorithmGuess(a3));
		pm3.saveDatabase(pm3.getName()+".xml");
		*/
		
		//IncaQuery q1 = new IncaQuery(getAlgorithmGuess(a1), 
		//											pm1, symboltable);
		
		//ConfidenceVector cv1 = q1.getCV();
		//ConfidenceVector cv2 = q2.getCV();
		//ConfidenceVector cv3 = q3.getCV();
		
		//outputCV(cv1);
		//ConfidenceVector cvSet = new ConfidenceVector("Ensemble");
		//cvSet.addCV(cv1);
		//cvSet.addCV(cv2);
		//cvSet.addCV(cv3);
		//System.out.println("Choice: " + (new Ensemble(cvSet)).getDecision());
		//System.out.println("Inca's Decision maps to Symbol: " + symboltable.getSymbolValue(""+
		//											(new Ensemble(cvSet)).getDecision()));
	}//end getIncaResult method
	
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
	private static String getPrefixName(int i){
		if(i == 0) return "zero";
		if(i == 1) return "one";
		if(i == 2) return "two";
		if(i == 3) return "three";
		if(i == 4) return "four";
		if(i == 5) return "five";
		if(i == 6) return "six";
		if(i == 7) return "seven";
		if(i == 8) return "eight";
		if(i == 9) return "nine";
		return "null";
	}//end getPrefixName method
	/**
	 * Main method for regression testing.
	 * @param args
	 */
	
	public static void main(String[] args){
		IncaDecision test = null;
		
		for(int i = 0; i < 10; i++){
			for(int numInst = 1; numInst < 11; numInst++){
				System.out.println("Instance: " + numInst);
				for(int numCat = 0; numCat < 10; numCat++){
					test = new IncaDecision(getPrefixName(numCat)+
											(numInst), false);
					test.getIncaResult();
				}
			}
		}
		
		/*
		test = new IncaDecision();
		test.outputPMs("CF");
		test.outputPMs("ANN");
		*/
	}//end main method - regression testing
	
}//end IncaDecision class
