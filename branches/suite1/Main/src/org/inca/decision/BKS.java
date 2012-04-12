package org.inca.decision;

import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class BKS {
	private Hashtable<String, BKSUnit> bkstable;
	
	public BKS(){
		bkstable = new Hashtable<String, BKSUnit>();
	}//end BKS constructor
	
	public void addTuple(String guesses, int size){
		if(bkstable.get(guesses) == null){
			bkstable.put(guesses, new BKSUnit(guesses, size));
			System.out.println("Input OK");
		}else{//debugging only
			System.out.println("Tuple Already exists.");
		}
	}//end addTuple method
	
	public BKSUnit getTuple(String tuple){
		return bkstable.get(tuple);
	}//end getTuple method
	
	public void trainSpace(String tuple, String correctAns){
		bkstable.get(tuple).trainBKS(correctAns);
	}//end trainSpace method
	
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
	
	public void readDatabase(String filename){
		
	}//end readDatabase method
}//end BKS class
