package org.elar.algorithms;
/**
 * 
 * @author James Neilan
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