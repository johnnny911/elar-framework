package com.inca.acceleglove;
import com.inca.main.PerformanceMatrix;

/**
*
* @author mark_desktop
*/
public class AlgFrame extends javax.swing.JFrame {

   public AlgFrame() {
       initComponents();
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">
   private void initComponents() {

       svmPanel = new javax.swing.JPanel();
       jPanel1 = new javax.swing.JPanel();
       jSeparator1 = new javax.swing.JSeparator();
       svmGuess = new javax.swing.JLabel();
       svmConfidence = new javax.swing.JLabel();
       runSvm = new javax.swing.JButton();
       jLabel3 = new javax.swing.JLabel();
       svmCorrect = new javax.swing.JButton();
       svmIncorrect = new javax.swing.JButton();
       jPanel3 = new javax.swing.JPanel();
       getGesture = new javax.swing.JButton();
       jScrollPane1 = new javax.swing.JScrollPane();
       gestureTable = new javax.swing.JTable();
       jLabel5 = new javax.swing.JLabel();
       jScrollPane2 = new javax.swing.JScrollPane();
       gestureField = new javax.swing.JTextPane();
       runAll = new javax.swing.JButton();

       setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

       runSvm.setText("Run");
       runSvm.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               runSvmActionPerformed(evt);
           }
       });

       jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
       jLabel3.setText("SVM");

       svmCorrect.setText("Correct");
       svmCorrect.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               svmCorrectActionPerformed(evt);
           }
       });

       svmIncorrect.setText("Incorrect");
       svmIncorrect.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               svmIncorrectActionPerformed(evt);
           }
       });

       javax.swing.GroupLayout svmPanelLayout = new javax.swing.GroupLayout(svmPanel);
       svmPanel.setLayout(svmPanelLayout);
       svmPanelLayout.setHorizontalGroup(
           svmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(svmPanelLayout.createSequentialGroup()
               .addComponent(jLabel3)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(runSvm, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
               .addGroup(svmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                   .addComponent(svmIncorrect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(svmCorrect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addContainerGap())
       );
       svmPanelLayout.setVerticalGroup(
           svmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(svmPanelLayout.createSequentialGroup()
               .addGroup(svmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addGroup(svmPanelLayout.createSequentialGroup()
                       .addContainerGap()
                       .addGroup(svmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                           .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                           .addComponent(runSvm)
                           .addComponent(jLabel3)))
                   .addGroup(svmPanelLayout.createSequentialGroup()
                       .addComponent(svmIncorrect)
                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                       .addComponent(svmCorrect)))
               .addContainerGap())
       );

       jPanel1.getAccessibleContext().setAccessibleName("AlgGuessPanel");

       getGesture.setText("Get Gesture");

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

       jLabel5.setText("Actual Gesture: ");

       jScrollPane2.setViewportView(gestureField);

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
                       .addGap(235, 235, 235)
                       .addComponent(getGesture)
                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                       .addComponent(jLabel5)
                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                       .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
               .addContainerGap())
       );
       jPanel3Layout.setVerticalGroup(
           jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(jPanel3Layout.createSequentialGroup()
               .addContainerGap()
               .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                       .addComponent(getGesture)
                       .addComponent(jLabel5))
                   .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
       getContentPane().setLayout(layout);
       layout.setHorizontalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addGroup(layout.createSequentialGroup()
                       .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addGroup(layout.createSequentialGroup()
                               .addContainerGap()
                               .addComponent(svmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                           .addGroup(layout.createSequentialGroup()
                               .addGap(41, 41, 41)
                               .addComponent(runAll, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                       .addGap(0, 381, Short.MAX_VALUE))
                   .addGroup(layout.createSequentialGroup()
                       .addContainerGap()
                       .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
               .addContainerGap())
       );
       layout.setVerticalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
               .addGap(33, 33, 33)
               .addComponent(runAll)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(svmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGap(127, 127, 127)
               .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGap(171, 171, 171))
       );

       pack();
   }// </editor-fold>

   private void runSvmActionPerformed(java.awt.event.ActionEvent evt) {                                         
       // TODO add your handling code here:
   }                                        

   private void svmCorrectActionPerformed(java.awt.event.ActionEvent evt) {                                         
       // TODO add your handling code here:
   }                                        

   private void svmIncorrectActionPerformed(java.awt.event.ActionEvent evt) {                                         
       // TODO add your handling code here:
   }                                        

   private void runAllActionPerformed(java.awt.event.ActionEvent evt) {
       // TODO add your handling code here:
   }

   /**
    * @param args the command line arguments
    */
//   public static void main(String args[]) {
//       /*
//        * Set the Nimbus look and feel
//        */
//       //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//       /*
//        * If Nimbus (introduced in Java SE 6) is not available, stay with the
//        * default look and feel. For details see
//        * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//        */
//       try {
//           for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//               if ("Nimbus".equals(info.getName())) {
//                   javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                   break;
//               }
//           }
//       } catch (ClassNotFoundException ex) {
//           java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//       } catch (InstantiationException ex) {
//           java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//       } catch (IllegalAccessException ex) {
//           java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//       } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//           java.util.logging.Logger.getLogger(AlgFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//       }
//       //</editor-fold>
//
//       /*
//        * Create and display the form
//        */
//       java.awt.EventQueue.invokeLater(new Runnable() {
//
//           public void run() {
//               new AlgFrame().setVisible(true);
//           }
//       });
//   }
   // Variables declaration - do not modify
   private javax.swing.JTextPane gestureField;
   private javax.swing.JTable gestureTable;
   private javax.swing.JButton getGesture;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JSeparator jSeparator1;
   private javax.swing.JButton runAll;
   private javax.swing.JButton runSvm;
   private javax.swing.JLabel svmConfidence;
   private javax.swing.JButton svmCorrect;
   private javax.swing.JLabel svmGuess;
   private javax.swing.JButton svmIncorrect;
   private javax.swing.JPanel svmPanel;
   // End of variables declaration
}