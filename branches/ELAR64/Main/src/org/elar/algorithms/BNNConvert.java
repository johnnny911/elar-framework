package org.elar.algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;

/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca P.O.C.
 *
 * BNNConvert.java - Class that takes a passed image, bounds the symbol
 * and creates a 48 feature vector for the back Propagation Neural Net.
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
public class BNNConvert {
	private IplImage im;
	private File outputFile;
	
	/**
	 * Constructor
	 * @param 	im	IplImage to convert
	 */
	public BNNConvert(IplImage im){
		this.im = im;
	}//end constructor method
	/**
	 * Set the image file to convert. Accepts name
	 * @param 	imageName	string value name
	 * @throws 	Exception	Null Image exception
	 */
	public void setupImageFile(String imageName)throws Exception{
		im = cvLoadImage(imageName);
		if(im == null){
			throw new Exception("ImageFile == Null");
		}
	}//end setupImageFile
	/**
	 * Creates and displays the image in a frame window
	 * @param 	name	String name of image window
	 */
	private void createNamedWindow(String name){
		CanvasFrame canvasFrame = new CanvasFrame(name);
        canvasFrame.setCanvasSize(im.width(), im.height());
		canvasFrame.showImage(im);
	}//end createNamedWindow method
	/**
	 * 
	 * @param image
	 * @param name
	 */
	private void createNamedWindow(IplImage image, String name){
		CanvasFrame canvasFrame = new CanvasFrame(name);
        canvasFrame.setCanvasSize(image.width(), image.height());
		canvasFrame.showImage(image);
	}//end createNamedWindow method
	/**
	 * 
	 * @param outputName
	 * @throws Exception
	 */
	public void setOutputFile(String outputName)throws Exception{
		outputFile = new File(outputName);
		if(outputFile == null){
			throw new Exception("OutputFile == Null");
		}
	}//end setOutputFile method
	/**
	 * 
	 * @throws Exception
	 */
	public void convertImage()throws Exception{
		IplImage mods = new IplImage();
		IplImage mods2 = new IplImage();
		IplImage nnImage = new IplImage();
		//normalizes pixel values - converts grey levels to 0-255
		mods = normalize();
		//bounds the symbol - resizes image (min size)
		mods2 = bBox(mods);
		//createNamedWindow(mods2, "Scaled");
		nnImage = scaleDown(mods2, 8, 6);
		//createNamedWindow(mods, "Scaled");
		writeOutputFile(nnImage);
	}//end convertImage method
	/**
	 * 
	 * @return
	 */
	private IplImage normalize(){
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
		
		scale = vMax - vMin;
		
		for(int i = 0; i < im.height(); i++){
			for(int j = 0; j < im.width(); j++){
				scalar.setVal(0, (((cvGet2D(im, i,j).getVal(0)-vMin)/scale)*255.0));
				cvSet2D(reImage, i, j, scalar);
			}
		}//end nested for loop
		
		return reImage;
	}//end normalize method
	/**
	 * 
	 * @param mods
	 * @return
	 */
	private IplImage bBox(IplImage mods){
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
	 * @param mods2
	 * @param newR
	 * @param newC
	 * @return
	 */
	private IplImage scaleDown(IplImage mods2, int newR, int newC){
		IplImage reImage = null;//IplImage.create(mods2.width(), mods2.height(), IPL_DEPTH_8U, 1);
		IplImage x = IplImage.create(mods2.width(), mods2.height(), IPL_DEPTH_8U, 1);
		int ii, jj, k, colk, rowk = 0, rs = 0, cs;
		double rFactor, cFactor, accum=0.0, area, xf;
		double []rw = new double[25];
		double []cw = new double[25];
		CvScalar scalar = new CvScalar();
		CvSize size = new CvSize();
		
		//Create a new, smaller image
		size.height(newR);
		size.width(newC);
		reImage = cvCreateImage(size, mods2.depth(), mods2.nChannels());
		x = cvCloneImage(mods2);
		//compute scale factor for rows and columns
		rFactor = (double)newR / (double)(x.height());
		cFactor = (double)newC / (double)(x.width());
		rFactor = 1.0 / rFactor;
		cFactor = 1.0 / cFactor;
		area = cFactor * rFactor;
		
		//for each pixel in the new image, compute a grey level
		//based on interpolation from the original image
		for(int i = 0; i < newR; i++){
			for(int j = 0; j < newC; j++){
				try{
				//set up the row re-scale
				rw[0] = bigger(i*rFactor) - i*rFactor;
				rs = (int)(Math.floor(i*rFactor)+0.001);
				k = 1;
				xf = rFactor - rw[0];
				if (k >= 25){
					System.out.println();
				}
				while(xf >= 1.0){
					rw[k++] = 1.0;
					xf = xf - 1.0;
				}
				if(xf < 0.0001){
					k--;
				}else{
					rw[k] = xf;
				}
				rowk = k;
				
				
				//set up the column re-scale
				cw[0] = bigger(j*cFactor) - j*cFactor;
				cs = (int) (Math.floor(j*cFactor)+0.001);  k=1;
				xf = cFactor - cw[0];

				while (xf >= 1.0) 
				{
					cw[k++] = 1.0;
					xf = xf - 1.0;
				}
				if (xf < 0.0001) k--; 
				 else cw[k] = xf;
				colk = k;
				
				//collect and weight pixels from the original into the new pixel
				accum = 0.0;
				for(ii = 0; ii <= rowk; ii++){
					if(ii+rs >= x.height()){
						continue;
					}
					for(jj = 0; jj <= colk; jj++){
						if(jj+cs >= x.width()){
							continue;
						}
						accum += rw[ii]*cw[jj]*(cvGet2D(x, rs+ii, cs+jj).getVal(0));
					}
				}
				}
				catch (ArrayIndexOutOfBoundsException e){
					System.out.println();
				}
				accum = accum / area;
				if(accum > 255.0){
					//System.out.println(accum+"at"+i+j);
				}
				scalar.setVal(0, accum+0.5);
				cvSet2D(reImage, i, j, scalar);
				
			}
		}//end re-scaling nested for loop
		
		return reImage;
	}//end scaleDown method
	/**
	 * 
	 * @param x
	 * @return
	 */
	private double bigger(double x){
		double y;
		
		y = Math.ceil(x);
		if(y == x){
			y = y + 1.0;
		}
		return y;
	}//end bigger method
	/**
	 * 
	 * @param outImage
	 * @throws Exception
	 */
	private void writeOutputFile(IplImage outImage) throws Exception{
		FileWriter stream = new FileWriter("nnDataout");
		BufferedWriter out = new BufferedWriter(stream);
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 6; j++){
				out.write(new String(""+
						(double)cvGet2D(outImage, i, j).getVal(0)/255.0)+" ");
			}
			out.write("\n");
		}
		out.close();
		stream.close();
	}//end writeOutputFile method
	
	public void createDatabase(String imageFile, int numCat)throws Exception{
		//String path = "dataset\\";
		String path = "D:\\Programming\\datasets2\\image_cv\\set4\\";
		im = cvLoadImage(path+imageFile);
		IplImage mods = new IplImage();
		IplImage mods2 = new IplImage();
		IplImage nnImage = new IplImage();
		//normalizes pixel values - converts grey levels to 0-255
		mods = normalize();
		//bounds the symbol - resizes image (min size)
		mods2 = bBox(mods);
		nnImage = scaleDown(mods2, 8, 6);
		//createNamedWindow(mods, "Scaled");
		writeOutputFile2(nnImage, numCat);
	}
	
	private void writeOutputFile2(IplImage outImage, int numCat) throws Exception{
		FileWriter stream = new FileWriter("nnDatabasecv14", true);
		BufferedWriter out = new BufferedWriter(stream);
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 6; j++){
				out.write(String.format("%.3f ",
						(double)cvGet2D(outImage, i, j).getVal(0)/255.0));
			}
			if(i == 7){
				out.write("" + numCat + " ");
			}
			out.write("\n");
		}
		out.close();
		stream.close();
	}//end writeOutputFile method
	
	static String getPrefixName(int i){
		if(i == 0) return "zero";
		if(i == 1) return "one";
		if(i == 2) return "two";
		if(i == 3) return "three";
		if(i == 4) return "four";
		if(i == 5) return "five";
		if(i == 6) return "six";
		if(i == 7) return "seven";
		if(i == 8) return "eight";
		if(i == 9) return "nine";
		return "null";
	}//end getPrefixName method
	/**
	 * Main for regression testing
	 * @param args
	 */
	
	public static void main(String[] args){
		BNNConvert test = new BNNConvert(new IplImage());
		try{
			//test.setupImageFile("dig1.pbm");
			//test.createNamedWindow("NN");
			//test.setOutputFile("nnDataout.txt");
			//test.convertImage();
			
			for(int numInst = 145; numInst < 151; numInst++){
				System.out.println("Instance: " + numInst);
				for(int numCat = 0; numCat < 10; numCat++){
					test.createDatabase(getPrefixName(numCat)+(numInst)+".png",
							numCat);
					
				}
			}
			
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
		//terminate
		System.exit(0);
	}//end main method
	
}//end BNNConvert class
