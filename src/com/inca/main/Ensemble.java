//Ensemble.java
package com.inca.main;

import static com.googlecode.javacv.cpp.opencv_core.cvLoad;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

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
				decision_templates = new double[10];
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
	}//end default constructor
	
	
	// Train decision template
	private static void trainDP() throws Exception{
		try{
			List<CvMat> pms = new ArrayList<CvMat>();
			
			CvMat ann = new CvMat(cvLoad("ANN.xml")); pms.add(ann);
			CvMat cf = new CvMat(cvLoad("CF.xml"));   pms.add(cf);
			Map<Integer,List<ConfidenceVector>>  symbolToCvs= new HashMap<Integer,List<ConfidenceVector>>();
			System.out.println("train");
			
			// Construct CV's for each symbol
			for (int i=0; i<10; i++){
			
				for (int k=0; k < pms.size(); k++){
					CvMat pm = pms.get(k);
					double divisor = 0.0;
					// each column (symbol recognized)
					// sum [row][j]
					for (int j=0; j<10; j++){
						divisor += pm.get(i, j);
					}
					
					
					ConfidenceVector cv = new ConfidenceVector(String.valueOf(i));
					// Loop back through, compute posterior probability
					for (int j=0; j<10; j++){
						double post = pm.get(i,j) / divisor;
						cv.addElement(post);
					}
					
					if (!symbolToCvs.containsKey(i)){
						List<ConfidenceVector> list = new ArrayList<ConfidenceVector>();
						list.add(cv);
						symbolToCvs.put(i, list);
					}
					else
						symbolToCvs.get(i).add(cv);
				}
			}
			
			// Finally, average CV value for each symbol and put into decision template
			for (int i=0; i < 10; i++){
				List<ConfidenceVector> cvs = symbolToCvs.get(i);
				double weight = 0.0;
				for (int j=0; j<cvs.size(); j++)
					weight += cvs.get(j).getElement(i);
				weight /= cvs.size();
				decision_templates[i] = weight;
			}
	
		}
		catch(Exception e){
			System.err.println("error loading databases ");
			throw new Exception("error loading database",e);
		}
		finally{
			if (true)
				System.out.println();
		}
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
