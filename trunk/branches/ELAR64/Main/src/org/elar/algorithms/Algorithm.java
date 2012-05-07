package org.elar.algorithms;

public abstract class Algorithm {
	public Algorithm(){}
	
	public abstract String recognizeSymbol(String file) throws Exception;
	public abstract void populateDatabase() throws Exception;
}//end AlgorithmClass