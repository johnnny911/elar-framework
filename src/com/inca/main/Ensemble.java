//Ensemble.java
package com.inca.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * Ensemble.java - Class that accepts the confidence vectors and combines the 
 * Classifiers for the Inca result.
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

public class Ensemble {
	private List<ConfidenceVector> confVects;
	private ConfidenceVector maxValues;
	private static boolean trained = false;
		// Symbol-space sized decision template
		// Essentially an average of all algorithms' performance for each symbol,
		// each CV is compared against this for the decision template combiner
	private static double[] decision_templates; 
	
	static{
		if (!trained){
			try{
				trainDP();
				trained = true;
			}
			catch(Exception e){
				trained = false;
			}	
		}
	}
	
	/**
	 * Constructor. Accepts a multi-dimensional ConfidenceVector
	 * containing all N algorithms confidence for intended symbol.
	 * @param vects		vector of vectors
	 */
	public Ensemble(List<ConfidenceVector> vects){
		this.confVects = vects;
		train();
		// Initialize with size of symbol space, basically
		decision_templates = new double[vects.get(0).getSize()];
	}//end default constructor
	
	private void train(){
		
	}
	
	// Train decision template
	private static void trainDP() throws Exception{
		String filename = "nnDatabase";
		File file = null;
		Scanner sc = null;
		try{
			file = new File(filename, "r");
			sc = new Scanner(file);
		}
		catch(Exception e){
			System.err.println("error loading database: " + filename);
			throw new Exception("error loading database: " + filename,e);
		}
		finally{
			try{if (sc != null) sc.close();} catch(Exception e){}
		}
		// Each symbol
//		for (int i=0; i<decision_templates.length; i++){
//			double avg = 0.0;
//			// Sum the value for this symbol across training set
////			for (int j=0; j<confVects.size(); j++)
////				avg += confVects.get(j).getElement(i);
////			avg /= confVects.size();
//			// Map the symbol to the average
//			decision_templates[i] = avg;
//		}
	}
	/**
	 *
	 * Returns the Averaged guess of symbol.
	 * @return 	integer guess mapping to symbol in alphabet.
	 */
	public int getDecision(){
		System.out.println("decision template says " + decisionTemplateCombiner());
		int avgComb = averagingCombiner();
		System.out.println("averaging combiner says " + avgComb);
		return avgComb;
	}//end getDecision method

	
	// Returns a guess based on the lowest squared euclidean distance
	// to its class' decision template.
	private int decisionTemplateCombiner(){
		if (!trained)
			return -1;
		
		Map<Integer,Double> choices = new HashMap<Integer,Double>();
		for (int i=0; i<confVects.size(); i++){
			ConfidenceVector cv = confVects.get(i);
			double distance = 0.0;
			for (int j=0; j < cv.getSize(); j++){
				distance += Math.pow(cv.getElement(j) - decision_templates[i], 2);
			}
			choices.put(i, distance);
		}
		return 0;
	}
	private int averagingCombiner(){
		int totalSize = confVects.get(0).getSize();
		double totalPs;
		ConfidenceVector choices = new ConfidenceVector("Choices");
		for(int i = 0; i < totalSize; i++){
			totalPs = 0;
			for(int j = 0; j < confVects.size(); j++){
				double elem = confVects.get(j).getElement(i);
				totalPs += !Double.isNaN(elem) ? elem : 0.0;
			}
			//totalPs /= confVects.size();
			choices.addElement(totalPs);
		}
		return choices.getIndexMax();
	}//end averagingCombiner method
}//end Ensemble class
