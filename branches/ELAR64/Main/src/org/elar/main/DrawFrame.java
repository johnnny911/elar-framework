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
package org.elar.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.List;
import jpen.*;
import jpen.event.PenListener;
import jpen.owner.multiAwt.AwtPenToolkit;
import jpen.event.PenAdapter;
import org.elar.decision.ConfidenceVector;
import org.elar.decision.IncaDecision;
import org.elar.decision.PerformanceMatrix;

/**
 *
 * @author borotech
 */
public class DrawFrame extends PenAdapter{
    //tablet variables
    private BufferedImage bi = null;
    private Graphics2D g2d , g2d2;
    private Point2D.Float prevLoc = null;// previous cursor location
    private Point2D.Float loc = null;///current cursor location 
    private float brushSize;
    private float opacity;
    private BasicStroke stroke;
    private int countSaves;
    private JInternalFrame frame = new JInternalFrame("INCA Draw Frame");
    private JPanel panel;
    private JTextField text = new JTextField();
    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;
    private String[] compAns = new String[3];
    private String ensAns = new String();
    private List<ConfidenceVector> cvSet = new ArrayList<ConfidenceVector>();
    
    public DrawFrame(){}
    
    public void setPanel(JPanel panel, JTextField text){
        this.panel = panel;
        this.text = text;
        prevLoc = new Point2D.Float();
        loc = new Point2D.Float();  
        
        //frame.setContentPane(panel);
        //frame.setSize(new Dimension(800, 600));
        
        //setup the panel and the panel
        panel.setSize(new Dimension(WIDTH, HEIGHT));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // Use the AwtPenToolkit to register a PenListener on the panel:
        AwtPenToolkit.addPenListener(panel, this);
        //AwtPenToolkit.addPenListener(panel, this);
        // setup the mouse to cause a pressure level event when the left button is pressed:
        PenManager pm=AwtPenToolkit.getPenManager();
        pm.pen.levelEmulator.setPressureTriggerForLeftCursorButton(0.5f);

        // show the window and setup the g2d
        //frame.setContentPane(panel);
        //frame.setVisible(true);
        g2d = (Graphics2D)panel.getGraphics();
        g2d2 = bi.createGraphics();
        
        // make the lines smooth
        /*
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
                        RenderingHints.VALUE_ANTIALIAS_ON);*/
        countSaves = 0;
    }
    
    public JInternalFrame getFrame(){
        frame.setVisible(true);
        return frame;
    }
    
    public JPanel getPanel(){
        panel.setVisible(true);
        return panel;
    }
    
    @Override
    public void penLevelEvent(PLevelEvent ev) {
        g2d = (Graphics2D)panel.getGraphics();
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
                if (ev.pen.getKind() == PKind.valueOf(PKind.Type.ERASER))	
                // using the eraser, create a white line, effectively 
                    //"erasing" the black line
                {
                        // set the color to white, and create the stroke
                        g2d.setColor(Color.white);
                        g2d2.setColor(Color.black);
                        stroke = new BasicStroke(brushSize * 2); 
                        // make it a bit more sensitive
                }else// default, we want to draw a black line onto the screen.
                {
                        // create the stroke
                        g2d.setColor(Color.black);	
                        g2d2.setColor(Color.white);
                        // round line endings
                        stroke=new BasicStroke(brushSize, BasicStroke.CAP_ROUND, 
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
        //check the jpen.demo.PenCanvas sou
    }
    
    public void clear(){
        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d2 = bi.createGraphics();
        g2d = null;
        g2d = (Graphics2D)panel.getGraphics();
        panel.paint(g2d);
    }
    
    public void recognize(String ensemble){
        //get the image
        createUnknownImage(bi);
        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d2 = bi.createGraphics();
        g2d = null;
        g2d = (Graphics2D)panel.getGraphics();
        panel.paint(g2d);
        convertUnknownImage();

        //recognize symbol, check for user feedback option 
       
        PerformanceMatrix pm1 = new PerformanceMatrix("CFcv11", 10);
        PerformanceMatrix pm3 = new PerformanceMatrix("ANNcv11", 10);
        IncaDecision run = new IncaDecision(pm1, null, pm3, null,text.getText(),
                                            ensemble);
        //run INCA model
        run.getIncaResult();
        //get the component answers
        compAns = run.getCompAns();
        //get ensemble answer
        ensAns = run.getEnsAns();
        //get cvs
        cvSet = run.getCVs();
    }
    
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
                image = ImageIO.read(new File("dataset\\image"+countSaves+".png"));
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

        File file = new File("dataset\\"+text.getText()+countSaves+".png");

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
        image2 = new BufferedImage(image.getWidth(), image.getHeight(), 
                                            BufferedImage.TYPE_INT_RGB);
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
    
    public String[] getCompAns(){
        return compAns;
    }
    
    public String getEnsAns(){
        return ensAns;
    }
    
    public List<ConfidenceVector> getCVs(){
        return cvSet;
    }
}
