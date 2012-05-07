/**
 * 
 * @author Mark Henderson and James Neilan
 * @version	1.0.0
 * Thesis and Research Work.
 *
    Copyright (C) 2012	James Neilan

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
package org.elar.acceleglove;
//OpenCV API
import com.googlecode.javacv.cpp.opencv_core.CvMat;

public abstract class Algorithm {
	private String name;
	/**
         * Constructor method.
         * @param name name of algorithm.
         */
	Algorithm(String name){
		this.name = name;
	}//end constructor method
	/**
         * Returns name of algorithm object.
         * @return name name of algorithm
         */
	public String getName(){
		return name;
	}//end getName method
	//abstract methods
	public abstract int predict(CvMat inputs);
	public abstract void train(GestureData inputs, Gesture actual);
}//end Algorithm class
