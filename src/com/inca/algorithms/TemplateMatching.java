package com.inca.algorithms;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.inca.main.ConfidenceVector;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class TemplateMatching extends Algorithm{
	private int dbSize;
	
	public TemplateMatching(){
		
	}//end constructor
	
	public TemplateMatching(int dbSize){
		this.dbSize = dbSize;
	}//end constructor
	
	public String match(String unknownSymbol){
		String inPath = "dataset\\";
		String unKn = "unknownset\\";
		IplImage inImage;
		ImageProcessing im;
		IplImage result;
		IplImage tempImage;
		String guess;
		ConfidenceVector tm = new ConfidenceVector("TM");

		IplImage sourceImage = cvLoadImage(unKn + unknownSymbol);
		
		for(int i = 0; i < dbSize; i++){
			
			inImage = cvLoadImage(inPath + getPrefixName(i)+"1.png");
			im = new ImageProcessing();
			tempImage = im.bBox(inImage);
			
			result = cvCreateImage(cvSize(sourceImage.width()-tempImage.width()+1,
					sourceImage.height()-tempImage.height()+1), IPL_DEPTH_32F, 1);
			
			cvZero(result);
			
			cvMatchTemplate(sourceImage, tempImage, result, CV_TM_CCORR_NORMED);
			
			double[] min = new double[1];
			double[] max = new double[1];
			CvPoint minLoc = new CvPoint(); 
			CvPoint maxLoc = new CvPoint();
			cvMinMaxLoc(result, min, max, minLoc, maxLoc, null);
			
			cvRectangle(sourceImage, maxLoc, 
								cvPoint(maxLoc.x()+tempImage.width(), 
								maxLoc.y()+tempImage.height()), CvScalar.RED, 1, 8, 0);
			
			//System.out.println(max[0]);
			//System.out.println(min[0]);
	
			tm.addElement(max[0]);
		}
		//im = new ImageProcessing();
		//im.createNamedWindow(sourceImage, "result");
		//cvWaitKey(0);
		return guess = ""+tm.getIndexMax();
		
	}//end match method
	
	private String getPrefixName(int i){
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
	
	@Override
	public String recognizeSymbol(String file) throws Exception {
		// TODO Auto-generated method stub
		return match(file);
	}

	@Override
	public void populateDatabase() throws Exception {
		// TODO Auto-generated method stub
		this.dbSize = 10;
	}
	/*
	public static void main(String[] args){
		TemplateMatching match = new TemplateMatching(10);
		System.out.println(match.match("(enter name)"));
	}
	*/
}//end TemplateMatching class