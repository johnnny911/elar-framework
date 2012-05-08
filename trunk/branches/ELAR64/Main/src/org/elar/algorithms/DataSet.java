package org.elar.algorithms;
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
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set1", 
				"D:\\Programming\\datasets2\\image_cv\\adilset", 0);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set1", 
		"D:\\Programming\\datasets2\\image_cv\\hansset", 50);
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set1", 
		"D:\\Programming\\datasets2\\image_cv\\bojanset", 101);
		//adilset
		set.createImageDataset("D:\\Programming\\datasets2\\image_cv\\set2", 
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
		"D:\\Programming\\datasets2\\image_cv\\hansset", 101);
	}
}