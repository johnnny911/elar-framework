//Alphabet.java
package org.elar.decision;
import java.util.Hashtable;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-Framework
 *
 * Alphabet.java -- Hashtable object that maps vector positions to symbol in the 
 * recognition alphabet.
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

public class Alphabet {
	private Hashtable<String, Integer> sigma;
	
	/**
	 * Constructor. Accepts an integer value for the initial 
	 * size of the alphabet table.
	 * @param size	Integer value
	 */
	public Alphabet(int size){
		sigma = new Hashtable<String, Integer>(size);
	}//end default constructor
	/**
	 * Adds a key,value pair to the table.
	 * @param key	String
	 * @param value	Integer value
	 */
	public void addSymbol(String key, Integer value){
		sigma.put(key, value);
	}//end addSymbol method
	/**
	 * Returns an Integer object mapped to the String key.
	 * @param key	String value
	 * @return		Integer value mapped to String key
	 */
	public Integer getSymbolValue(String key){
		return sigma.get(key);
	}//end getSymbol method
	/**
	 * Removes the key,value pair from the table
	 * @param key	String key to object in table
	 */
	public void removeSymbol(String key){
		sigma.remove(key);
	}//end removeSymbol method
	/**
	 * Clears all table contents.
	 */
	public void clearAlphabet(){
		sigma.clear();
	}//end clearAlphabet method
	/**
	 * Returns the integer current size, i.e. number of items in 
	 * the table.
	 * @return		table.size and in integer.
	 */
	public int getSize(){
		return sigma.size();
	}//end getSize method
}//end Alphabet class