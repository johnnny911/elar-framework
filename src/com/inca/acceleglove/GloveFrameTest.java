package com.inca.acceleglove;

import com.idrt.Glove;
import com.idrt.GloveConnectionEvent;
import com.idrt.GloveConnectionListener;
import com.idrt.Handshape;

public class GloveFrameTest implements GloveConnectionListener{
	private AlgFrame frame;
	
	public GloveFrameTest()throws Exception{
		frame = new AlgFrame();
		frame.setVisible(true);
		createGlove();
	}//end constructor method
	
	private void createGlove()throws Exception{
		Handshape hand = null ;
		Glove glove = new Glove();
		glove.addGloveConnectionListener(this);
		frame.setGestureObject(hand, glove);
	}//end createGlove method
	
	public static void main(String[] args){
		try {
			new GloveFrameTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end main method

	@Override
	public void statusReceived(GloveConnectionEvent event) {
		Glove g = (Glove) event.getSource();
		g.close();
		System.out.println("The Glove is disconnected");
		System.exit(0);
	}
}//end GLoveGrameTest class
