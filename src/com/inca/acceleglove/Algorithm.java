package com.inca.acceleglove;

public abstract class Algorithm {
	private String name;
	
	Algorithm(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract int predict(GestureData inputs);
	public abstract void train(GestureData inputs, Gesture actual);

}
