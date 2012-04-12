package org.inca.algorithms;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_NN;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import com.googlecode.javacv.cpp.opencv_ml.*;


public class SVMRecg extends Algorithm{
	private int numTrnSamples;
	private int numCategories;
	private CvMat trainingData;
	private CvMat trainingCategories;
	private int size;
	private CvSVM svmClassifier;
	private static final int DEFAULT_TRAIN_SIZE = 10;
	private static final int DEFAULT_NUM_CAT = 10;
	private static final int DEFAULT_SIZE = 50;
	private static final String PATH = "unknownset\\";
	
	public SVMRecg(int numTrain, int numCat, int size){
		setTrainingSamples(numTrain);
		setCategories(numCat);
		this.size = size;
		trainingData = cvCreateMat(numTrnSamples * numCategories, 
										this.size * this.size, CV_32FC1);
		trainingCategories = cvCreateMat(numTrnSamples * numCategories, 1,
																CV_32FC1);
	}//end KNearestNRecg constructor
	
	public SVMRecg(){
		setTrainingSamples(DEFAULT_TRAIN_SIZE);
		setCategories(DEFAULT_NUM_CAT);
		this.size = DEFAULT_SIZE;
		trainingData = cvCreateMat(numTrnSamples * numCategories, 
										this.size * this.size, CV_32FC1);
		trainingCategories = cvCreateMat(numTrnSamples * numCategories, 1,
																CV_32FC1);
	}//end KNearestNRecg default constructor
	
	public String recognizeSymbol(String fileName){
		IplImage parseImage = null;
		IplImage sourceImage = null;
		CvMat row = new CvMat();
		CvMat data = new CvMat();
		
		sourceImage = cvLoadImage(PATH + fileName);
		parseImage = preprocessing(sourceImage, size, size);
		int r = (int)classify(parseImage, 1);
	
		//System.out.println(r);
		return Integer.toString(r);
	}//end test method
	
	private IplImage preprocessing(IplImage im, int nWidth, int nHeight){
		IplImage reImage = null; 
		IplImage im2 = IplImage.create(im.width(), im.height(),
														IPL_DEPTH_8U, 1);
		IplImage im3;
		
		im2 = bBox(im);
		
		int newSize = (im2.width() > im2.height()) ? im2.width():im2.height();
		
		im3 = IplImage.create(cvSize(newSize, newSize), IPL_DEPTH_8U, 1);
		reImage = IplImage.create(cvSize(nWidth, nHeight), 8, 1);
		
		cvResize(im3, reImage, CV_INTER_NN);
		
		return reImage;
	}//end preprocessing method
	
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
	
	private IplImage normalize(IplImage im){
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
	
	public double classify(IplImage image, int showResults){
		IplImage parseImage;
		CvMat data = new CvMat();
		CvMat nearest = cvCreateMat(1, 5, CV_32FC1);
		double result;
		//image processing
		parseImage = preprocessing(image, size, size);
		
		//set data
		IplImage img32 = cvCreateImage(cvSize(size, size), IPL_DEPTH_32F, 1);
		cvConvertScale(parseImage, img32, 0.0039215, 0);
		cvGetSubRect(img32, data, cvRect(0, 0, size, size));
		CvMat rowHeader = new CvMat();
		CvMat row1 = new CvMat();
		row1 = cvReshape(data, rowHeader, 0, 1);
		
		//find closest match
		result = svmClassifier.predict(row1, false);
		
		return result;
	}//end classify method
	
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
	
	private void getData(){
		IplImage sourceImage = IplImage.create(500, 500, IPL_DEPTH_8U, 1);
		IplImage parseImage = null;
		String fileName;
		String path = "dataset\\";
		CvMat row = new CvMat();
		CvMat data = new CvMat();
		//get images for each class
		//System.out.println("Getting data...Please wait");
		for(int i = 0; i < getNumCategories(); i++){
			for(int j = 0; j < getTrainingSamples(); j++){
				//get image in directories
				fileName = getPrefixName(i);			
				sourceImage = cvLoadImage(""+path+fileName+""+(i+1)+".png");
				if(sourceImage == null){
					System.out.println("Null Image");
				}
				
				parseImage = preprocessing(sourceImage, size, size);
				
				//set class labels
				cvGetRow(this.trainingCategories, row, i * numTrnSamples + j);
				cvSet(row, cvRealScalar(i));
				//set data
				cvGetRow(this.trainingData, row, i * numTrnSamples + j);
				
				IplImage image = cvCreateImage(cvSize(size, size), IPL_DEPTH_32F, 1);
				cvConvertScale(parseImage, image, 0.0039215, 0);
				cvGetSubRect(image, data, cvRect(0, 0, size, size));
				
				CvMat rowHeader = new CvMat();
				CvMat row1 = new CvMat();
				row1 = cvReshape(data, rowHeader, 0, 1);
				cvCopy(row1, row, null);
			}
		}
		//System.out.println("Data Processed");
	}//end method getData
	
	public void populateDatabase(){
		this.getData();
		this.train();
	}//end populateDatabase method
	
	private void train(){
		CvTermCriteria crit = new CvTermCriteria(1);
		crit.max_iter(100);
		crit.epsilon(0.0001);
		crit.type(1);

		CvSVMParams params = new CvSVMParams();
		params.kernel_type(CvSVM.RBF);
		params.svm_type(CvSVM.C_SVC);
		params.C(1);
		params.term_crit(crit);
		svmClassifier = new CvSVM();
		svmClassifier.train(trainingData, trainingCategories, null, null, params);//, 5, 
				//CvSVM.get_default_grid(CvSVM.C),
				//CvSVM.get_default_grid(CvSVM.GAMMA),
				//CvSVM.get_default_grid(CvSVM.P),
				//CvSVM.get_default_grid(CvSVM.NU),
				//CvSVM.get_default_grid(CvSVM.COEF),
				//CvSVM.get_default_grid(CvSVM.DEGREE), false);
		//cvSave("training.xml", trainingData);
	}//end train method
	/**
	 * 
	 * @param trainingSamples
	 */
	private void setTrainingSamples(int trainingSamples){
		this.numTrnSamples = trainingSamples;
	}//end setTrainingSamples method
	/**
	 * 
	 * @param numCat
	 */
	private void setCategories(int numCat){
		this.numCategories = numCat;
	}//end setCategories method
	/**
	 * 
	 * @return
	 */
	public int getTrainingSamples(){
		return this.numTrnSamples;
	}//end getTrainingSamples method
	/**
	 * 
	 * @return
	 */
	public int getNumCategories(){
		return this.numCategories;
	}//end getNumCategories method
	
	public static void main(String[] args){
		int numSamples = 50;
		int numCategories = 10;
		int sz = 100;
		
		SVMRecg test = new SVMRecg(numSamples, numCategories, sz);
		test.getData();
		test.train();
		
		System.out.println(test.recognizeSymbol("zero1.png"));
	}
}//end SVMRecg class