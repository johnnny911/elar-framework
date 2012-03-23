package com.inca.algorithms;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.inca.main.IncaDecision;

import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 * 
 * @author James Neilan
 * @version	1.0.0
 * Inca P.O.C.
 *
  * ContourFeaturesRecg.java - 
 * for a symbol over the sample space. Adaptation of a Contour Feature 
 * algorithm, from "Algorithms for Image Processing and Computer Vision", 
 * J.R.Parker, 2010
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
public class ContourFeaturesRecg extends Algorithm {
	private int database[][];
	private int dbv[];
	private int dbSize;
	private int symbol;
	private String datafile;
	private int x, y;
	private boolean createDb = false;
	//private static final String PATH = "unknownset\\";
	//private static final String PATH = "dataset2\\";
	private static final String PATH = "D:\\Programming\\datasets2\\image_cv\\dataset1\\";
	/**
	 * 
	 */
	public ContourFeaturesRecg(String datafile, int x, int y){
		this.datafile = datafile;
		this.x = x;
		this.y = y;
	}//end CountourFeaturesRecg method
	
	/**
	 * 
	 */
	public ContourFeaturesRecg(){
		
	}//end CountourFeaturesRecg method
	
	/**
	 * 
	 * @param sourceImage
	 * @param i
	 * @param j
	 * @return
	 */
	private double getPixelVal(IplImage sourceImage, int i, int j){
		return cvGet2D(sourceImage,i,j).getVal(0);
	}//end getPixel method
	/**
	 * 
	 * @param data
	 * @param n
	 */
	private void rescale (double []data, int n){
		double newd[] = new double[50];
		double w1, w2, dj, xinc, xi;
		double w[] = new double[20];
		double kp;
		int nx, ix, k1,k2;
	
		if (n < 50) 
		{
		  dj = (double)n/50.0f;
		  for (int i=0; i<49; i++) 
		  {
		   kp = (float)(n*i)/50.0f;
		   k1 = (int)kp;
		   k2 = k1 + 1;
		   newd[i] = (data[k2]-data[k1])*(kp-k1) + data[k1];
		  }
		  newd[49] = data[n-1];
	
		} else 
		{
		  xinc = (double)n/50.0f;
		  if (xinc-(int)xinc == 0) nx = (int)(xinc + 1.0);
		    else nx = (int)(xinc+2.0);
		  for (int i=0; i<50; i++) 
		  {
			xi = (double)i*xinc;
			ix = (int)xi;
			w[0] = (int)(xi+1.0) - xi;	/* First weight */
			w[nx-1] = 1.0f-w[0];
			for (int j=1; j<nx-1; j++) w[j] = 1.0;
			w1 = 0.0; w2 = 0.0;
			for (int j=0; j<nx; j++) 
			{
			   w1 = w1 + w[j]*data[i+j];
			   w2 += w[j];
			}
			newd[i] = w1/w2;
		  }
		}
	
		for (int i=0; i<50; i++)
		   data[i] = newd[i];
	}//end rescale method
	/**
	 * 
	 * @param ldata
	 * @param rdata
	 * @param n
	 */
	private void outprof (double []ldata, double []rdata, int n)
	{
		int i,j;
	/*
		for (i=1; i<=n; i++) {
		   //System.out.println (String.format("%2d : ", i));
		   for (j=0; j<ldata[i]; j++){} //System.out.println (".");
		   for (j=(int)ldata[i]; j<=(int)rdata[i]; j++){} //System.out.println ("#");
		   System.out.println (String.format("\t\t\t%f %f\n", ldata[i], rdata[i]));
		}*/
	}//end outprof method
	/**
	 * 
	 * @param ldata
	 * @param n
	 */
	public void outdata (double []ldata, int n)
	{
		int i;
	/*
		for (i=1; i<=n; i++) {
		   System.out.println(String.format("%2d : %f\n", i, ldata[i]));
		}
		*/
	}//end outdata method
	/**
	 * 
	 * @param data
	 * @param lmin
	 * @param lmax
	 * @return
	 */
	private double lrpeak (double []data, int lmin, int lmax){
		int imax, imin;
	
		imin = 100; imax = -imin;
		for (int i=lmin; i<=lmax; i++) {
		   if (data[i] < imin) imin = (int)data[i];
		   if (data[i] > imax) imax = (int)data[i];
		}
		return (double)(imax - imin + 1);
	}//end lrpeak method
	/**
	 * 
	 * @param data
	 * @param lmin
	 * @param lmax
	 * @return
	 */
	private int lrmax (double []data, int lmin, int lmax){
		double x;
		int j, i;
	
		x = data[lmin]; j = lmin;
		for (i=lmin; i<=lmax; i++) {
		   if (data[i] > x) {
		      j = i;
		      x = data[i];
		   }
		}
		return j;
	}//end lrmax method
	/**
	 * 
	 * @param data
	 * @param lmin
	 * @param lmax
	 * @return
	 */
	private int lrmin (double []data, int lmin, int lmax){
		int i,j;
		double x;
	
		x = data[lmin]; j = lmin;
		for (i=lmin; i<lmax; i++) {
		   if (data[i] < x) {
		      j = i;
		      x = data[i];
		   }
		}
		return j;
	}//end lrmin method
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int bdiff (int []a, int []b)
	{
		int i,j=0;
	
		for (i=0; i<48; i++)
		  if (a[i] != b[i]) j++;
		return j;
	}//end bdiff method
	/**
	 * 
	 * @param bits
	 * @return
	 */
	private int searchDB(int []bits){
		int i,j,k,vmin,imin;
		boolean clear = false;
		int hist[] = new int[10];

		vmin = 50; imin = -1;
		//for(int lp = 0; lp < hist.length; lp++){
			//hist[lp] = 0;
		//}
		hist[0] = hist[1] = hist[2] = hist[3] = hist[4] = 0;
		hist[5] = hist[6] = hist[7] = hist[8] = hist[9] = 0;
		for (i=0; i<dbSize; i++)
		{
		  j = bdiff (bits, database[i]);
		  if (j < vmin)
		  {
		    vmin = j;
		    imin = i;
		    hist[0] = hist[1] = hist[2] = hist[3] = hist[4] = 0;
		    hist[5] = hist[6] = hist[7] = hist[8] = hist[9] = 0;
		    hist[dbv[imin]] += 1;
		    clear = true;
		  } else if (j == vmin)
		  {
			hist[dbv[imin]] += 1;
			clear = false;
		  }
		}
		if (clear) 
		 return dbv[imin];

		//System.out.println ("Tie ... ");
		j = 0;
		for (i = 0; i<10; i++)
		  if (hist[i]>hist[j]) j = i;
		k = 0;
		for (i=0; i<10; i++)
		{
		  if (hist[i] == hist[j]) k++;
		  //System.out.println (String.format("%d ", hist[i]));
		}
		//System.out.println ("\n");
		if (k>1) return -1;
		return j;
	}//end searchDB method
	/**
	 * 
	 * @param sourceImage
	 * @return
	 */
	private int recognize(IplImage sourceImage){//int itr
		int j,nr, wi;
		int lmax,lmin,rmax,rmin;
		int a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17;
		int b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,c1,c2,c3,d1,d2;
		int e1,e2,e3,e4,e5,e6,f1,f2,f3,g1,g2,g3,g4,h,ii = 0;
		double ratio, wmax;
				
		double w1, w2, w3,k;
		int bits[] = new int[50];
			
		int num = -1;
		wmax = -1;
		if (sourceImage.height() < 52) nr = 52; else nr = sourceImage.height();
		double lprof[] = new double[2*(nr+1)];
		double rprof[] = new double[2*(nr+1)];
		double dl[]    = new double[2*(nr+1)];
		double dr[]    = new double[2*(nr+1)];
		double width[] = new double[2*(nr+1)];
			
		nr = sourceImage.height();
		for (int i = 0; i < sourceImage.height(); i++) 
		{
			/* Compute the left and right profiles */
			j = 0;
			while (j < sourceImage.width() && getPixelVal(sourceImage, i,j)>0) j++;
			lprof[i] = (double)j;
			  
			j = sourceImage.width()-1;
		    while (j > 0 && getPixelVal(sourceImage,i,j) > 0) j--;
		    rprof[i] = (double)j;
		   
		    //System.out.println ("  " + i + " : " + "L " + lprof[i]+" R " + rprof[i]);
			
			/* May as well compute width, too */
			
		    width[i] = rprof[i]-lprof[i];
			
		    if (width[i] > wmax) { wmax = width[i]; wi = i; }
			
			/* First differences of the profiles */
			
		    if (i==0)
				      dl[i] = 0;
		    else
				      dl[i] = lprof[i]-lprof[i-1];
				   
		    if (i==0)	
		    	dr[i] = 0;		
		    else
		    	dr[i] = rprof[i]-rprof[i-1];	
		}
			
			
		/* Scale: features are based on a standard size of 50 pixels */
		rescale (lprof, nr);
		rescale (rprof, nr);
		rescale (dl, nr);
		rescale (dr, nr);
		
		for (int i = 50; i > 0; i--) 
		{
			lprof[i] = lprof[i-1];	
			rprof[i] = rprof[i-1];
			dl[i] = dl[i-1];
			dr[i] = dr[i-1];
			width[i] = width[i-1];
		}
			
		//System.out.println("Scaled profiles: \n");	
		outprof (lprof, rprof, 50);
		//System.out.println ("Scaled profiles: dLeft\n");	
		outdata (dl, 50);
		//System.out.println("Scaled profiles: dRight\n");			
		outdata (dr, 50);					
		/* Find the width everywhere */		
		j = 1;		
		for (int i = 1; i <= 50; i++) 		
		{		
			width[i] = rprof[i]-lprof[i];			
			if (width[i] > width[j]) j = i;			
		}
		
		wmax = width[j]; wi = j;		
		ratio = (float)(nr)/(float)wmax;					
		
		/* The numeral 1 is often easy */
		
		if (ratio > 2.5) 		
		{		
			num = 1;			
			//System.out.println("Probably a '1'\n");			
		}		
		
		/*	At this point we have:
			LPROF - The left profile (distance from left hand margin, in pixels)	
				scaled to be from 0..49 (50 rows).
			RPROF - The right profile (distance from left hand margin, in pixels)
				scaled to be from 0..49 (50 rows).
			DL    - First difference in the left profile. DL[k]=LPROF[k]-LPROF[k-1].
			DR    - First difference in the right profile. DR[k]=RPROF[k]-RPROF[k-1].
		    The A and B classes of features are based on the value
			   of the peaks in the first difference arrays.		  */			
		
		if ((k=lrpeak(dl,  2, 50)) < 10) a1  = 1; else a1 = 0; bits[0] = a1;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl,  2, 10)) <  5) a2  = 1; else a2 = 0; bits[1] = a2;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl,  2, 15)) >  5) a3  = 1; else a3 = 0; bits[2] = a3;		
		//System.out.println ( k);
		if ((k=lrpeak(dl,  2, 15)) > 10) a4  = 1; else a4 = 0; bits[3] = a4;
		//System.out.println ( k);
		if ((k=lrpeak(dl,  2, 20)) > 10) a5  = 1; else a5 = 0; bits[4] = a5;		
		//System.out.println( k);		
		if ((k=lrpeak(dl,  2, 25)) >  5) a6  = 1; else a6 = 0; bits[5] = a6;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl,  5, 15)) >  5) a7  = 1; else a7 = 0; bits[6] = a7;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl,  5, 35)) >  5) a8  = 1; else a8 = 0; bits[7] = a8;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl,  5, 40)) > 10) a9  = 1; else a9 = 0; bits[8] = a9;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 10, 30)) > 10) a10 = 1; else a10 = 0; bits[9] = a10;				
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 15, 40)) > 10) a11 = 1; else a11 = 0; bits[10] = a11;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 25, 50)) <  5) a12 = 1; else a12 = 0; bits[11] = a12;	
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 30, 50)) > 10) a13 = 1; else a13 = 0; bits[12] = a13;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 30, 50)) <  5) a14 = 1; else a14 = 0; bits[13] = a14;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 35, 50)) <  5) a15 = 1; else a15 = 0; bits[14] = a15;		
		//System.out.println ( k);		
		if ((k=lrpeak(dl, 35, 50)) > 10) a16 = 1; else a16 = 0; bits[15] = a16;		
		//System.out.println( k);		
		if ((k=lrpeak(dl, 40, 50)) >  5) a17 = 1; else a17 = 0; bits[16] = a17;		
		//System.out.println ( k);						
		if ((k=lrpeak(dr,  2, 50)) > 10) b1  = 1; else b1  = 0; bits[17] = b1;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr,  2, 15)) > 10) b2  = 1; else b2  = 0; bits[18] = b2;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr,  2, 30)) < 10) b3  = 1; else b3  = 0; bits[19] = b3;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr,  2, 45)) <  5) b4  = 1; else b4  = 0; bits[20] = b4;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 25, 45)) < 10) b5  = 1; else b5  = 0; bits[21] = b5;		
		
		//System.out.println ( k);
				
		if ((k=lrpeak(dr, 25, 50)) > 10) b6  = 1; else b6  = 0; bits[22] = b6;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 25, 50)) <  5) b7  = 1; else b7  = 0; bits[23] = b7;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 30, 50)) > 10) b8  = 1; else b8  = 0; bits[24] = b8;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 35, 50)) >  5) b9  = 1; else b9  = 0; bits[25] = b9;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 35, 50)) > 10) b10 = 1; else b10 = 0; bits[26] = b10;		
		//System.out.println ( k);		
		if ((k=lrpeak(dr, 40, 50)) >  5) b11 = 1; else b11 = 0; bits[27] = b11;		
		//System.out.println ( k);
			
			/* The C features require that minima be found on each side
			   of the minimum of a profile in a given range.            */		
		lmin = lrmin (lprof,  1, 50);		
		lmax = lrmax (lprof,  1, 50);		
		rmin = lrmin (rprof,  1, 50);		
		rmax = lrmax (rprof,  1, 50);		
				
		if ( (lrmin(rprof, 1, 30) < lrmax(rprof, rmin, 30)) &&
				(lrmin(rprof, 1, 30) > lrmax(rprof, 1, rmin) ) )
					c1 = 1; else c1 = 0;
		if ( (lrmin(rprof, 10, 40) < lrmax(rprof, rmin, 40)) &&
				     (lrmin(rprof, 10, 40) > lrmax(rprof,  1, rmin)) )
					c2 = 1; else c2 = 0;
		if ( (lrmin(rprof, 10, 45) < lrmax(rprof, rmin, 45)) &&
				     (lrmin(rprof, 10, 45) > lrmax(rprof,  1, rmin)) )
					c3 = 1; else c3 = 0;
		bits[28] = c1; bits[29] = c2; bits[30] = c3;
			
		/* The D features use right profile properties */
		if (rprof[lrmin(rprof, 5,25)] == rprof[lrmax(rprof,1,rmin)]) 
				d1 = 1; else d1 = 0;
		if (rprof[lrmin(rprof, 5,25)] == rprof[lrmax(rprof,rmin,40)])
				d2 = 1; else d2 = 0;
		bits[31] = d1; bits[32] = d2;
	
		/* The E features use simple left profile properties  */
		if (lrmax(lprof, 1, 30) < lrmin(lprof, 1, lmax)) e1 = 1; else e1 = 0;
		if (lrmax(lprof, 10, 30) < lrmin(lprof, 10, lmax)) e2 = 1; else e2 = 0;
		if (lrmax(lprof, 10, 30) < lrmin(lprof, 10, lmax)) e3 = 1; else e3 = 0;
		if (lrmax(lprof, 15, 45) < lrmin(lprof, 15, 45)) e4 = 1; else e4 = 0;
		if (lrmax(lprof, 20, 50) < lrmin(lprof, 20, 50)) e5 = 1; else e5 = 0;
		if (lrmax(lprof, 40, 50) < lrmin(lprof, 40, 50)) e6 = 1; else e6 = 0;
		bits[33] = e1; bits[34] = e2; bits[35] = e3; 
		bits[36] = e4; bits[37] = e5; bits[38] = e6;
			
		/* The F features use simple right profile properties */
		if (lrmin(rprof, 1, 30) < lrmax(rprof, 1, rmin)) f1 = 1; else f1 = 0;
		if (lrmin(rprof, 20, 35) < lrmax(rprof, 20, 35)) f2 = 1; else f2 = 0;
		if (lrmin(rprof, 35, 50) < lrmax(rprof, 35, 50)) f3 = 1; else f3 = 0;
		bits[39] = f1; bits[40] = f2; bits[41] = f3;
			
		/* The G features use the width */
		if (width[20] >= width[40]) g1 = 1; else g1 = 0;
		if (width[25] >= width[10]) g2 = 1; else g2 = 0;
		if (width[25] >= width[40]) g3 = 1; else g3 = 0;
		if (width[25] >= width[45]) g4 = 1; else g4 = 0;
		bits[42] = g1; bits[43] = g2; bits[44] = g3; bits[45] = g4;
			
		/* The H property is the ratio between height and width */
		if (ratio > 2.5) h = 1; else h = 0;
		bits[46] = h;
			
		/* The I property is a complicated combination of width properties */
		j = lrmin(width, 11, 39);
		w1 = width[j];
		w2 = width[ lrmax(width, 2, j-1) ];
		w3 = width[ lrmax(width, j+1, 49)];
		if (w1<w2 && w1<w3) ii = 1; else ii = 0;
		bits[47] = ii;
					
		num = searchDB(bits);
		/*	
		System.out.println (String.format("%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d",a1,a2,a3,a4,a5,a6,a7,a8,a9,a10));
		System.out.println (String.format("%1d%1d%1d%1d%1d%1d%1d ", a11,a12,a13,a14,a15,a16,a17));
		System.out.println (String.format("%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d ", 
						b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11));
		System.out.println (String.format("%1d%1d%1d ", c1,c2,c3));
		System.out.println (String.format("%1d%1d ", d1,d2));
		System.out.println(String.format("%1d%1d%1d%1d%1d%1d ", e1, e2, e3, e4, e5, e6));
		System.out.println (String.format("%1d%1d%1d ", f1,f2,f3));
		System.out.println (String.format("%1d%1d%1d%1d %1d %1d\n",g1,g2,g3,g4, h, ii));
		System.out.println (String.format("Found a '%d'\n", num));
		*/	
		if(createDb){
			try {
				writeDatabase(bits);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//for(int g = 0; g < bits.length; g++){
		//	System.out.print(bits[g]);
		//}
		return num;	
	}//end recognize method
	/**
	 * 
	 * @param bits
	 * @throws Exception
	 */
	private void writeDatabase(int [] bits) throws Exception{//int itr
		FileWriter stream = new FileWriter("cfDatabasecv14.db", true);
		BufferedWriter out = new BufferedWriter(stream);
		//String iter = ""+itr+" ";
		//out.write(iter);
		for(int i = 0; i < bits.length; i++){
			out.write(new String(""+bits[i]));
		}
		out.write(new String("\n"));
		out.close();
		stream.close();
	}
	/**
	 * 
	 * @param file
	 * @throws Exception
	 */
	public String recognizeSymbol(String file) throws Exception{//int itr
		IplImage sourceImage = cvLoadImage(""+PATH+file);
		if(sourceImage == null){
			throw(new Exception("Source Image Null."));
		}//end image check 
		
		if(database != null){
			ImageProcessing out = new ImageProcessing();
			IplImage bxImage = IplImage.create(sourceImage.width(), 
										sourceImage.height(), IPL_DEPTH_8U, 1);
			bxImage = out.bBox(sourceImage);
			
			symbol = recognize(bxImage);//int itr
			
		}else{
			throw(new Exception("Database = null."));
		}//end database check 
		
		return ""+symbol;
	}//end recognizeSymbol method
	/**
	 * 
	 * @param dataFile
	 * @param sizeX
	 * @param sizeY
	 * @throws Exception
	 */
	public void popDB(String dataFile, int sizeX, int sizeY) 
	throws Exception{
		database = new int[sizeX][sizeY];
		dbv = new int [sizeX];
		int k;
		File inFile = new File(dataFile);
		Scanner in = new Scanner(inFile);
		String line = null;
		
		for (int i=0; i<sizeX; i++)
		{
			if(in.hasNext()){
				line = in.next();
				//System.out.println(line);
				dbv[i] = Integer.parseInt(line);
		
				line = in.next();
				//System.out.println(line);
				for (int j=0; j<48; j++)//48
				{
					k = Integer.parseInt(Character.toString(line.charAt(j)));
					if( k == 1){
						database[i][j] = 1;
					}else{
						database[i][j] = 0;
					}
				}
			}else{
				break;
			}
		
		}
		this.dbSize = sizeX;
		in.close();
	}//end populateDatabase method
	/**
	 * 
	 * @return
	 */
	public int getSymbol(){
		return this.symbol;
	}//end getSymbol method
	
	@Override
	public void populateDatabase() throws Exception {
		popDB("cfDatabasecv11.db", 1500, 50);
	}
	
	private void setCreateDB(boolean set){
		this.createDb = set;
	}
	
	/**
	 * Main method used for regression testing.
	 * @param args
	 */
	/*
	public static void main(String[] args){
		ContourFeaturesRecg test = new ContourFeaturesRecg();
		IncaDecision run = new IncaDecision();
		String chk = null;
		try {
			test.popDB("prof.db", 1500, 50);
			test.setCreateDB(true);
			//for(int j = 0; j < 10; j++){
			int j = 9;
				for(int i = 1; i < 151; i++){
					chk = ""+i;
					test.recognizeSymbol(run.getPrefixName(j)+i+".png", j);
				}
			//}
			
			//System.out.println("Symbol: "+ test.getSymbol());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(chk);
		}
	}//end main method*/
}//end ContourFeatruesRecg class