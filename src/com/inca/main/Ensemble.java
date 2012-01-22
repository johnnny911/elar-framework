//Ensemble.java
package com.inca.main;

import java.util.List;

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
	
	/**
	 * Constructor. Accepts a multi-dimensional ConfidenceVector
	 * containing all N algorithms confidence for intended symbol.
	 * @param vects		vector of vectors
	 */
	public Ensemble(List<ConfidenceVector> vects){
		this.confVects = vects;
	}//end default constructor
	/**
	 * Returns the Averaged guess of symbol.
	 * @return 	integer guess mapping to symbol in alphabet.
	 */
	public int getDecision(){
		return averagingCombiner();
	}//end getDecision method
	/**
	 * Private method that performs the averaging and returns the recognition
	 * decision to caller method.
	 * @return	index	index of recognition guess
	 */
	private int averagingCombiner(){
		int totalSize = confVects.get(0).getSize();
		double totalPs;
		ConfidenceVector choices = new ConfidenceVector("Choices");
		for(int i = 0; i < totalSize; i++){
			totalPs = 0;
			for(int j = 0; j < confVects.size(); j++){
				totalPs += confVects.get(j).getElement(i);
			}
			//totalPs /= confVects.size();
			choices.addElement(totalPs);
		}
		return choices.getIndexMax();
	}//end averagingCombiner method
}//end Ensemble class
