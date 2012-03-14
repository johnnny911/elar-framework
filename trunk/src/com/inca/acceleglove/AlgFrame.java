/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inca.acceleglove;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.idrt.Glove;
import com.idrt.Handshape;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_ml.CvKNearest;
import com.inca.main.PerformanceMatrix;

/**
 *
 * @author mark_desktop
 */
public class AlgFrame extends javax.swing.JFrame {
	private GestureData data = null;
	private PerformanceMatrix svmPm, nnPm;
	private Handshape hand;
	private Glove glove;
	private int inc = 0;
	private SupportVM svm = new SupportVM();
	private static final int K = 5;
	private KNearest knn = new KNearest(K);
	
    /**
     * Creates new form NewJFrame
     */
    public AlgFrame() {
        initComponents();
    }
    
    public void setGestureObject(Handshape hand, Glove glove){
    	this.hand = hand;
    	this.glove = glove;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        getGesture = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        gestureTable = new javax.swing.JTable();
        runAll = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        runSvm = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        svmGuess = new javax.swing.JLabel();
        svmConfidence = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        nnGuess = new javax.swing.JLabel();
        nnConfidence = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        runNN = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        gestureSelectionList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        svmLastRun = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nnLastRun = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        recordButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        getGesture.setText("Get Gesture Reading");
        
        getGesture.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent evt){
        		getGestureActionPerformed(evt);
        	}
        });

        gestureTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "T X", "T Y", "T Z", "I X", "I Y", "I Z", "M X", "M Y", "M Z", "R X", "R Y", "R Z", "Pi X", "Pi Y", "Pi Z", "Pa X", "Pa Y", "Pa Z"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gestureTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(gestureTable);
        gestureTable.getColumnModel().getColumn(0).setResizable(false);
        gestureTable.getColumnModel().getColumn(1).setResizable(false);
        gestureTable.getColumnModel().getColumn(2).setResizable(false);
        gestureTable.getColumnModel().getColumn(3).setResizable(false);
        gestureTable.getColumnModel().getColumn(4).setResizable(false);
        gestureTable.getColumnModel().getColumn(5).setResizable(false);
        gestureTable.getColumnModel().getColumn(6).setResizable(false);
        gestureTable.getColumnModel().getColumn(7).setResizable(false);
        gestureTable.getColumnModel().getColumn(8).setResizable(false);
        gestureTable.getColumnModel().getColumn(9).setResizable(false);
        gestureTable.getColumnModel().getColumn(10).setResizable(false);
        gestureTable.getColumnModel().getColumn(11).setResizable(false);
        gestureTable.getColumnModel().getColumn(12).setResizable(false);
        gestureTable.getColumnModel().getColumn(13).setResizable(false);
        gestureTable.getColumnModel().getColumn(14).setResizable(false);
        gestureTable.getColumnModel().getColumn(15).setResizable(false);
        gestureTable.getColumnModel().getColumn(16).setResizable(false);
        gestureTable.getColumnModel().getColumn(17).setResizable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(getGesture)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getGesture)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        runAll.setText("Run All");
        runAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAllActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("SVM");

        runSvm.setText("Run");
        runSvm.setEnabled(true);
        runSvm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSvmActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        svmGuess.setText("?");

        svmConfidence.setText("0.0%");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(svmGuess, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(svmConfidence)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addComponent(svmGuess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(svmConfidence, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        nnGuess.setText("?");

        nnConfidence.setText("0.0%");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nnGuess, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nnConfidence)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator2)
                    .addComponent(nnConfidence, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nnGuess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("NN");

        runNN.setText("Run");
        runNN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runNNActionPerformed(evt);
            }
        });

        jLabel5.setText("Actual Gesture: ");

        gestureSelectionList.setModel(new javax.swing.AbstractListModel() {
            //String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            //String[] strings = { "ok", "peace", "one", "stop", "thumbsup" };
        	String[] strings = { "one", "two", "three", "four", "five", "six", "seven",
        						 "eight", "nine"};
        	public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(gestureSelectionList);

        jLabel1.setText("Last Run:");

        jLabel6.setText("n/a");

        recordButton.setText("Record");
        recordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(runAll, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(runSvm, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(runNN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(svmLastRun)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(recordButton))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(nnLastRun)
                .addGap(470, 470, 470))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runAll)
                    .addComponent(recordButton))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel2)
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(38, 38, 38))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(runSvm)
                                    .addComponent(jLabel3)
                                    .addComponent(svmLastRun))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(runNN)
                                        .addComponent(jLabel4))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nnLastRun)))
                        .addGap(112, 112, 112)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(199, 199, 199))
        );

        jPanel1.getAccessibleContext().setAccessibleName("AlgGuessPanel");

        pack();
    }// </editor-fold>

    private void runSvmActionPerformed(java.awt.event.ActionEvent evt) {                                       
       // if (data != null){
    	try{
    		hand = glove.captureHandshape();
    		GestureData hOut = new GestureData(hand.toString());
    		CvMat hArry = hOut.toCvMat1();
    		for(int i = 0; i < hOut.NUM_POINTS; i++){
    			gestureTable.getModel().setValueAt(hArry.get(i), 0, i);
    		}
    		
    		svm.train(getKnnData("gloveset"), getClassLabels());
    		int guess = svm.predict(hArry);
    		System.out.println(Gesture.get(guess));
    	}catch(Exception e){
    		e.printStackTrace();
    	}		
        //}
    }                                      

    private void runAllActionPerformed(java.awt.event.ActionEvent evt) {                                       
    	//if (data != null){
        	runSvmActionPerformed(evt);
        	runNNActionPerformed(evt);
       // }
    }                                      

    private void runNNActionPerformed(java.awt.event.ActionEvent evt) {                                      
    	//if (data != null){
    	try{
    		hand = glove.captureHandshape();
    		GestureData hOut = new GestureData(hand.toString());
    		CvMat hArry = hOut.toCvMat();
    		for(int i = 0; i < hOut.NUM_POINTS; i++){
    			gestureTable.getModel().setValueAt(hArry.get(i), 0, i);
    		}
    		
    		knn.train(getKnnData("gloveset"), getClassLabels());
    		int guess = knn.predict(hArry);
    		System.out.println(Gesture.get(guess));
    	}catch(Exception e){
    		e.printStackTrace();
    	}	
        //}	
    }             
    
    protected CvMat getKnnData(String filename) throws Exception{
    	CvMat data = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH, 
    							GestureData.NUM_POINTS, CV_32FC1);
    	
    	File inFile = new File(filename);
		Scanner in = new Scanner(inFile);
		int line;
		
    	for(int i = 0; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i++){
    		for(int j = 0; j < GestureData.NUM_POINTS; j++){
    			if(in.hasNext()){
    				line = in.nextInt();
    				data.put(i, j, line);
    			}
    		}
    	}
    	in.close();
    	/*
    	inFile = new File("glovesetmark");
		in = new Scanner(inFile);
		line=0;
    	for(int i = GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH*2; i++){
    		for(int j = 0; j < GestureData.NUM_POINTS; j++){
    			if(in.hasNext()){
    				line = in.nextInt();
    				data.put(i, j, line);
    			}
    		}
    	}
    	in.close();
    	*/
    	return data;
    }
    

    
    protected CvMat getClassLabels(){
    	CvMat labels = cvCreateMat(GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH, 1, CV_32FC1);
    	for(int i = 0; i < GestureData.NUM_GESTURES*GestureData.NUM_GESTURES_TRAIN_EACH; i++){
    		if(i>=0 && i<49)	labels.put(i, Gesture.ZERO.getKey());
    		if(i>=49 && i<99)	labels.put(i, Gesture.ONE.getKey());
    		if(i>=99 && i<149)	labels.put(i, Gesture.TWO.getKey());
    		if(i>=149 && i<199)	labels.put(i, Gesture.THREE.getKey());
    		if(i>=199 && i<249)	labels.put(i, Gesture.FOUR.getKey());
    		if(i>=249 && i<299)	labels.put(i, Gesture.FIVE.getKey());
    		if(i>=299 && i<349)	labels.put(i, Gesture.SIX.getKey());
    		if(i>=349 && i<399)	labels.put(i, Gesture.SEVEN.getKey());
    		if(i>=399 && i<449)	labels.put(i, Gesture.EIGHT.getKey());	
    		if(i>=449 && i<499)	labels.put(i, Gesture.NINE.getKey());
    	}
    	return labels;
    }

    private void recordButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	//if (data != null){
    	inc++;
    	try{
    		hand = glove.captureHandshape();
    		GestureData hOut = new GestureData(hand.toString());
    		CvMat hArry = hOut.toCvMat();
    		for(int i = 0; i < hOut.NUM_POINTS; i++){
    			gestureTable.getModel().setValueAt(hArry.get(i), 0, i);
    		}
    		writeOutput(hand.toString(), "gloveset");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	System.out.println(inc);
        //}
    }
    
    private void getGestureActionPerformed(ActionEvent evt){
    	try{
    		hand = glove.captureHandshape();
    		GestureData hOut = new GestureData(hand.toString());
    		CvMat hArry = hOut.toCvMat();
    		for(int i = 0; i < hOut.NUM_POINTS; i++){
    			gestureTable.getModel().setValueAt(hArry.get(i), 0, i);
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    protected void writeOutput(String data, String fileName)throws Exception{
    	FileWriter stream = new FileWriter(fileName, true);
    	BufferedWriter out = new BufferedWriter(stream);
    	out.write(data);
    	out.write(" \n");
    	out.close();
    	stream.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AlgFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify
    private javax.swing.JList gestureSelectionList;
    private javax.swing.JTable gestureTable;
    private javax.swing.JButton getGesture;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel nnConfidence;
    private javax.swing.JLabel nnGuess;
    private javax.swing.JLabel nnLastRun;
    private javax.swing.JButton recordButton;
    private javax.swing.JButton runAll;
    private javax.swing.JButton runNN;
    private javax.swing.JButton runSvm;
    private javax.swing.JLabel svmConfidence;
    private javax.swing.JLabel svmGuess;
    private javax.swing.JLabel svmLastRun;
    // End of variables declaration
}
