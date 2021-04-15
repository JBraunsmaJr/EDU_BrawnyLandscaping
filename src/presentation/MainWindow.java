/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import configuration.AppConfig;
import models.*;
import java.sql.*;
import java.util.ArrayList;

import orm.ConnectionConfig;
import orm.DbSet;
import persistence.*;
import util.Logging;

/**
 *
 * @author jonbr
 */
public class MainWindow extends javax.swing.JFrame 
{
    
    CustomerPanel customerPanel;
    ProductsPanel productsPanel;
    OrderPanel orderPanel;
    
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        initCustom();
    }
    
    private void initCustom()
    {
        /*
            Ensure system resources are disposed of when application closes
        */
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                try
                {
                    ApplicationDbContext.getInstance().dispose();
                    Logging.warning("Closing database connection");
                    System.exit(0);
                }
                catch(Exception ex)
                {
                }
            }
        });
        
        customerPanel = new CustomerPanel();
        productsPanel = new ProductsPanel();
        orderPanel = new OrderPanel();
        
        this.tabPanelWindow.addTab("Customers", null, customerPanel, "Add/Edit/Delete Customers"); 
        this.tabPanelWindow.addTab("Products", null, productsPanel, "Add/Edit/Delete Products");
        this.tabPanelWindow.addTab("Orders", null, orderPanel, "Add/Edit/Delete Orders");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPanelWindow = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tabPanelWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPanelWindowMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel1.setText("Brawny Landscaping");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanelWindow, javax.swing.GroupLayout.DEFAULT_SIZE, 1060, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tabPanelWindow, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabPanelWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPanelWindowMouseClicked
        switch(tabPanelWindow.getSelectedIndex())
        {
            case 0:
                customerPanel.onPanelFocused();
                break;
            case 1:
                productsPanel.onPanelFocused();
                break;
            case 2:
                orderPanel.onPanelFocused();
                break;                
        }
    }//GEN-LAST:event_tabPanelWindowMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try
        {
            ConnectionConfig config = new ConnectionConfig("root","devry123","brawnylandscapingdb",3306);
            new ApplicationDbContext(config);
            AppConfig.getInstance();
            Logging.config("Completed Database Initialization");
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() 
            {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane tabPanelWindow;
    // End of variables declaration//GEN-END:variables
}
