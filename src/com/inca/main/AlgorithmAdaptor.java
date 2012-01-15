package com.inca.main;

import com.inca.algorithms.BPNNetRecg;
import com.inca.algorithms.ContourFeaturesRecg;
import com.inca.algorithms.KNearestNRecg;
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
	private Object algorithm;
	private int cast;
	
	/**
	 * 
	 * @param algorithm
	 */
	public AlgorithmAdaptor(ContourFeaturesRecg algorithm){
		this.algorithm = algorithm;
		this.cast = 0;
	}//end AlgorithmAdaptor constructor
	/**
	 * 
	 * @param algorithm
	 */
	public AlgorithmAdaptor(BPNNetRecg algorithm){
		this.algorithm = algorithm;
		this.cast = 1;
	}//end AlgorithmAdaptor constructor
	/**
	 * 
	 * @param algorithm
	 */
	public AlgorithmAdaptor(KNearestNRecg algorithm){
		this.algorithm = algorithm;
		this.cast = 2;
	}//end AlgorithmAdaptor constructor
	/**
	 * 
	 * @param unknownSymbol
	 * @return
	 * @throws Exception
	 */
	public String recognizeSymbol(String unknownSymbol)throws Exception{
		String guess = null;
		
		switch(cast){
		case 0:
			((ContourFeaturesRecg)algorithm).populateDatabase("prof.db", 1000, 50);
			guess = ((ContourFeaturesRecg)algorithm).recognizeSymbol(unknownSymbol+".png");
			break;
		case 1:
			((BPNNetRecg)algorithm).populateDatabase("nnDatabase");
			guess = ((BPNNetRecg)algorithm).recognizeSymbol(unknownSymbol+".png");
			break;
		case 2:
			((KNearestNRecg)algorithm).populateDatabase();
			guess = ((KNearestNRecg)algorithm).recognizeSymbol(unknownSymbol+".png");
			break;
		default:
			throw (new Exception("Algorithm not found or not in adaptor."));
		}
		
		return guess;
	}//end recognizeSymbol method
}//end AlgorithmAdapter class