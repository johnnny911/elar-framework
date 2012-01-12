//Symbol.java
package com.inca.main;
/**
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * Symbol.java
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
public class Symbol {
	private String name;
	private int recCount;
	private double postProb;
	private Object symbolData;
	
	/**
	 * 
	 * @param name
	 */
	public Symbol(String name){
		this.name = name;
	}//end default constructor
	/**
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}//end getName method
	/**
	 * 
	 */
	public void incrementCount(){
		recCount += 1;
	}//end incrementCount method
	/**
	 * 
	 * @return
	 */
	public int getCount(){
		return recCount;
	}//end getCount method
	/**
	 * 
	 * @param prob
	 */
	public void setProb(double prob){
		postProb = prob;
	}//end setProb method
	/**
	 * 
	 * @return
	 */
	public double getProb(){
		return postProb;
	}//end getProb method
	/**
	 * 
	 * @param data
	 */
	public void setFeatures(Object data){
		symbolData = data;
	}//end setFeatures method
	/**
	 * 
	 * @return
	 */
	public Object getFeatures(){
		return symbolData;
	}//end getFreatures method
}//end Symbol class