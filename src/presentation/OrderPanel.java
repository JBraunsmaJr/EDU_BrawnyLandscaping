/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import models.*;
import orm.Exceptions.ValidationException;
import persistence.ApplicationDbContext;
import presentation.components.IProductConsumer;
import presentation.components.IProductSelector;
import presentation.components.StandardProduct;
import util.Logging;

/**
 *
 * @author jonbr
 */
public class OrderPanel extends javax.swing.JPanel implements IDbPanel<CustomerOrder>, IProductConsumer {

    private boolean isDirty = false;
    
    /**
     * Used to display a list of all customer orders
     */
    DefaultListModel<CustomerOrder> orderModel;
    
    /**
     * Used to display a list of all items on order for selected customer order
     */
    DefaultListModel<OrderItem> orderItemModel;
    
    private Customer selectedCustomer;
    private Address selectedAddress;
    private CustomerOrder selectedCustomerOrder;
    private OrderItem selectedOrderItem;
    
    ArrayList<Customer> customerCache;
    ArrayList<Product> productCache;
    ArrayList<IProductSelector> productWidgets;
    ArrayList<CustomerOrder> customerOrderCache;
    
    /**
     * Creates new form OrderPanel
     */
    public OrderPanel() {
        orderModel = new DefaultListModel<>();
        orderItemModel = new DefaultListModel<>();
        
        initComponents();
        initializeCustomerModel();
        
        customerOrderCache = new ArrayList<>();
    }
    
    private void initCustom()
    {
        initializeCustomerModel();                                     // need to grab any updates that may have occurred from the customer panel
        initializeProducts();                                          // need to grab any updates that may have occurred from the product panel
        initializeOrders();
    }       
    
    private void initializeOrders()
    {
        
    }
    
    /**
     * Populates the customer model with a list of customers from the database
     */
    private void initializeCustomerModel()
    {
        customerCache = ApplicationDbContext.getInstance().customers.get("addresses");
        
        jcCustomerOption.removeAllItems();
        
        for(Customer customer : customerCache)
            jcCustomerOption.addItem(customer);
    }
    
    private void initializeProducts()
    {
        productCache = ApplicationDbContext.getInstance().products.get();
              
        // This is required. Without the layout -- you will not be able to see the components that get added
        productArea.setLayout(new GridLayout(productCache.size(), 1, 10, 10));
        
        if(productWidgets != null) // we should remove ALL product widgets on screen just to refresh things from other panels
        {
            productArea.removeAll();            
            productWidgets.clear();
        }
        else
            productWidgets = new ArrayList<>();
        
        for(Product product : productCache)
        {
            IProductSelector widget = null;
            
            /**
             * Cannot directly add the interface to the scroll pane
             * Must cast it to the designated JForm class within our components package
             */
            
            widget = new StandardProduct(product, this);   
            Component comp = (StandardProduct)widget;                                
            productArea.add(comp);
            
            productWidgets.add(widget);
        }
        
        productArea.revalidate();
        productArea.repaint();
        this.revalidate();
    }
    
    /**
     * Sets selectedCustomer to specified index from model
     * Updates address model to display customer's associated addresses
     * @param index 
     */
    private void selectCustomer(int index)
    {
        if(index < 0 || index > jcCustomerOption.getModel().getSize())
        {
            index = 0;
            Logging.warning("OrderPanel.selectCustomer -- index out of bounds. Unable to select item: " + index + " - Setting to first index");
        }
        
        selectedCustomer = (Customer) jcCustomerOption.getItemAt(index);
        
        System.out.println("Index Customer: " + index);
        
        if(selectedCustomer == null)
            return;
        
        jcCustomerAddressOption.removeAllItems();       
                
        for(Address address : selectedCustomer.getAddresses())
            jcCustomerAddressOption.addItem(address);          
        
        if(selectedCustomer.getAddresses().size() > 0)
            selectAddress(0);
    }
    
    private void selectAddress(int index)
    {
        if(index < 0 || index > jcCustomerAddressOption.getModel().getSize())
        {
            Logging.warning("OrderPanel.selectAddress -- index out of bounds. Unable to select item: " + index + " - Setting to first index");
            index = 0;
        }
        
        selectedAddress = (Address) jcCustomerAddressOption.getItemAt(index);
        
        if(selectedAddress == null)
        {
            Logging.warning("OrderPanel.selectAddress -- unable to select address. -- Null");
            return;
        }
        
        // now we have to update the order list to be based on the selected address
        orderModel.clear();
        
        // Retrieve ALL orders for the selected address
        // Furthermore, include all the ITEMS associated for each order (backreference to OrderItem table)
        customerOrderCache = ApplicationDbContext.getInstance().orders.get((order)->order.getAddressId() == selectedAddress.getId(), "items");
        
        // Update the list containing all the orders
        orderModel.clear();
        orderModel.addAll(customerOrderCache);
        
        if(customerOrderCache.size() > 0)
            selectOrder(0);
    }
    
    private void selectOrder(int index)
    {
        selectedCustomerOrder = orderModel.get(index);
        
        // Now we need to populate the order item model
        orderItemModel.clear();
        orderItemModel.addAll(selectedCustomerOrder.getOrderItems());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcCustomerOption = new javax.swing.JComboBox<>();
        jcCustomerAddressOption = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlOrderItems = new javax.swing.JList<>();
        btnRemoveOrderItem = new javax.swing.JButton();
        btnSubmitOrder = new javax.swing.JButton();
        btnViewCustomerOrders = new javax.swing.JButton();
        productAreaScrollPane = new javax.swing.JScrollPane();
        productArea = new javax.swing.JPanel();

        jcCustomerOption.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jcCustomerOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcCustomerOptionActionPerformed(evt);
            }
        });

        jcCustomerAddressOption.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jcCustomerAddressOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcCustomerAddressOptionActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setText("Customer:");

        jlOrderItems.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jlOrderItems.setModel(orderItemModel);
        jScrollPane2.setViewportView(jlOrderItems);

        btnRemoveOrderItem.setBackground(new java.awt.Color(255, 51, 51));
        btnRemoveOrderItem.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnRemoveOrderItem.setText("Remove");
        btnRemoveOrderItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveOrderItemActionPerformed(evt);
            }
        });

        btnSubmitOrder.setBackground(new java.awt.Color(102, 255, 51));
        btnSubmitOrder.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnSubmitOrder.setText("Submit");
        btnSubmitOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitOrderActionPerformed(evt);
            }
        });

        btnViewCustomerOrders.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnViewCustomerOrders.setText("View Customer Orders");
        btnViewCustomerOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewCustomerOrdersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productAreaLayout = new javax.swing.GroupLayout(productArea);
        productArea.setLayout(productAreaLayout);
        productAreaLayout.setHorizontalGroup(
            productAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 793, Short.MAX_VALUE)
        );
        productAreaLayout.setVerticalGroup(
            productAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
        );

        productAreaScrollPane.setViewportView(productArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcCustomerOption, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcCustomerAddressOption, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productAreaScrollPane))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnRemoveOrderItem, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewCustomerOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSubmitOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcCustomerOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcCustomerAddressOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productAreaScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveOrderItem)
                    .addComponent(btnSubmitOrder)
                    .addComponent(btnViewCustomerOrders))
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveOrderItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOrderItemActionPerformed
        delete();
    }//GEN-LAST:event_btnRemoveOrderItemActionPerformed

    private void btnSubmitOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitOrderActionPerformed
        save();
    }//GEN-LAST:event_btnSubmitOrderActionPerformed

    private void btnViewCustomerOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewCustomerOrdersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewCustomerOrdersActionPerformed

    private void jcCustomerOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcCustomerOptionActionPerformed
        selectCustomer(jcCustomerOption.getSelectedIndex());
    }//GEN-LAST:event_jcCustomerOptionActionPerformed

    private void jcCustomerAddressOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcCustomerAddressOptionActionPerformed
        selectAddress(jcCustomerAddressOption.getSelectedIndex());
    }//GEN-LAST:event_jcCustomerAddressOptionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRemoveOrderItem;
    private javax.swing.JButton btnSubmitOrder;
    private javax.swing.JButton btnViewCustomerOrders;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<Address> jcCustomerAddressOption;
    private javax.swing.JComboBox<Customer> jcCustomerOption;
    private javax.swing.JList<OrderItem> jlOrderItems;
    private javax.swing.JPanel productArea;
    private javax.swing.JScrollPane productAreaScrollPane;
    // End of variables declaration//GEN-END:variables
    
    public void onPanelFocused()
    {
        initCustom();        
    }
    
    @Override
    public void setDirty(boolean value) {
        isDirty = value;   
    }

    @Override
    public void onItemSelected(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        int index = jlOrderItems.getSelectedIndex();
        
        var orderItem = selectedCustomerOrder.getOrderItems().get(index);
        
        // Delete the order item from the database
        ApplicationDbContext.getInstance().orders.delete(orderItem.getId());
        
        // Delete the item from the array list
        selectedCustomerOrder.getOrderItems().remove(index);
    }

    @Override
    public void save() {
        if(selectedCustomerOrder == null)
        {
            Logging.warning("Unable to save customer order. -- Customer order has not been set");
            return;
        }
        
        System.out.println("Customer Order Id: " + selectedCustomerOrder.getId());
        
        selectedCustomerOrder.setAddressId(selectedAddress.getId());        
        
        try
        {
            // Save / update record based on id
            if(selectedCustomerOrder.getId() <= 0)
            {
                // Cannot forget to save this PK for later use from cached orders
                int pk = ApplicationDbContext.getInstance().orders.insert(selectedCustomerOrder);
                selectedCustomerOrder.setId(pk);
            }
            else
                ApplicationDbContext.getInstance().orders.update(selectedCustomerOrder);
            
            // Must update all the associated order items
            for(OrderItem item : selectedCustomerOrder.getOrderItems())
            {
                if(item.getId() <= 0)
                {
                    int pk = ApplicationDbContext.getInstance().orderItems.insert(item);
                    item.setId(pk);
                }
                else
                    ApplicationDbContext.getInstance().orderItems.update(item);
            }
        }
        catch(ValidationException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getErrors(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ArrayList<CustomerOrder> getAllItems() {
        return null;
    }

    @Override
    public void resetItemData() 
    {
        
    }

    @Override
    public void addToOrder(ProductSelection product) 
    {
        setDirty(true);
        
        if(selectedAddress == null)
        {
            Logging.warning("Could not create an order. -- Address was not set");
            return;
        }
        
        if(selectedCustomerOrder == null)
        {
            selectedCustomerOrder = new CustomerOrder();
            LocalDateTime now = LocalDateTime.now();
            selectedCustomerOrder.setOrderedOnDate(new Date(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
            selectedCustomerOrder.setAddressId(selectedAddress.getId());
        }
        
        
        
        OrderItem item = new OrderItem();
        item.setOrderId(selectedCustomerOrder.getId());
        item.setProductId(product.getProduct().getId());
        
        if(product.getProduct().getRequiresDimensions())
            item.setUnits(product.calculateDimensions());
        else
            item.setUnits(product.getUnits());
        selectedCustomerOrder.addOrderItem(item);
        orderItemModel.addElement(item);
    }
}
