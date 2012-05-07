package org.elar.acceleglove;
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
import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
//mapping for sensors on glove
enum GesturePoint{
	THUMB_X, THUMB_Y, THUMB_Z,
	INDEX_X, INDEX_Y, INDEX_Z,
	MIDDLE_X, MIDDLE_Y, MIDDLE_Z,
	RING_X, RING_Y, RING_Z,
	PITCH_X, PITCH_Y, PITCH_Z,
	PA_X, PA_Y, PA_Z
	}

public class GestureData {
	public static final int NUM_POINTS = 18;
	public static final int NUM_GESTURES = 10;
	public static final int NUM_GESTURES_TRAIN_EACH = 150;//from 50 to 150 for cv tests
	public static final int NUM_GESTURES_TRAIN = NUM_GESTURES_TRAIN_EACH*2;
	
	private double[] data;
        /**
         * Constructor method.
         * @param str input data values from data glove. 18 values.
         */
	public GestureData(String str){
		data = new double[NUM_POINTS];
		String[] split = str.split(" ");
		for (int i=0; i<split.length; i++)
			data[i] = Integer.parseInt(split[i]);
	}//end constructor method
	/**
         * Returns gesture data in data array.
         * @param gp point objects in array.
         * @return data value of point in array.
         */
	public double get(GesturePoint gp){
		return data[gp.ordinal()];
	}//end get method
	/**
         * Returns data for gesture
         * @return array of data points for gesture.
         */
	public double[] getData(){ 
		return data; 
	}//end getData method
	/**
         * Sets element in data array for a gesture.
         * @param ix    index of array
         * @param elem  value in gesture array
         */
	public void set(int ix, double elem){
		data[ix] = elem;
	}//end set method
	/**
         * Returns a CvMAt object for rec. algorithms
         * @return outputs  CvMAt object for rec. algorithms.
         */
	public CvMat toCvMat(){
		CvMat inputs = cvCreateMat(NUM_POINTS*NUM_GESTURES, data.length, CV_32FC1);
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		return inputs;
	}//end toCvMAt method
        /**
         * Returns a CvMAt object for rec. algorithms
         * @return outputs  CvMAt object for rec. algorithms.
         */
	public CvMat toCvMat1(){
		CvMat inputs = cvCreateMat(1, data.length, CV_32FC1);
		for (int i=0; i<data.length; i++){
			inputs.put(i, data[i]);
		}
		return inputs;
	}//end toCvMAt1 method
}//end GestureData class