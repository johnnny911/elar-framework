//Ensemble.java
package org.inca.decision;

import static com.googlecode.javacv.cpp.opencv_core.cvLoad;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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
	private static final int SYMBOL_SPACE_SZ = 10;
        private String ensemble;
        private static final String DB_PATH = "databases\\";
	
	static{
		if (!trained){
			try{
				decision_templates = new double[SYMBOL_SPACE_SZ];
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
        
        public Ensemble(List<ConfidenceVector> vects, String ensemble){
		this.confVects = vects;
                this.ensemble = ensemble;
	}//end default constructor
	
	
	// Train decision template
	private static void trainDP() throws Exception{
		try{
			List<CvMat> pms = new ArrayList<CvMat>();
			
			CvMat ann = new CvMat(cvLoad(DB_PATH+"GlKNNcv1.xml")); pms.add(ann);
			CvMat cf = new CvMat(cvLoad(DB_PATH+"GlSVMcv1.xml"));   pms.add(cf);
			Map<Integer,List<ConfidenceVector>>  symbolToCvs= new HashMap<Integer,List<ConfidenceVector>>();
			//System.out.println("train");
			
			// Construct CV's for each symbol
			for (int i=0; i<10; i++){
			
				for (int k=0; k < pms.size(); k++){
					CvMat pm = pms.get(k);
					double divisor = 0.0;
					// each column (symbol recognized)
					// sum [row][j]
					for (int j=0; j<SYMBOL_SPACE_SZ; j++){
						divisor += pm.get(i, j);
					}
					
					
					ConfidenceVector cv = new ConfidenceVector(String.valueOf(i));
					// Loop back through, compute posterior probability
					for (int j=0; j<SYMBOL_SPACE_SZ; j++){
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
			for (int i=0; i < SYMBOL_SPACE_SZ; i++){
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
			
		}
	}
	/**
	 *
	 * Returns the Averaged guess of symbol.
	 * @return 	integer guess mapping to symbol in alphabet.
	 */
	public int getDecision(){
            int combiner = 0;
            if(ensemble.equalsIgnoreCase("MaxSum")){
                combiner = averagingCombiner();  
            }
            if(ensemble.equalsIgnoreCase("Decision Template")){
                combiner = decisionTemplateCombiner();  
            }
            if(ensemble.equalsIgnoreCase("Demspter-Shafer")){
                combiner = dempsterShaferCombiner();  
            }
            if(ensemble.equalsIgnoreCase("BKS")){//to do finish this.
                combiner = averagingCombiner();  
            }
               
		//int dtc = decisionTemplateCombiner();
		//int dsc = dempsterShaferCombiner();
		//System.out.println("averaging combiner says " + avgComb);
		//System.out.println("decision template says " + decisionTemplateCombiner());
		//System.out.println("demp shaf says " + dempsterShaferCombiner());
            return combiner;
	}//end getDecision method

	
	// Returns a guess based on the lowest squared euclidean distance
	// to its class' decision template.
	private int decisionTemplateCombiner(){
		if (!trained)
			return -1;
		
		List<CombinerChoice> choices = new ArrayList<CombinerChoice>();
		
		for (int i=0; i<decision_templates.length; i++){
			double distance = 0.0;
			for (int j=0; j < confVects.size(); j++){
				double dist = confVects.get(j).getElement(i);
				if (!Double.isNaN(dist))
					distance += Math.pow(dist - decision_templates[i], 2);
			}
			distance *= ( 1 - 1.0 / ( (confVects.size()*decision_templates.length) ) );
			if (distance != 0.0) choices.add(new CombinerChoice(i, distance));
		}
		
//		for (int i=0; i<confVects.size(); i++){
//			ConfidenceVector cv = confVects.get(i);
//			double distance = 0.0;
//			for (int j=0; j < cv.getSize(); j++){
//				distance += Math.pow(cv.getElement(j) - decision_templates[i], 2);
//			}
//			choices.add(new CombinerChoice(i, distance));
//		}
		return Collections.min(choices).getSymbol();
	}
	
	// With C = number of symbols
	//		L = number of classifiers
	// Proximity (C x L) --> Degrees of Belief (C x L) --> Support (C)
	private int dempsterShaferCombiner(){
		if (!trained)
			return -1;
		
		// Calculate proximity from decision template 
		// to each classifier
		ArrayList<DempShaf>[] symbolToProx = new ArrayList[SYMBOL_SPACE_SZ];
		
		for (int i=0; i < SYMBOL_SPACE_SZ; i++){
			for (int j=0; j< confVects.size(); j++){
				ConfidenceVector cv = confVects.get(j);
				double proximity = 0.0;
				final double output = cv.getElement(i);
				// Numerator: distance to this symbol's decision template
				proximity = Math.pow(output - decision_templates[i], 2);
				proximity += 1;
				proximity = Math.pow(proximity, -1);
				double denom = 0.0;
				// Denominator: sum of distance to all decision templates
				for (int k=0; k<decision_templates.length; k++){
					denom += Math.pow(Math.pow(output - decision_templates[k],2), -1);
				}
				proximity /= denom;
				
				// Each decision template symbol has a list of proximities (length L)
				// attached to it
				if (symbolToProx[i] == null)
					symbolToProx[i] = new ArrayList<DempShaf>();
				
				symbolToProx[i].add(new DempShaf(i, proximity));
			}
		}
		
		
		
		// Generate degrees of belief for every classifier for every symbol
		for (int i=0; i<symbolToProx.length; i++){
			ArrayList<DempShaf> dsList = (ArrayList<DempShaf>)symbolToProx[i];
			for (int j=0; j<dsList.size(); j++){
				DempShaf ds = dsList.get(j);
				double belief = 0.0;
				belief = ds.getProx();
				double product = 0.0;
				// What did the other classifiers for this symbol say?
				for (int k=0; k<dsList.size(); k++){
					if (dsList.get(k) != ds) 
						product = product > 0.0 ? product*dsList.get(k).getProx() : dsList.get(k).getProx();
				}
				belief *= product;
				// Denominator
				belief /= ( (1 - ds.getProx()) * (1 - product) ) ;
				
				ds.setBelief(belief);
			}
		}
		
		// For each symbol, calculate support
		List<CombinerChoice> choices = new ArrayList<CombinerChoice>();
		for (int i=0; i<symbolToProx.length; i++){
			ArrayList<DempShaf> dsList = (ArrayList<DempShaf>)symbolToProx[i];
			double support = 0.0;
			for (int j=0; j<dsList.size(); j++){
				support = support > 0.0 ? support*dsList.get(j).getBelief() : dsList.get(j).getBelief();
			}
			choices.add(new CombinerChoice(i, support));
		}
		
		return Collections.min(choices).getSymbol();
	}
	
	private class DempShaf{
		private double prox, belief;
		private int ix; // Symbol
		
		DempShaf(int ix){
			this(ix, 0.0, 0.0);
		}
		
		DempShaf(int ix, double prox){
			this(ix, prox, 0.0);
		}
		
		DempShaf(int ix, double prox, double belief){
			this.prox = prox;
			this.belief = belief;
			this.ix = ix;
		}
		
		public double getProx() {return prox;}
		public void setProx(double prox) {this.prox = prox;}
		public double getBelief() {return belief;}
		public void setBelief(double belief) {this.belief = belief;}
		public int getIx() {return ix;}
		public void setIx(int ix) {this.ix = ix;}

	}
	
	private class CombinerChoice implements Comparable<CombinerChoice>{
		private int ix; // Symbol
		private double confidence;
		
		CombinerChoice(int ix, double confidence){
			this.ix = ix;
			this.confidence = confidence;
		}
		
		public int compareTo(CombinerChoice other){
			return new Double(confidence).compareTo(other.confidence);
		}
		
		public int getSymbol(){
			return ix;
		}
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
