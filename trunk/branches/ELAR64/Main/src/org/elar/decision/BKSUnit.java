package org.elar.decision;

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
public class BKSUnit {
	private int[] guessNum;
	private String tuple = null;
	
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
			for(int i = 0; i < guessNum.length; i++){
				if(tuple.charAt(i) == getValue(correctAns).toCharArray()[0]){
					guessNum[i] += 1;
				}
			}
			/*
			for(int i = 0; i < guessNum.length; i++){
				System.out.println(guessNum[i]);
			}
			*/
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
	@Override
	public String toString(){
		return tuple;
	}
}//end BKSUnit class
