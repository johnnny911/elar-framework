package com.inca.acceleglove;


public class GloveInteraction{
	
	public static void main(String[] args){
		AlgFrame af = new AlgFrame();
		af.setVisible(true);
		Gesture g = new Gesture("1,4,5,6,9,8," +
								"3,6,2,1,4,2," +
								"2,0,6,7,1,3");
		int left = g.get(GesturePoint.THUMB_X);
	}
}
	
