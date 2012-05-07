/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elar.main;

import java.awt.Color;
import java.awt.Dimension;
import org.elar.decision.PerformanceMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author borotech
 */
public class BarChart {
    private PerformanceMatrix pm1,pm2,pm3, pm4;
    private static final int ALPH_SIZE = 10;
    private ChartPanel panel1, panel2, panel3, panel4;
    private static final String DB_PATH = "databases\\";
    /**
     * 
     * @param pmName1
     * @param pmName2
     * @param pmName3 
     */
    public BarChart(String pmName1, String pmName2, String pmName3, String pmName4){
        if(pmName1!=null){this.pm1 = new PerformanceMatrix(pmName1,ALPH_SIZE);}
        if(pmName2!=null){this.pm2 = new PerformanceMatrix(pmName2,ALPH_SIZE);}
        if(pmName3!=null){this.pm3 = new PerformanceMatrix(pmName3,ALPH_SIZE);}
        if(pmName4!=null){this.pm4 = new PerformanceMatrix(pmName4,ALPH_SIZE);}
        setPerformanceMatrices();
        createChart();
    }
    /**
     * 
     */
    private void setPerformanceMatrices(){
        if(pm1!=null){pm1.getDatabase(DB_PATH+pm1.getName()+".xml");}
        if(pm2!=null){pm2.getDatabase(DB_PATH+pm2.getName()+".xml");}
        if(pm3!=null){pm3.getDatabase(DB_PATH+pm3.getName()+".xml");}
        if(pm4!=null){pm4.getDatabase(DB_PATH+pm4.getName()+".xml");}
    }
    /**
     * 
     */
    private void createChart(){
        
        if(pm1!=null){
            DefaultCategoryDataset dataset1 = setDataValues(pm1);
            panel1 = getChart(dataset1, pm1);
        }
        if(pm2!=null){
            DefaultCategoryDataset dataset2 = setDataValues(pm2);
            panel2 = getChart(dataset2, pm2);
        }
        if(pm3!=null){
            DefaultCategoryDataset dataset3 = setDataValues(pm3);
            panel3 = getChart(dataset3, pm3);
        }
        if(pm4!=null){
            DefaultCategoryDataset dataset4 = setDataValues(pm4);
            panel4 = getChart(dataset4, pm4);
        }
    }//end createChart method
    /**
     * 
     * @param pm
     * @return 
     */
    private DefaultCategoryDataset setDataValues(PerformanceMatrix pm){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(((double)pm.getRecNum(0, 0)/50)*100, "Accuary", "0");
        dataset.setValue(((double)pm.getRecNum(1, 1)/50)*100, "Accuary", "1");
        dataset.setValue(((double)pm.getRecNum(2, 2)/50)*100, "Accuary", "2");
        dataset.setValue(((double)pm.getRecNum(3, 3)/50)*100, "Accuary", "3");
        dataset.setValue(((double)pm.getRecNum(4, 4)/50)*100, "Accuary", "4");
        dataset.setValue(((double)pm.getRecNum(5, 5)/50)*100, "Accuary", "5");
        dataset.setValue(((double)pm.getRecNum(6, 6)/50)*100, "Accuary", "6");
        dataset.setValue(((double)pm.getRecNum(7, 7)/50)*100, "Accuary", "7");
        dataset.setValue(((double)pm.getRecNum(8, 8)/50)*100, "Accuary", "8");
        dataset.setValue(((double)pm.getRecNum(9, 9)/50)*100, "Accuary", "9");
        return dataset;
    }//end setDataValues method
    /**
     * 
     * @param dataset
     * @return 
     */
    private ChartPanel getChart(DefaultCategoryDataset dataset, PerformanceMatrix pm){
        
        JFreeChart chart = ChartFactory.createBarChart
        (pm.getName(),"Digits", "Accuracy", dataset, 
        PlotOrientation.VERTICAL, false,true, false);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(new Dimension(500,270));
        return panel;
    }
    /**
     * 
     * @return 
     */
    public ChartPanel getPanel1(){
        return panel1;
    }
    /**
     * 
     * @return 
     */
    public ChartPanel getPanel2(){
        return panel2;
    }
    /**
     * 
     * @return 
     */
    public ChartPanel getPanel3(){
        return panel3;
    }
    /**
     * 
     * @return 
     */
    public ChartPanel getPanel4(){
        return panel4;
    }
}