//IncaQuery.java
package org.elar.decision;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * IncaQuery.java - Creates the confidence vector for a certain algorithm.
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

public class IncaQuery {
	private String algGuess;
	private ConfidenceVector cv;
	private PerformanceMatrix pm;
	private Alphabet sigma;
	
	/**
	 * Constructor. Accepts the symbol name, performance matrix, and
	 * alphabet for recognition.
	 * @param symbol	String symbol name
	 * @param pm		Performance matrix of algorithm
	 * @param sigma		Alphabet of recognition symbols
	 */
	public IncaQuery(String symbol, PerformanceMatrix pm, Alphabet sigma){
		this.algGuess = symbol;
		this.pm = pm;
		this.sigma = sigma;
	}//end default constructor method
	/**
	 * Creates the confidence vector for the algorithm.
	 */
	private void createConfVect(){
		String guess = new String(IncaDecision.getPrefixName(Integer.parseInt(algGuess)));
		int symbolNumber = sigma.getSymbolValue(guess).intValue();
		double divisor = 0, post = 0;
		// sum column (# times this symbol was recognized)
		for(int i = 0; i < pm.getSize(); i++){
			divisor += pm.getRecNum(i, symbolNumber);
		}
		cv = new ConfidenceVector((String)algGuess);
		for(int i = 0; i < pm.getSize(); i++){
			post = pm.getRecNum(i, symbolNumber) / divisor;
			cv.addElement(post);
		}
	}//end createConfVect method
	/**
	 * returns the confidence vector corresponding to the symbol
	 * to be recognized.
	 * @return	cv		ConfidenceVector of probability space for symbol
	 */
	public ConfidenceVector getCV(){
		createConfVect();
		return cv;
	}
}//end IncaQuery class