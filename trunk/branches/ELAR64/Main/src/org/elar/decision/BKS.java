package org.elar.decision;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Thesis and Research Work.
 *
 * BKS.java - Class for creating the behavior knowledge space for tuples of 
 * algorithms in an ensemble recognition system. Depends upon the BKSUnit.java
 * class object as part of the hash table.
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
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class BKS {
	private Hashtable<String, BKSUnit> bkstable;
	/**
         * Constructor
         */
	public BKS(){
		bkstable = new Hashtable<String, BKSUnit>();
	}//end BKS constructor
	/**
         * Methods adds the tuple containing guesses and size of tuple to the
         * hash table.
         * @param guesses   tuple of algorithm guesses
         * @param size      size of tuple
         */
	public void addTuple(String guesses, int size){
		if(bkstable.get(guesses) == null){
			bkstable.put(guesses, new BKSUnit(guesses, size));
			System.out.println("Input OK");
		}else{//debugging only
			System.out.println("Tuple Already exists.");
		}
	}//end addTuple method
	/**
         * Method returns the BKSUnit mapped to tuple.
         * @param tuple     string of algorithm guesses
         * @return          BKSUnit of mapped tuple
         */
	public BKSUnit getTuple(String tuple){
		return bkstable.get(tuple);
	}//end getTuple method
	/**
         * Adds the correct answer to the BKSUnit tuple.
         * @param tuple          algorithm answers
         * @param correctAns     the correct label of the input data
         */
	public void trainSpace(String tuple, String correctAns){
		bkstable.get(tuple).trainBKS(correctAns);
	}//end trainSpace method
	/**
         * Returns the tuples best representative class of unknown symbol.
         * @param tuple     guess from algorithms
         * @return          best class label from unknown tuple
         */
	public String getGuess(String tuple){
		if(bkstable.get(tuple) != null){
			return bkstable.get(tuple).getBestClass();
		}
		return "No Such Tuple in Table.";
	}//end getGuess method
	//not correct--------
	public void writeDatabase(){
		FileWriter stream;
		try {
			stream = new FileWriter("bksDB", true);
			BufferedWriter out = new BufferedWriter(stream);
			for(int i = 0; i < bkstable.size(); i++){
				out.write(bkstable.keySet().toString());
				//finish
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end writeDatabase method
	/**
         * TO DO
         * @param filename 
         */
	public void readDatabase(String filename){
            //todo store BKS table.
	}//end readDatabase method
}//end BKS class
