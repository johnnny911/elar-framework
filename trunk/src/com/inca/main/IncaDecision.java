//IncaDecision.java
package com.inca.main;

import com.inca.algorithms.BPNNetRecg;
import com.inca.algorithms.ContourFeaturesRecg;
import com.inca.algorithms.KNearestNRecg;

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
	
	/**
	 * Constructor.
	 */
	public IncaDecision(PerformanceMatrix pm1, PerformanceMatrix pm2, 
						PerformanceMatrix pm3, String unknownSymbol){
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		this.unknownSymbol = unknownSymbol;
		//testing
		//buildSymbolTable();
		//buildPerfMatrices();
	}//end default constructor
	
	public IncaDecision(String unknownSymbol){
		this.unknownSymbol = unknownSymbol;//+".png";
	}//end constructor
	
	/**
	 * Regression testing only.
	 */
	private void buildSymbolTable(){
		symboltable = new Alphabet(SIZE);
		for(int i = 0; i < SIZE; i++){
			symboltable.addSymbol(""+i, new Integer(i));
		}
	}//end buildSymbolTable method
	/**
	 * regression testing only.
	 */
	private void buildPerfMatrices(){
		pm1 = new PerformanceMatrix("A", symboltable.getSize());
		pm2 = new PerformanceMatrix("B", symboltable.getSize());
		pm3 = new PerformanceMatrix("C", symboltable.getSize());
		int r, c, min = 0, max = 9;
		
		for(int i = 0; i < 5000; i ++){
			r = min + (int)(Math.random() * ((max - min) + 1));
			c = min + (int)(Math.random() * ((max - min) + 1));
			pm1.recordRecNum(r, c);
			r = min + (int)(Math.random() * ((max - min) + 1));
			c = min + (int)(Math.random() * ((max - min) + 1));
			pm2.recordRecNum(r, c);
			r = min + (int)(Math.random() * ((max - min) + 1)) ;
			c = min + (int)(Math.random() * ((max - min) + 1)) ;
			pm3.recordRecNum(r, c);
		}
	}//end buildPerfmatrices method
	/**
	 * 
	 */
	public void getIncaResult(){
		/*
		IncaQuery q1 = new IncaQuery("4", pm1, symboltable);
		IncaQuery q2 = new IncaQuery("3", pm2, symboltable);
		IncaQuery q3 = new IncaQuery("3", pm3, symboltable);
		*/
		AlgorithmAdaptor a1 = new AlgorithmAdaptor(new ContourFeaturesRecg("prof.db", 1000, 50));
		System.out.println("CF Guess: " + getAlgorithmGuess(a1));
		
		AlgorithmAdaptor a2 = new AlgorithmAdaptor(new KNearestNRecg());
		System.out.println("K-NN Guess: " + getAlgorithmGuess(a2));
		
		AlgorithmAdaptor a3 = new AlgorithmAdaptor(new BPNNetRecg());
		System.out.println("ANN Guess: " + getAlgorithmGuess(a3));
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
	/**
	 * Main method for regression testing.
	 * @param args
	 */
	/*
	public static void main(String[] args){
		IncaDecision test = new IncaDecision();
		test.getIncaResult();
		//outputPMs();
		
	}//end main method - regression testing
	*/
}//end IncaDecision class
