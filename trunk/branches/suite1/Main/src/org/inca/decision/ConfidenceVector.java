//ConfidenceVector.java
package org.inca.decision;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * ConfidenceVector.java - Vector object that holds the Bayesian probability
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

public class ConfidenceVector{
	private Vector<Double> cv;
	private String name;
	private Comparator comparator = Collections.reverseOrder();
	
	/**
	 * Constructor. Accepts string name.
	 * @param algName	String name identifying the object
	 */
	public ConfidenceVector(String algName){
		this.cv = new Vector<Double>();
		this.name = algName;
	}//end constructor method
	/**
	 * Returns the name of the vector.
	 * @return	name	String value of name
	 */
	public String getName(){
		return name;
	}//end getName method
	/**
	 * Adds a double value element to the vector.
	 * @param element	double value number
	 */
	public void addElement(double element){
		cv.add(element);
	}//end addElement method
	/**
	 * Adds a confidenceVector to the vector. Used to create
	 * a multi-dimensional vector.
	 * @param v		ConfidenceVector to add
	 */
//	public void addCV(ConfidenceVector v){
//		cv.add(v);
//	}//end addCV method
	/**
	 * Sorts the vector from max to min.
	 */
	private void sortVector(){
		Collections.sort(cv, comparator);
	}//end sortVector method
	/**
	 * Returns the Object containing the max ordered value 
	 * in the vector.
	 * @return	Collections.max
	 */
	public Object getMax(){
		return Collections.max(cv);
	}//end getMax method
	/**
	 * Returns the integer index of the max value in the
	 * vector.
	 * @return	index	index of max value
	 */
	public int getIndexMax(){
		return cv.indexOf(getMax());
	}//end getIndexMax method
	/**
	 * Returns the integer size of the vector. Number of 
	 * elements.
	 * @return	size	integer size of vector
	 */
	public int getSize(){
		return cv.size();
	}//end getSize method
	/**
	 * Returns the object contained in the vector at the
	 * index position.
	 * @param index		target position
	 * @return	element object at position index
	 */
	public double getElement(int index){
		return cv.elementAt(index);
	}//end getElement mehtod
}//end ConfidenceVector class