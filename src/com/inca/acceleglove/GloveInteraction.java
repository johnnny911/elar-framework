package com.inca.acceleglove;

import java.util.Random;


public class GloveInteraction{
	
	public static void main(String[] args){
		AlgFrame af = new AlgFrame();
		af.setVisible(true);
		GestureData fuGest = new GestureData("0,6,5,6,9,4," +
											"3,6,2,4,3,4," +
											"2,0,6,7,2,4");
		GestureData tuGest = new GestureData("5,4,3,2,1," +
											"5,2,7,1,4," +
											"5,0,2,1,3");
		NeuralNet nn = new NeuralNet();
		KNearest knn = new KNearest();
		for (int j=0; j<100; j++){
		Random rand = new Random();
			// Train FU
			for (int i=0; i<GestureData.NUM_POINTS; i++){
				if (i != 5 && i != 11 && i != 17)
					fuGest.set(i, rand.nextInt(10) + 6);
			}
			nn.train(fuGest,  Gesture.FU);
			knn.train(fuGest, Gesture.FU);
			// Train Thumbsup
			for (int i=0; i<GestureData.NUM_POINTS; i++){
				if (i != 0 && i != 5 && i != 10)
					tuGest.set(i, rand.nextInt(10) + 6);
			}
			nn.train(tuGest, Gesture.THUMBSUP);
			knn.train(tuGest, Gesture.THUMBSUP);
		}
		
		System.out.println( Gesture.get(knn.predict(fuGest)) );
		System.out.println( Gesture.get(knn.predict(tuGest)) );
	}
}
	
