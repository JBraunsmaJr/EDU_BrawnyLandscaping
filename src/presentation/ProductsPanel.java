/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import configuration.AppConfig;
import io.ImageFileFilter;
import models.Product;
import orm.Exceptions.ValidationException;
import persistence.ApplicationDbContext;
import util.FileHandler;
import util.Logging;

/**
 *
 * @author jonbr
 */
public class ProductsPanel extends javax.swing.JPanel implements IDbPanel<Product> {
    
    Product selectedProduct;
    boolean isDirty = false;
    ArrayList<Product> products;
    ProductTableModel productModel;
    
    /**
     * Creates new form ProductsPanel
     */
    public ProductsPanel() {
        productModel = new ProductTableModel();
        
        initComponents();
        productsTable.setModel(productModel);
        fileChooser.addChoosableFileFilter(new ImageFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        productModel.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent event)
            {
                setDirty(true);
            }
        });
        
        btnProductImage.setEnabled(false);
        
        productModel.addRange(getAllItems());
    }

    public void onPanelFocused()
    {
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        btnDeleteProduct = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnProductImage = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Description", "Unit Price", "Requires Dimensions"
            }
        ));
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(productsTable);

        btnDeleteProduct.setBackground(new java.awt.Color(255, 0, 51));
        btnDeleteProduct.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnDeleteProduct.setText("DELETE");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

        btnAddProduct.setBackground(new java.awt.Color(0, 255, 102));
        btnAddProduct.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnAddProduct.setForeground(new java.awt.Color(0, 0, 0));
        btnAddProduct.setText("ADD ROW");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnProductImage.setText("Image");
        btnProductImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductImageActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnProductImage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 237, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProductImage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        delete();
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        Product temp = new Product();
        productModel.add(temp);
        selectedProduct = temp;
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnProductImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductImageActionPerformed
        int result = fileChooser.showOpenDialog(this);
        
        if(result == JFileChooser.APPROVE_OPTION)
        {
            String path = fileChooser.getSelectedFile().getPath();
            this.captureImage(path);
            setDirty(true);
        }
    }//GEN-LAST:event_btnProductImageActionPerformed

    private void captureImage(String chosenFile)
    {
        String fileId = null;

        try
        {
            fileId = FileHandler.copyFileTo(chosenFile, AppConfig.getInstance().getContentDir());
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
        }

        if(fileId == null)
        {
            JOptionPane.showMessageDialog(null, "Invalid File ID", "IO Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateImage(fileId);
    }

    private void updateImage(String path)
    {
        if(path == null || path.isBlank())
        {
            btnProductImage.setIcon(null);
            return;
        }

        BufferedImage image = FileHandler.LoadImage(path);

        int width = btnProductImage.getWidth();
        int height = btnProductImage.getHeight();
        
        var scaledIcon = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
        btnProductImage.setIcon(new ImageIcon(scaledIcon));
        selectedProduct.setImagePath(path);
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void productsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMouseClicked
        int index = productsTable.getSelectedRow();
        
        onItemSelected(index);
    }//GEN-LAST:event_productsTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnProductImage;
    private javax.swing.JButton btnSave;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable productsTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setDirty(boolean value) {
        isDirty = value;
    }

    @Override
    public void onItemSelected(int index) {
        if(index < 0)
        {
            btnProductImage.setEnabled(false);
            return;
        }
        
        btnProductImage.setEnabled(true);
        
        selectedProduct = (Product) productModel.getProducts().toArray()[index];
        updateImage(selectedProduct.getImagePath());
        
    }

    @Override
    public void delete() {
        int selectedIndex = productsTable.getSelectedRow();
       
        if(selectedIndex < 0)
            return;
        
        Product product = (Product) productModel.getProducts().toArray()[selectedIndex];
        productModel.remove(selectedIndex);
        
        // Remove object from database
        ApplicationDbContext.getInstance().products.delete(product.getId());
               
    }

    @Override
    public void save() {
        
        int rowCount = 0;
        for(Product product : productModel.getProducts())
        {
            rowCount++;
            
            try
            {                
                if(product.getId() >= 1)
                {
                    Logging.config("Updating Record:\n" + product);
                    ApplicationDbContext.getInstance().products.update(product);
                }
                else
                {
                    Logging.config("Inserting Record: \n" + product);
                    // Capture that PK so we can update our local instance, also prevents
                    // duplicate inserts
                    int pk = ApplicationDbContext.getInstance().products.insert(product);
                    product.setId(pk);
                }
            }
            catch(ValidationException ex)
            {
                JOptionPane.showMessageDialog(null, String.format("Row %s", rowCount) + "\n" + ex.getErrors(), "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        
        resetItemData();        
    }

    @Override
    public ArrayList<Product> getAllItems() {
        return ApplicationDbContext.getInstance().products.get();
    }

    @Override
    public void resetItemData() {
        
    }
}
