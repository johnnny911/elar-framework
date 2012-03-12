//PerformanceMatrix.java
package com.inca.main;
import java.lang.reflect.Array;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_core.*;
/**
 * 
 * @author James Neilan
 * @version	1.1.0
 * Inca-POC
 *
 * PerformanceMatrix.java - Object that contains the performance past
 * of a given algorithm in the Inca POC.
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

// Actual = row
// Seen = column
public class PerformanceMatrix {
	private String name;
	private CvMat pm;
	private int size;
	
	/**
	 * Constructor. Accepts the string name and size of the performance
	 * matrix.
	 * @param algName	String name of matrix
	 */
	public PerformanceMatrix(String algName, int size){
		this.size = size;
		this.pm = CvMat.create(size, size);
		this.name = algName;
		this.getDatabase(algName+".xml");
	}//end constructor method
	/**
	 * Returns the string value of the name of the matrix.
	 * @return	name	String value identifier.
	 */
	public String getName(){
		return name;
	}//end getName method
	/**
	 * Adds 1 to the number of recognitions of given symbol.
	 * @param row	integer row
	 * @param col	integer column
	 */
	public void recordRecNum(int row, int col){
		pm.put(row,col, (pm.get(row,col)+1));
	}//end recordRecNum method
	/**
	 * Returns the integer value at position row,col.
	 * @param row	integer row
	 * @param col	integer column
	 * @return		integer value at r,c
	 */
	public int getRecNum(int row, int col){
		return (int) pm.get(row,col);
	}//end getRecNum method
	/**
	 * From http://www.source-code.biz/snippets/java/3.htm
	 * @param oldArray	old pm to resize.
	 * @param newSize	integer value of size to increase
	 * @return Object	new performance matrix sized to newSize
	 */
	/*
	private static Object resizeArray(Object oldArray, int newSize){
		int oldSize = Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = Array.newInstance(elementType, newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if(preserveLength > 0){
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		}
		return newArray;
	}//end resizeArray method
	*/
	/**
	 * From http://www.source-code.biz/snippets/java/3.htm.
	 * Resizes the multi-dimensional array.
	 * @param increase	integer value to increase size.
	 */
	/*
	public void resize(int increase){
		//resize rows
		pm = (int [][])resizeArray(pm, increase);
		//resize columns
		for(int i = 0; i < pm.length; i++){
			pm[i] = (int[])resizeArray(pm[i], increase);
		}
	}//end resize method
	*/
	
	public double getConfidence(int sym){
		double conf = 0.0;
		for (int i=0; i<size; i++){
			conf += pm.get(i, sym);
		}
		conf /= pm.get(sym, sym);
		return conf;
	}
	
	/**
	 * Returns the integer size of the performance matrix.
	 * @return	length		integer length
	 */
	public int getSize(){
		return pm.rows();
	}///end getSize method
	
	public void getDatabase(String fileName){
		pm = new CvMat(cvLoad(fileName));
	}//end getDatabase method
	
	public void saveDatabase(String fileName){
		cvSave(fileName, pm);
	}//end saveDatabase method
}//end PerformanceMatrix class