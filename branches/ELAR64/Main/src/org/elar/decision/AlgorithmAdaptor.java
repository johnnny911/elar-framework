package org.elar.decision;

import org.elar.algorithms.*;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca P.O.C.
 *
 * AplgorithmAdaptor.java - Adaptor class for calling recognize method for 
 * differing algorithms.
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
public class AlgorithmAdaptor {
	private Algorithm algorithm;
	/**
	 * 
	 * @param algorithm
	 */
	public AlgorithmAdaptor(Algorithm algorithm){
		this.algorithm = algorithm;
	}//end AlgorithmAdaptor constructor
	/**
	 * 
	 * @param unknownSymbol
	 * @return
	 * @throws Exception
	 */
	public String recognizeSymbol(String unknownSymbol)throws Exception{
		algorithm.populateDatabase();
		return algorithm.recognizeSymbol(unknownSymbol+".png");//.png
	}//end recognizeSymbol method
}//end AlgorithmAdapter class