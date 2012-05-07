package org.elar.algorithms;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCloneImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca P.O.C.
 *
 * ImageProcessing.java - 
 * for a symbol over the sample space. Adaptation of a back propagation 
 * neural network, from "Algorithms for Image Processing and Computer Vision", 
 * J.R.Parker, 2010
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
public class ImageProcessing {
	
	/**
	 * 
	 */
	public ImageProcessing(){
		
	}//end ImageProcessing constructor
	/**
	 * 
	 * @param im
	 * @return
	 */
	public IplImage normalize(IplImage im){
		IplImage reImage = IplImage.create(im.width(), im.height(), IPL_DEPTH_8U, 1);
		double vMin = 256, vMax = 0;
		double scale = 1.0;
		CvScalar scalar = new CvScalar();
		
		for(int i = 0; i < im.height(); i++){
			for(int j = 0; j < im.width(); j++){
				if(cvGet2D(im, i,j).getVal(0) < vMin){
					vMin = cvGet2D(im, i, j).getVal(0);
				}
				if(cvGet2D(im, i,j).getVal(0) > vMax){
					vMax = cvGet2D(im, i, j).getVal(0);
				}
			}
		}//end nested for loop
		return reImage;
	}//end normalize method
	/**
	 * 	
	 * @param mods
	 * @return
	 */
	public IplImage bBox(IplImage mods){
		int sr = 10000, lr = 0, sc = 10000, lc = 0, n, m;
		IplImage x = IplImage.create(mods.width(), mods.height(), IPL_DEPTH_8U, 1);
		IplImage reImage = new IplImage();
		CvScalar scalar = new CvScalar();
		CvSize size = new CvSize();
		
		x = cvCloneImage(mods);
		for(int i = 0; i < x.height(); i++){
			for(int j = 0; j < x.width(); j++){
				if(cvGet2D(x, i, j).getVal(0) == 0){
					lr = i;
					if(lc < j){
						lc = j;
					}
					if(sr > i){
						sr = i;
					}
					if(sc > j){
						sc = j;
					}
				}
			}
		}//end nested for loop
		n = lr - sr + 1;
		m = lc - sc +1;
		size.height(n);
		size.width(m);
		reImage = cvCreateImage(size, x.depth(), x.nChannels());
		
		for(int i = sr; i <= lr; i++){
			for(int j = sc; j <= lc; j++){
				scalar = cvGet2D(x, i, j);
				cvSet2D(reImage, i - sr, j - sc, scalar);
			}
		}//end nested for loop
		
		return reImage;
	}//end bBox method
	/**
	 * 
	 * @param im
	 * @param name
	 */
	public void createNamedWindow(IplImage im, String name){
		CanvasFrame canvasFrame = new CanvasFrame(name);
        canvasFrame.setCanvasSize(im.width(), im.height());
		canvasFrame.showImage(im);
	}//end createNamedWindow method
}//end ImageProcessing class