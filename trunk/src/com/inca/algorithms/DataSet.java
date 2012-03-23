package com.inca.algorithms;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DataSet {
	
	public DataSet(){}
	
	public void createImageDataset(String set, String source, int incr){
		BufferedImage image = null;
		BNNConvert run = new BNNConvert(null);
		String chk = null;
		
		try {
			for(int i = 0; i < 10; i++){
				for(int j = 1; j < 51; j++){
					chk = (""+i+" "+j);
					image = ImageIO.read(new File(source+"\\"+run.getPrefixName(i)+j+".png"));
					File file = new File(set+"\\"+run.getPrefixName(i)+(incr+j)+".png");
					ImageIO.write(image, "png", file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(chk);
		}
	}
	
	public static void main(String[] args){
		DataSet set = new DataSet();
								  
		//dset1
		set.createImageDataset("E:\\Programming\\datasets2\\image_cv\\set1", 
				"E:\\Programming\\datasets2\\image_cv\\adilset", 0);
		set.createImageDataset("E:\\Programming\\datasets2\\image_cv\\set1", 
		"E:\\Programming\\datasets2\\image_cv\\hansset", 50);
		set.createImageDataset("E:\\Programming\\datasets2\\image_cv\\set1", 
		"E:\\Programming\\datasets2\\image_cv\\bojanset", 100);
		//adilset
		/*set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set2", 
				"D:\\Programming\\datasets2\\image_cv\\dataset1", 0);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set2", 
		"D:\\Programming\\datasets2\\image_cv\\hansset", 50);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set2", 
		"D:\\Programming\\datasets2\\image_cv\\bojanset", 101);
		//hansset
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set3", 
				"D:\\Programming\\datasets2\\image_cv\\dataset1", 0);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set3", 
		"D:\\Programming\\datasets2\\image_cv\\adilset", 50);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set3", 
		"D:\\Programming\\datasets2\\image_cv\\bojanset", 101);
		//bojanset
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set4", 
				"D:\\Programming\\datasets2\\image_cv\\dataset1", 0);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set4", 
		"D:\\Programming\\datasets2\\image_cv\\adilset", 50);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set4", 
		"D:\\Programming\\datasets2\\image_cv\\hansset", 101);*/
	}
}