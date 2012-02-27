//TabletTraining.java
package com.inca.tablet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.inca.algorithms.BNNConvert;
import com.inca.main.IncaDecision;

import jpen.PKind;
import jpen.PLevel;
import jpen.PLevelEvent;
import jpen.PenManager;
import jpen.event.PenAdapter;
import jpen.owner.multiAwt.AwtPenToolkit;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca-P.O.C.
 *
 * TabletTraining.java - Simple GUI interface for the capture of input 
 * symbols from the pen tablet. Designed for input to the Inca Framework.
 * Adapted from JavaCV examples.http://code.google.com/p/javacv/
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
public class TabletTraining extends PenAdapter implements ActionListener{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private JFrame frame = new JFrame("INCA Tablet Training Input GUI");
	private JPanel panel = new JPanel();
	private JButton save = new JButton("Save");
	private JButton delete = new JButton("Delete");
	private JButton recog = new JButton("Recog");
	private JLabel label = new JLabel("Annotation:");
	private JTextField text = new JTextField("(enter name)");
	private JLabel countval = new JLabel(" 0 ");
	private JCheckBox incrLrn = new JCheckBox("IncrLrn");
	private static final String DATAPATH1 = "dataset\\";
	private static final String DATAPATH2 = "dataset2\\";
	private BufferedImage bi = null;
	private Graphics2D g2d = null, g2d2 = null;
	private Point2D.Float prevLoc = null;// previous cursor location
	private Point2D.Float loc = null;///current cursor location 
	//brush dynamics
	private float brushSize;
	private float opacity;
	private BasicStroke stroke;
	private int countSaves;
	
	/**
	 * Constructor. Creates the input GUI pane.
	 */
	public TabletTraining(){
		prevLoc = new Point2D.Float();
		loc = new Point2D.Float();      
		//setup the panel and the panel
		panel.setBackground(Color.white);
		//set the cross-hairs to the cursor to see position
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		frame.setContentPane(panel);
		text.setSize(60, 20);
		panel.add(save);
		panel.add(delete);
		panel.add(recog);
		panel.add(label);
		panel.add(text);
		panel.add(countval);
		panel.add(incrLrn);
		
		incrLrn.addActionListener((ActionListener) this);
		save.addActionListener((ActionListener) this);
		delete.addActionListener((ActionListener) this);
		recog.addActionListener((ActionListener) this);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// Use the AwtPenToolkit to register a PenListener on the panel:
		AwtPenToolkit.addPenListener(panel, this);
		// setup the mouse to cause a pressure level event when the left button is pressed:
		PenManager pm=AwtPenToolkit.getPenManager();
		pm.pen.levelEmulator.setPressureTriggerForLeftCursorButton(0.5f);
	
		// show the window and setup the g2d
		frame.setVisible(true);
		g2d = (Graphics2D)panel.getGraphics();
		g2d2 = bi.createGraphics();

		// make the lines smooth
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		countSaves = 0;
	}//end constructor method
	/**
	 * 
	 */
	@Override
	public void penLevelEvent(PLevelEvent ev){
		//if this event is not a movement, ignore it
		if(!ev.isMovement()){
			
			return;
		}
		//set brush size, etc...
		float pressure = ev.pen.getLevelValue(PLevel.Type.PRESSURE);
		brushSize = pressure * 10;
		opacity = pressure * 255;
		/* since getLevelValue(PLevel.Type.PRESSURE) returns 0 -> 1,
		 we need to multiply that by 255 to
		 get the correct amount of color
		 (0 -> 255). When then subtract it
		 from 255 so we start with a black line
		 with full pressure */

		// get the current cursor location
		loc.x = ev.pen.getLevelValue(PLevel.Type.X);
		loc.y = ev.pen.getLevelValue(PLevel.Type.Y);
		
		if (brushSize>0){
			if (ev.pen.getKind() == PKind.valueOf(PKind.Type.ERASER))	// using the eraser, create a white line, effectively "erasing" the black line
			{
				// set the color to white, and create the stroke
				g2d.setColor(Color.white);
				g2d2.setColor(Color.black);
				stroke = new BasicStroke(brushSize * 2); // make it a bit more sensitive
			}else// default, we want to draw a black line onto the screen.
			{
				// create the stroke
				g2d.setColor(Color.black);	
				g2d2.setColor(Color.white);
				stroke=new BasicStroke(brushSize, BasicStroke.CAP_ROUND, // round line endings
							 BasicStroke.JOIN_MITER	);
			}
			// draw a line between the current and previous locations
			g2d.setStroke(stroke);
			g2d.draw(new Line2D.Float(prevLoc, loc));
			g2d2.setStroke(stroke);
			g2d2.draw(new Line2D.Float(prevLoc, loc));
		    
		}
		// set the current position to the previous position
		prevLoc.setLocation(loc);
		//NOTE: if performance is an issue, you can get better performance by painting on an 
		//off-screen image and requesting a paint of the dirty region when penTock() is called,
		//check the jpen.demo.PenCanvas source code for an example of this technique.
	}//end penLevelEvent overridden method
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Save")){
			countSaves += 1;
			countval.setText(" " + countSaves + " ");
			createImage(bi);
			bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			g2d2 = bi.createGraphics();
			g2d = null;
			g2d = (Graphics2D)panel.getGraphics();
			panel.paint(g2d);
			convertImage();
		}
		if(e.getActionCommand().equalsIgnoreCase("Delete")){
			bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			g2d2 = bi.createGraphics();
			g2d = null;
			g2d = (Graphics2D)panel.getGraphics();
			panel.paint(g2d);
		}
		if(e.getActionCommand().equalsIgnoreCase("Recog")){
			//get the image
			createUnknownImage(bi);
			bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			g2d2 = bi.createGraphics();
			g2d = null;
			g2d = (Graphics2D)panel.getGraphics();
			panel.paint(g2d);
			convertUnknownImage();
			
			//recognize symbol, check for user feedback option
			if(incrLrn.isSelected()){
				IncaDecision run = new IncaDecision(text.getText(), true);
			}else{
				IncaDecision run = new IncaDecision(text.getText(), false);
				run.getIncaResult();	
			}//end incremental learning check
		}
	}//end actionPerformed method
	/**
	 * 
	 * @param bi
	 */
	private void createImage(BufferedImage bi) { 	    
		File file = new File("dataset2\\image" + countSaves + ".png");
	    try {
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //end createImage method
	
	private void createUnknownImage(BufferedImage bi) { 	    
		File file = new File("unknownset\\image.png");
	    try {
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //end createImage method
	/**
	 * 
	 */
	//invert the black and white pixels.
	private void convertImage(){
		BufferedImage image = null;
		BufferedImage image2 = null;
		try {
			image = ImageIO.read(new File("dataset2\\image"+countSaves+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < image.getWidth(); i++){
			for(int j = 0; j < image.getHeight(); j++){
				Color c = new Color(image.getRGB(i,j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if(r==0 && g==0 && b==0){
					image2.setRGB(i,j,Color.white.getRGB());
				}else if(r==255 && g==255 && b==255){
					image2.setRGB(i, j, Color.black.getRGB());
				}
			}
		}//end convert color
		
		File file = new File("dataset2\\"+text.getText()+countSaves+".png");
	    
		try {
			ImageIO.write(image2, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end convertImage method
	
	//invert the black and white pixels.
	private void convertUnknownImage(){
		BufferedImage image = null;
		BufferedImage image2 = null;
		try {
			image = ImageIO.read(new File("unknownset\\image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < image.getWidth(); i++){
			for(int j = 0; j < image.getHeight(); j++){
				Color c = new Color(image.getRGB(i,j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if(r==0 && g==0 && b==0){
					image2.setRGB(i,j,Color.white.getRGB());
				}else if(r==255 && g==255 && b==255){
					image2.setRGB(i, j, Color.black.getRGB());
				}
			}
		}//end convert color
		
		File file = new File("unknownset\\"+text.getText()+".png");
	    
		try {
			ImageIO.write(image2, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteImageFile("image.png");
	}//end convertImage method
	
	private void deleteImageFile(String fileName){
		File file = new File("unknownset\\"+fileName);
		file.delete();
	}//end deleteImageFile method
	
	/**
	 * 
	 * @param args
	 */
	//main method --- only for testing
	public static void main(String[] args){
		TabletTraining newTab = new TabletTraining();
	}//end main method
}//end TabletTraining class
