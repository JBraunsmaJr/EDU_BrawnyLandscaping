/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;
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
    
    private void initializePanel()
    {
        initializeCustomerModel();                                     // need to grab any updates that may have occurred from the customer panel
        initializeProducts();                                          // need to grab any updates that may have occurred from the product panel
        initializeOrders();
    }       
    
    private void initializeOrders()
    {
        this.btnFulfilled.setEnabled(false);
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
    
    void clearOrderItemPanel()
    {
        orderItemModel.clear();
        selectedCustomerOrder = null;
        selectedOrderItem = null;
    }
    
    /**
     * Sets selectedCustomer to specified index from model
     * Updates address model to display customer's associated addresses
     * @param index 
     */
    private void selectCustomer(int index)
    {
        if(index < 0 || index > jcCustomerOption.getModel().getSize())
            index = 0;
        
        // Whenever we select a customer - we have to 
        // clear the order item model
        // this way if no order is selected
        // nothing should appear
        clearOrderItemPanel();
        
        selectedCustomer = (Customer) jcCustomerOption.getItemAt(index);
        
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
            index = 0;
        
        selectedAddress = (Address) jcCustomerAddressOption.getItemAt(index);
        
        if(selectedAddress == null)
            return;
        
        // now we have to update the order list to be based on the selected address
        orderModel.clear();
        clearOrderItemPanel();
        
        // Retrieve ALL orders for the selected address
        // Furthermore, include all the ITEMS associated for each order (backreference to OrderItem table)
        customerOrderCache = ApplicationDbContext.getInstance().orders.get((order)->order.getAddressId() == selectedAddress.getId(), "items");
        
        // Update the list containing all the orders
        orderModel.clear();
        orderModel.addAll(customerOrderCache);                
        
        if(customerOrderCache.size() > 0)
            selectOrder(0);               
        else
            btnFulfilled.setEnabled(false);
        recalculateOrderTotal();
    }
    
    private void selectOrder(int index)
    {
        selectedCustomerOrder = orderModel.get(index);
        
        // Should only be fulfilled if the order was submitted to the database (which will have an Id > 1
        this.btnFulfilled.setEnabled(selectedCustomerOrder.getId() > 0);
        
        // Now we need to populate the order item model
        orderItemModel.clear();
        orderItemModel.addAll(selectedCustomerOrder.getOrderItems());
        recalculateOrderTotal();
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
        productAreaScrollPane = new javax.swing.JScrollPane();
        productArea = new javax.swing.JPanel();
        btnFulfilled = new javax.swing.JButton();
        totalText = new javax.swing.JLabel();

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
        jlOrderItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlOrderItemsMouseClicked(evt);
            }
        });
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

        javax.swing.GroupLayout productAreaLayout = new javax.swing.GroupLayout(productArea);
        productArea.setLayout(productAreaLayout);
        productAreaLayout.setHorizontalGroup(
            productAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 793, Short.MAX_VALUE)
        );
        productAreaLayout.setVerticalGroup(
            productAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        productAreaScrollPane.setViewportView(productArea);

        btnFulfilled.setBackground(new java.awt.Color(51, 204, 255));
        btnFulfilled.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnFulfilled.setText("Fulfill");
        btnFulfilled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFulfilledActionPerformed(evt);
            }
        });

        totalText.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        totalText.setText("Total: $0");

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(btnRemoveOrderItem, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(totalText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSubmitOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFulfilled, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)))))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalText)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(productAreaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveOrderItem)
                    .addComponent(btnSubmitOrder)
                    .addComponent(btnFulfilled))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveOrderItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOrderItemActionPerformed
        delete();
    }//GEN-LAST:event_btnRemoveOrderItemActionPerformed

    private void btnSubmitOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitOrderActionPerformed
        save();
    }//GEN-LAST:event_btnSubmitOrderActionPerformed

    private void jcCustomerOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcCustomerOptionActionPerformed
        selectCustomer(jcCustomerOption.getSelectedIndex());
    }//GEN-LAST:event_jcCustomerOptionActionPerformed

    private void jcCustomerAddressOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcCustomerAddressOptionActionPerformed
        selectAddress(jcCustomerAddressOption.getSelectedIndex());
    }//GEN-LAST:event_jcCustomerAddressOptionActionPerformed

    private void jlOrderItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlOrderItemsMouseClicked
        /*
            This means the user has clicked on the order items panel --> thus selecting an item        
        */
        
        int selectedIndex = jlOrderItems.getSelectedIndex();
        
        // Avoid selecting a - "non-selected" index
        if(selectedIndex <= 0)
            return;
        
        selectedOrderItem = jlOrderItems.getSelectedValue();
    }//GEN-LAST:event_jlOrderItemsMouseClicked

    private void btnFulfilledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFulfilledActionPerformed
        /*
            By fulfilling an order -- it shall get removed            
        */
        
        if(selectedCustomerOrder == null)
        {
            JOptionPane.showMessageDialog(null, "No order selected", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(selectedCustomerOrder.getId() <= 0)
        {
            JOptionPane.showMessageDialog(null, "This order has not been submitted. Thus cannot be fulfilled", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        this.btnFulfilled.setEnabled(false);
        
        // Remove the order items from the database first -- dependency on customer order
        ApplicationDbContext.getInstance().orderItems.deleteWhere("orderId = " + selectedCustomerOrder.getId());
        
        // Remove the customer order from the database
        ApplicationDbContext.getInstance().orders.delete(selectedCustomerOrder.getId());
        
        // bye bye items
        orderItemModel.clear();
        
        selectedCustomerOrder = new CustomerOrder();
        selectedCustomerOrder.setAddressId(selectedAddress.getId());
        recalculateOrderTotal();
    }//GEN-LAST:event_btnFulfilledActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFulfilled;
    private javax.swing.JButton btnRemoveOrderItem;
    private javax.swing.JButton btnSubmitOrder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<Address> jcCustomerAddressOption;
    private javax.swing.JComboBox<Customer> jcCustomerOption;
    private javax.swing.JList<OrderItem> jlOrderItems;
    private javax.swing.JPanel productArea;
    private javax.swing.JScrollPane productAreaScrollPane;
    private javax.swing.JLabel totalText;
    // End of variables declaration//GEN-END:variables
    
    public void onPanelFocused()
    {
        initializePanel();        
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
    public void delete() 
    {
        if(selectedOrderItem == null)
            return;
        
        int index = jlOrderItems.getSelectedIndex();
        
        
        Logging.info(String.format("Removing %s - %s units from order", selectedOrderItem.getProduct().getName(), selectedOrderItem.getUnits()));
        System.out.println(selectedOrderItem.getId());
        
        // Delete the order item from the database (if it has an ID)
        if(selectedOrderItem.getId() > 0)
            ApplicationDbContext.getInstance().orderItems.delete(selectedOrderItem.getId());
        
        // Delete the item from the customer order (selected)
        selectedCustomerOrder.getOrderItems().remove(index);
        
        // Delete the item from the view
        orderItemModel.remove(index);
        
        // Reset selctedOrderItem as it should be "deleted" at this point
        selectedOrderItem = null;
        recalculateOrderTotal();
    }

    @Override
    public void save() {
        if(selectedCustomerOrder == null)
        {
            Logging.warning("Unable to save customer order. -- Customer order has not been set");
            return;
        }
        
        if(selectedCustomer == null)
        {
            Logging.warning("Unable to save customer order -- no customer selected");
            return;
        }
        
        if(selectedAddress == null)
        {
            Logging.warning("Unable to save customer order -- no address selected");
            return;
        }
            
        // Update the address to be whatever is selected
        selectedCustomerOrder.setAddressId(selectedAddress.getId());        
        
        try
        {
            // Save / update record based on id
            if(selectedCustomerOrder.getId() <= 0)
            {
                // Cannot forget to save this PK for later use from cached orders
                int pk = ApplicationDbContext.getInstance().orders.insert(selectedCustomerOrder);
                
                // Update ID for caching purposes
                selectedCustomerOrder.setId(pk);
            }
            else
                ApplicationDbContext.getInstance().orders.update(selectedCustomerOrder);
            
            // Must update all the associated order items
            for(OrderItem item : selectedCustomerOrder.getOrderItems())
            {
                // Ensure this value matches the customer order primary key
                item.setOrderId(selectedCustomerOrder.getId()); 
                
                if(item.getId() <= 0)
                {
                    int pk = ApplicationDbContext.getInstance().orderItems.insert(item);
                    item.setId(pk);
                }
                else
                    ApplicationDbContext.getInstance().orderItems.update(item);
            }
            
            // Inform the user that the order has been saved
            JOptionPane.showMessageDialog(null, "Order Saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
            btnFulfilled.setEnabled(selectedCustomerOrder.getId() > 0);
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
        
        // If no order is selected -- we're creating a new one
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
        
        recalculateOrderTotal();
    }
    
    void recalculateOrderTotal()
    {
        if(selectedCustomerOrder == null)
        {
            totalText.setText("Total: $0");
            return;
        }
        
        double total = 0.0;

        for(OrderItem item : selectedCustomerOrder.getOrderItems())
            total += item.getUnits() * item.getProduct().getPrice();
        
        DecimalFormat format = new DecimalFormat("0.00");
        totalText.setText(String.format("Total $%s", format.format(total)));
    }
}
