package org.elar.algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_core.*;

public class BPNNWeights {
	private String fileName;
	private int numInput;
	private int numOutput;
	private int numHidden;
	private static final String PATH = "weightsData\\";

	/**
	 * 
	 * @param fileName
	 * @param numInput
	 * @param numHidden
	 * @param numOutput
	 */
	public BPNNWeights(String fileName, int numInput, int numHidden,
						int numOutput){
		this.fileName = fileName;
		this.numHidden = numHidden;
		this.numInput = numInput;
		this.numOutput = numOutput;
	}//end BPNNWeights constructor
	/**
	 * 
	 * @param hWeights
	 * @param fileHweights
	 * @param oWeights
	 * @param fileOWeights
	 */
	public void saveWeightsCV(CvMat hWeights, String fileHweights,
							CvMat oWeights, String fileOWeights){
		cvSave("fileHWeghts", hWeights);
		cvSave("fileOWeights", oWeights);
	}//end saveWeightsCV method
	/**
	 * 
	 * @param hWeights
	 * @param oWeights
	 * @throws Exception
	 */
	public void saveWeights(double[][] hWeights, double[][] oWeights)
													throws Exception{
		FileWriter stream = new FileWriter(fileName);
		BufferedWriter out = new BufferedWriter(stream);
		int count = 0;
		//hWeights
		for(int i = 0; i < numInput; i++){
			out.write(new String(""+i));
			for(int j= 0; j < numHidden; j++){
				out.write(new String(" "+hWeights[j][i]));
				count++;
				if(count > 6){
					out.write(new String("\n"));
					count = 0;
				}
			}
		}//end out loop
		//oWeights
		for(int i = 0; i < numHidden; i++){
			out.write(new String(""+i));
			for(int j= 0; j < numOutput; j++){
				out.write(new String(" "+oWeights[j][i]));
				count++;
				if(count > 6){
					out.write(new String("\n"));
					count = 0;
				}
			}
		}//end out loop
		out.close();
	}//end saveWeights method
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public double[][] getHiddenSet(String fileName)throws Exception{
		double[][] reMatrix = new double[numHidden][numInput];
		File inFile = new File(fileName);
		Scanner in = new Scanner(inFile);
		
		for (int i = 0; i < numInput; i++){
			  for (int j = 0; j < numHidden; j++){
			    reMatrix[j][i] = in.nextDouble();
			  }
		}//end loop
		return reMatrix;
	}//end getHiddenSet method
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public double[][] getOuputSet(String fileName)throws Exception{
		double[][] reMatrix = new double[numOutput][numHidden];
		File inFile = new File(fileName);
		Scanner in = new Scanner(inFile);
		
		for (int i = 0; i < numHidden; i++){
			  for (int j = 0; j < numOutput; j++){
			    reMatrix[j][i] = in.nextDouble();
			  }
		}//end loop
		return reMatrix;
	}//end getOutputSet method
}//end BPNNWeights