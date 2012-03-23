package com.inca.algorithms;

import java.io.File;
import java.util.Scanner;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca P.O.C.
 *
 * BNNetRecg.java - 
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
public class BPNNetRecg extends Algorithm {
	private double database[];
	private int numberOfInputs = 0;
	private int numberOfHidden = 0;
	private int numberOfOutputs = 0;
	private double inputs[] = null;			//input values in the first layer
	private double hWeights[][] = null;		//weights for the hidden layer. hWeights [i][j] is the 
											//weight for the hidden node i from input node j.
	private double hidden[] = null;			//outputs from the hidden layer
	private double vHidden[] = null;
	private double errHidden[] = null;		//errors in hidden nodes
	private double oWeights[][] = null;		//weights for the output layer, as before.
	private double outputs[] = null;		//final outputs
	private double vOutputs[] = null;
	private double errOut[] = null; 		//errors in output nodes
	private double should[] = null;			//correct output vector for training datum.
	private static final double LEARNING_RATE = 0.3;
	private int actual = 0;
	private static final int DEFAULT_IN = 48;
	private static final int DEFAULT_HIDDEN = 96;
	private static final int DEFAULT_OUT = 10;
	private static final String DEFAULT_DATA_OUT = "nnDataOut";
	//private static final String PATH = "unknownset\\";
	//private static final String PATH = "dataset2\\";
	private static final String PATH = "D:\\Programming\\datasets2\\image_cv\\dataset1\\";
	/**
	 * 
	 * @param numIn
	 * @param numHidden
	 * @param numOut
	 */
	public BPNNetRecg(int numIn, int numHidden, int numOut){
		setNodeParams(numIn, numHidden, numOut);
		setup();
		initializeAllWeights();
	}//end BPNNetRecg constructor
	
	public BPNNetRecg(){
		setNodeParams(DEFAULT_IN, DEFAULT_HIDDEN, DEFAULT_OUT);
		setup();
		initializeAllWeights();
	}//end BPNNetRecg constructor
	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void trainNet(String fileName)throws Exception{
		double errorTerm = 0.0;
		int dSet = 1;
		boolean loopCheck = false;
		File trainingFile = new File(fileName);
		Scanner in = new Scanner(trainingFile);
		while(loopCheck = getInputs(in)){
			//System.out.println("Training on the dataset.");
			computeAllHidden();
			computeAllOutputs();
			//weight errors propagate back
			computeTrainingOutputs();
			computeOutputError();
			computeHiddenError();
			//update nodes
			updateOutput();
			updateHidden();
			//compute error term
			errorTerm = computeErrorTerm();
			//System.out.println("\nThe set "+dSet+" error term is: "+errorTerm);
			dSet++;
		}//end training loop
		in.close();
	}//end trainNet method
	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public String recognizeSymbol(String fileName)throws Exception{
		String guess = null;
		BNNConvert out = new BNNConvert(new IplImage());
		out.setupImageFile(PATH + fileName);
		out.convertImage();
		File dataFile = new File(DEFAULT_DATA_OUT);
		Scanner in = new Scanner(dataFile);
		boolean loopCheck = false;
		//apply the NN to the remaining inputs
		getInputRec(in);
		computeAllHidden();
		computeAllOutputs();
		guess = ""+outputResults2();
		
		in.close();
		return guess;
	}//end recognizeSymbol method
	
	private int outputResults2(){
		double max = 0.0;
		for(int i = 0; i < numberOfOutputs; i++){
			//System.out.println("Out: " + i + " Value: " + outputs[i]);
			if(outputs[i] > max){
				max = outputs[i];
			}
		}
		int guess = getMaxIndex(max);
		return guess;
	}
	
	private int getMaxIndex(double max){
		int guess = 0;
		for(int i = 0; i < numberOfOutputs; i++){
			if(outputs[i] == max){
				guess = i;
			}
		}
		return guess;
	}
	/**
	 * 
	 * @param numIn
	 * @param numHidden
	 * @param numOut
	 */
	private void setNodeParams(int numIn, int numHidden, int numOut){
		this.numberOfInputs = numIn;
		this.numberOfHidden = numHidden;
		this.numberOfOutputs = numOut;
	}//end getParams method
	/**
	 * 
	 * @param node
	 */
	private void computeHidden(int node){
		
		double x = 0;
		
		for(int i = 0; i < numberOfInputs; i++){
			x += inputs[i]*hWeights[node][i];
		}
		hidden[node] = outputFunction(x);
		vHidden[node] = x;
	}//end computeHidden method
	/**
	 * 
	 * @param x
	 * @return
	 */
	private double outputFunction(double x){
		return (1.0/(1.0 + Math.exp(-x))); //sigmoidal
		//return x; //linear
	}//end outputFunction method
	/**
	 * 
	 * @param node
	 */
	private void computeOutput(int node){
		double x = 0;
		for(int i = 0; i < numberOfHidden; i++){
			x += hidden[i]*oWeights[node][i];
		}
		outputs[node] = outputFunction(x);
		vOutputs[node] = x;
	}//end computeOutput method
	/**
	 * 
	 */
	private void computeAllHidden(){
		for(int i = 0; i < numberOfHidden; i++){
			computeHidden(i);
		}
	}//end computeAllHiden method
	/**
	 * 
	 */
	private void computeAllOutputs(){
		for(int i = 0; i < numberOfOutputs; i++){
			computeOutput(i);
		}
	}//end computeAllOutputs method
	/**
	 * 
	 * @return
	 */
	private int computeOutputError(){
		int x = 0;
		for(int i = 0; i < numberOfOutputs; i++){
			errOut[i] = (should[i]-outputs[i])*ofDerivative(vOutputs[i]);
			x += errOut[i];
		}
		
		return x;
	}//end computeOutputError method
	/**
	 * 
	 * @param x
	 * @return
	 */
	private double ofDerivative(double x){
		double a = 0.0;
		a = outputFunction(x);
		return 1.0; //linear
		//return a*(1.0-a);
	}//end ofDerivative method
	/**
	 * 
	 */
	private void computeHiddenError(){
		for(int i = 0; i < numberOfHidden; i++){
			errHidden[i] = computeHiddenNodeError(i);
		}
	}//end computeHiddenError method
	/**
	 * 
	 * @param node
	 * @return
	 */
	private double computeHiddenNodeError(int node){
		double x = 0.0;
		for(int i = 0; i < numberOfOutputs; i++){
			x += errOut[i]*oWeights[i][node];
		}
		return ofDerivative(vHidden[node])*x;
	}//end computeHIddenNodeError method
	/**
	 * 
	 * @return
	 */
	private double computeErrorTerm(){
		double x = 0.0;
		for(int i = 0; i < numberOfOutputs; i++){
			x += (errOut[i]*errOut[i]);
		}
		return x/2.0;
	}//end computeErrorTerm method
	/**
	 * 
	 */
	private void computeTrainingOutputs(){
		//System.out.println("The output should be:");
		//System.out.println("0 1 2 3 4 5 6 7 8 9");
		for(int i = 0; i < numberOfOutputs; i++){
			if( i == actual){
				should[i] = 1.0;
			}else{
				should[i] = 0.0;
			}
			//System.out.print(" " + should[i]);
		}
	}//end computeTrainingOutputs method
	/**
	 * 
	 * @return
	 */
	private double weightInit(){
		return (Math.random() - 0.5);
	}//end initWeight method
	/**
	 * 
	 */
	private void initializeAllWeights(){
		for(int i = 0; i < numberOfInputs; i++){
			for(int j = 0; j < numberOfHidden; j++){
				hWeights[j][i] = weightInit();
			}
		}
		for(int i = 0; i < numberOfHidden; i++){
			for(int j = 0; j < numberOfOutputs; j++){
				oWeights[j][i] = weightInit();
			}
		}
	}//end initializeAllWieghts method
	/**
	 * 
	 */
	private void setup(){
		inputs = new double[numberOfInputs];
		hWeights = new double[numberOfHidden][numberOfInputs];
		hidden = new double[numberOfHidden];
		vHidden = new double[numberOfHidden];
		errHidden = new double[numberOfHidden];
		oWeights = new double[numberOfOutputs][numberOfHidden];
		outputs = new double[numberOfOutputs];
		vOutputs = new double[numberOfOutputs];
		errOut = new double[numberOfOutputs];
		should = new double[numberOfOutputs];
	}//end setup method
	/**
	 * 
	 * @param in
	 * @return
	 */
	private boolean getInputs(Scanner in){
		try{
			double num;
			for(int i = 0; i < numberOfInputs; i++){
				num = in.nextDouble();
				inputs[i] = num;
				if(num < 0.0){
					return false;
				}
			}
			actual = in.nextInt();
		}
		catch(Exception e){
			//System.out.println("End of file. Exiting input loop...");
			return false;
		}
		
		return true;
	}//end getInputs method
	
	private void getInputRec(Scanner in){
		try{
			double num;
			for(int i = 0; i < numberOfInputs; i++){
				num = in.nextDouble();
				inputs[i] = num;
				if(num < 0.0){
					//return -1;
				}
			}
			//actual = in.nextInt();
		}
		catch(Exception e){
			//System.out.println("End of file. Exiting input loop...");
			//return -1;
		}
		
		//return true;
	}//end getInputRec method
	
	/**
	 * 
	 */
	private void updateOutput(){
		for(int i = 0; i < numberOfOutputs; i++){
			for(int j = 0; j < numberOfHidden; j++){
				oWeights[i][j] += LEARNING_RATE*errOut[i]*hidden[j];
			}
		}
	}//end updateOutput method
	/**
	 * 
	 */
	private void updateHidden(){
		for(int i = 0; i < numberOfHidden; i++){
			for(int j = 0; j < numberOfInputs; j++){
				hWeights[i][j] += LEARNING_RATE*errHidden[i]*inputs[j];
			}
		}
	}//end updateHidden method
	/**
	 * 
	 */
	public void outputResults(){
		int j = 0;
		for(int i = 0; i < numberOfOutputs; i++){
			System.out.println(outputs[i]);
			if(outputs[i] > outputs[j]){
				j = i;
			}
			System.out.println("Actual " + i + " NN classified as " + j);			
		}
	}//end outputResults method
	
	private void setNumOutputs(int numOut){
		this.numberOfOutputs = numOut;
	}
	
	public void populateDatabase()throws Exception{
		this.trainNet("nnDatabasecv11");
	}
	/**
	 * Main method used for regression testing.
	 * @param args
	 */
	/*
	public static void main(String[] args){
		BPNNetRecg test = new BPNNetRecg(48, 96, 10);
		try {
			//test.trainNet("datapc1");
			test.trainNet("nnDatabase2.txt");
			//test.setNumOutputs(1);
			//test.recognizeSymbol("nnDataOut");
			BPNNWeights run = new BPNNWeights("weightsOut.txt", 48, 96, 10);
			run.saveWeights(hWeights, oWeights);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//end main method
	*/
}//end BPNNet class