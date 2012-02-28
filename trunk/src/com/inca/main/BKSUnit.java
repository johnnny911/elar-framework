package com.inca.main;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;


public class BKSUnit {
	private int[] guessNum;
	private String tuple;
	
	public BKSUnit(String guesses, int size){
		this.tuple = guesses;
		this.guessNum = new int[size];
		initVect();
	}//end BKSUnit
	
	private void initVect(){
		for(int i = 0; i < guessNum.length; i++){
			guessNum[i] = 0;
		}
	}//end initVect method
	
	public void trainBKS(String correctAns){
		addGuessNum(correctAns);
	}//end trainBKS method
	
	public String getBestClass(){
		int max = 0;
		int idx = 0;
		for(int i = 0; i < guessNum.length; i++){
			if(max <= guessNum[i]){
				max = guessNum[i];
				idx = i;
			}
		}
		return ""+tuple.charAt(idx);
	}//end getBestClass method
	
	private String getValue(String ans){
		if(ans.equals("zero"))  return "0"; if(ans.equals("one"))    return "1";
		if(ans.equals("two"))   return "2";  if(ans.equals("three")) return "3";
		if(ans.equals("four"))  return "4"; if(ans.equals("five"))   return "5";
		if(ans.equals("six"))   return "6";  if(ans.equals("seven")) return "7";
		if(ans.equals("eight")) return "8";if(ans.equals("nine"))    return "9";
		return null;
	}//end getValue method
	
	private void addGuessNum(String correctAns){
		if(tuple.contains(getValue(correctAns))){
			guessNum[tuple.indexOf(getValue(correctAns))] += 1;
		}
		//otherwise tuple does not contain correct guess
	}//end addGuessNum method
	
	public int getTotalSampleSize(){
		int total = 0;
		for(int i = 0; i < guessNum.length; i++){
			total += guessNum[i];
		}
		return total;
	}//end getTotalSampleSize method
}//end BKSUnit class
