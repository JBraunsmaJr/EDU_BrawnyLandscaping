/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import orm.Exceptions.ValidationException;
import persistence.*;
import models.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonbr
 */
public class CustomerPanel extends javax.swing.JPanel 
{
    private JStringList customerListModel;
    private DefaultTableModel customerAddressModel;
    private ArrayList<Customer> customers;
    private Customer selectedCustomer;
    
    /**
     * Tracks the previous customer that was selected (if applicable)
     */
    private int previouslySelectedCustomerIndex = -1;
    
    /**
     * Does the Customer Panel have any pending changes
     * false -> no changes
     * true -> user has made some changes
     */
    private boolean isDirty;
    
    /**
     * Creates new form CustomerPanel
     */
    public CustomerPanel() {
        initComponents();
        initCustom();
        setDirty(false);
    }
    
    /**
     * Helps with functionality on the form
     * Save button is not available if data has not been changed
     * @param value 
     */
    void setDirty(boolean value)
    {
        isDirty = value;
        btnSaveCustomer.setEnabled(value);
    }
    
    void initCustom()
    {
        selectedCustomer = new Customer();

        try
        {
            //customers = CustomerDbEntity.GET_CUSTOMERS(true); // yes include all the addresses associated with each customer
            customers = ApplicationDbContext.getInstance().customers.get("addresses");

            customerListModel = new JStringList();
            customerAddressModel = (DefaultTableModel) tableAddresses.getModel();
            
            // Allow user to modify address data on the right hand side
            customerAddressModel.addTableModelListener(new TableModelListener()
            {
                public void tableChanged(TableModelEvent event)
                {
                    int rowIndex = event.getFirstRow();
                    int colIndex = event.getColumn();
                    
                    /**
                     * Since we are allowing the user to modify address data in an excel like format
                     * we must track acquire the row position (aka the index within the selectedCustomer -> Address arrayList
                     * Then the headers which are 0 - 3 (street, city, state, zip)
                     * 
                     * With that information we can easily acquire the updated data and populate the necessary address instance
                     */
                    
                    if(rowIndex >= 0 && rowIndex < selectedCustomer.getAddresses().size() && colIndex >= 0)
                    {
                        Address item = (Address) selectedCustomer.getAddresses().toArray()[rowIndex];
                        String value = (String) customerAddressModel.getValueAt(rowIndex, colIndex);
                        
                        switch(colIndex)
                        {
                            case 0:
                                item.setStreet(value);
                                break;
                            case 1:
                                item.setCity(value);
                                break;
                            case 2:
                                item.setState(value);
                                break;
                            case 3:
                                item.setZip(value);
                                break;
                        }
                        
                        setDirty(true);
                        // at this point the referenced address instance has been populated in the selected customer
                    }
                }
            });

            listCustomers.setModel(customerListModel);
            tableAddresses.setModel(customerAddressModel);
            
            // Allow user to select from left panel to populate the form
            listCustomers.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent event)
                {
                    if(event.getClickCount() == 1)
                    {
                        int index = listCustomers.locationToIndex(event.getPoint());
                        
                        if(index >= 0)
                        {
                            // We must ask the user if they want to discard their changes
                            selectCustomer(index);
                        }
                    }
                }
            });

            populateCustomerList();
            resetCustomerData();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }    
    
    private void populateCustomerList()
    {
        customerListModel.clear();
        
        for(Customer customer : customers)
        {
            customerListModel.add(String.format("%s %s, %s", 
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail()));
        }
    }
    
    /**
     * Select customer from the listCustomersModel
     * @param index 
     */
    private void selectCustomer(int index)
    {
        if(isDirty)
        {
            int confirmSelection = JOptionPane.showConfirmDialog(null, "Do you want to discard these changes?", "You have pending changes", JOptionPane.YES_NO_OPTION);

            if(confirmSelection == JOptionPane.YES_OPTION)
            {
                Object item = listCustomers.getModel().getElementAt(index);
                previouslySelectedCustomerIndex = index;
            }
            else
            {
                if(previouslySelectedCustomerIndex < 0)
                    listCustomers.clearSelection();
                else
                    listCustomers.setSelectedIndex(previouslySelectedCustomerIndex);
                
                return;
            }
        }
        else
        {
            Object item = listCustomers.getModel().getElementAt(index);
            previouslySelectedCustomerIndex = index;
        }
        
        resetCustomerData();
        
        selectedCustomer = (Customer) customers.toArray()[index];
        
        txtFirstName.setText(selectedCustomer.getFirstName());
        txtLastName.setText(selectedCustomer.getLastName());
        txtEmail.setText(selectedCustomer.getEmail());
        txtPhone.setText(selectedCustomer.getPhone());
        
        // validate our selection
        if(selectedCustomer == null)
            return;
        
        populateAddresses();
    }
    
    /**
     * Populate address list with the ones from selected customer
     */
    private void populateAddresses()
    {
        if(selectedCustomer == null)
            return;
        
        for(Address address : selectedCustomer.getAddresses())
        {
            customerAddressModel.addRow(new String[]
            {  
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZip()
            });
        }
    }

    /**
     * Is the specified field empty/null in value?
     * @param field
     * @return 
     */
    boolean isEmpty(JTextField field)
    {
        if(field.getText() == null)
            return true;
        
        return field.getText().isEmpty() || field.getText().isBlank();
    }

    /**
     * Reset the form for new submissions
     */
    void resetCustomerData()
    {
        selectedCustomer = new Customer();
        
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        
        listCustomers.clearSelection();
        
        // Clear address table
        while(customerAddressModel.getRowCount() > 0)
            customerAddressModel.removeRow(0);
        
        tableAddresses.clearSelection();
        setDirty(false); // no longer have pending changes
    }
    
    /**
     * Populate selected customer with information currently filled out on the form
     */
    void hydrateSelectedCustomer()
    {
        if(selectedCustomer == null)
            selectedCustomer = new Customer();
        
        selectedCustomer.setFirstName(txtFirstName.getText());
        selectedCustomer.setLastName(txtLastName.getText());
        selectedCustomer.setEmail(txtEmail.getText());
        selectedCustomer.setPhone(txtPhone.getText());
    }
    
    /**
     * Save form information to the database
     */
    void saveCustomer()
    {
        // Either create a new instance of use existing instance
        Customer customer = selectedCustomer == null ? new Customer() : selectedCustomer;
        
        customer.setFirstName(txtFirstName.getText());
        customer.setLastName(txtLastName.getText());
        customer.setEmail(txtEmail.getText());

        if(!isEmpty(txtPhone))
            customer.setPhone(txtPhone.getText());

        try
        {
            // PK default value would be less than 1
            // So if >= 1 we can update record
            if(customer.getId() < 1)
            {
                int newId = ApplicationDbContext.getInstance().customers.insert(customer);
                customer.setId(newId);
                customers.add(customer);            
            }
            else
            {
                ApplicationDbContext.getInstance().customers.update(customer);
            }
            
            // recreates the labels in the proper order
            populateCustomerList();
            
            // Update/Add any addresses (if applicable)
            for(Address address : customer.getAddresses())
            {
                address.setCustomerId(customer.getId());

                if(address.getId() < 1)
                {
                    int newId = ApplicationDbContext.getInstance().addresses.insert(address);
                    address.setId(newId);
                }
                else
                    ApplicationDbContext.getInstance().addresses.update(address);
            }
            
            resetCustomerData();
        }
        catch(ValidationException ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString(), "Invalid", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving customer");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listCustomers = new javax.swing.JList<>();
        btnAddCustomer = new javax.swing.JButton();
        btnDeleteCustomer = new javax.swing.JButton();
        labelFirst = new javax.swing.JLabel();
        txtFirstName1 = new javax.swing.JLabel();
        txtFirstName2 = new javax.swing.JLabel();
        txtFirstName3 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        txtFirstName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        btnSaveCustomer = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAddresses = new javax.swing.JTable();
        lblAddressErrorText = new javax.swing.JLabel();
        btnAddAddress = new javax.swing.JButton();
        btnDeleteAddress = new javax.swing.JButton();

        listCustomers.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(listCustomers);

        btnAddCustomer.setBackground(new java.awt.Color(0, 255, 102));
        btnAddCustomer.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnAddCustomer.setForeground(new java.awt.Color(0, 0, 0));
        btnAddCustomer.setText("ADD");
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });

        btnDeleteCustomer.setBackground(new java.awt.Color(255, 0, 51));
        btnDeleteCustomer.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnDeleteCustomer.setText("DELETE");
        btnDeleteCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCustomerActionPerformed(evt);
            }
        });

        labelFirst.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        labelFirst.setText("First Name");

        txtFirstName1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtFirstName1.setText("Last Name");

        txtFirstName2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtFirstName2.setText("Email");

        txtFirstName3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtFirstName3.setText("Phone");

        txtLastName.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtLastName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLastNameFocusLost(evt);
            }
        });

        txtFirstName.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFirstNameFocusLost(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });

        txtPhone.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhoneFocusLost(evt);
            }
        });

        btnSaveCustomer.setText("Save");
        btnSaveCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCustomerActionPerformed(evt);
            }
        });

        tableAddresses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Street", "City", "State", "Zip"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableAddresses);

        lblAddressErrorText.setForeground(new java.awt.Color(255, 0, 51));

        btnAddAddress.setBackground(new java.awt.Color(0, 255, 102));
        btnAddAddress.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnAddAddress.setForeground(new java.awt.Color(0, 0, 0));
        btnAddAddress.setText("ADD");
        btnAddAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAddressActionPerformed(evt);
            }
        });

        btnDeleteAddress.setBackground(new java.awt.Color(255, 0, 51));
        btnDeleteAddress.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnDeleteAddress.setText("DELETE");
        btnDeleteAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAddressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(labelFirst)
                            .addGap(18, 18, 18)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtFirstName1)
                                .addComponent(txtFirstName2))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(txtFirstName3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnSaveCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(445, 445, 445)
                        .addComponent(lblAddressErrorText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFirst)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFirstName1)
                            .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFirstName2)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAddressErrorText)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtFirstName3)))
                .addGap(18, 18, 18)
                .addComponent(btnSaveCustomer)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomerActionPerformed
        // We want to reset the form
        resetCustomerData();
        
        // Hydrate new customer
        hydrateSelectedCustomer();
    }//GEN-LAST:event_btnAddCustomerActionPerformed

    private void btnDeleteCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCustomerActionPerformed
        if(selectedCustomer != null)
        {
            try
            {
                ApplicationDbContext.getInstance().addresses.deleteWhere(String.format("customerId = %s", selectedCustomer.getId()));
                ApplicationDbContext.getInstance().customers.delete(selectedCustomer.getId());

                customers.remove(selectedCustomer);
                
                customerListModel.removeElement(String.format("%s %s, %s", 
                        selectedCustomer.getFirstName(),
                        selectedCustomer.getLastName(),
                        selectedCustomer.getEmail()));
                
                selectedCustomer = null;
                
                // We must refresh the form
                resetCustomerData();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting record");
            }
        }
    }//GEN-LAST:event_btnDeleteCustomerActionPerformed

    private void btnSaveCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCustomerActionPerformed
        if(selectedCustomer.getAddresses().size() > 0)
        {
            for(int i = 0; i < selectedCustomer.getAddresses().size(); i++)
            {
                Address address = selectedCustomer.getAddresses().get(i);

                if(!AddressDbEntity.IS_VALID(address))
                {
                    ArrayList<String> errors = AddressDbEntity.VALIDATE(address);
                    JOptionPane.showMessageDialog(null, String.join("\n", errors));
                    
                    // set the selected item in address table
                    this.tableAddresses.setRowSelectionInterval(i, i);
                    
                    return;
                }
            }
        }
        
        if(CustomerDbEntity.IS_VALID(selectedCustomer))
        {
            saveCustomer();
            resetCustomerData();
        }
        else
        {
            ArrayList<String> errors = CustomerDbEntity.VALIDATE(selectedCustomer);
            JOptionPane.showMessageDialog(null, String.join(",", errors));
        }
    }//GEN-LAST:event_btnSaveCustomerActionPerformed

    private void btnAddAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAddressActionPerformed
        Address address = new Address();
        address.setCustomerId(selectedCustomer.getId());
        selectedCustomer.getAddresses().add(address);
        this.customerAddressModel.addRow(new String[] { "", "", "", "" });
        setDirty(true);        
    }//GEN-LAST:event_btnAddAddressActionPerformed

    private void btnDeleteAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAddressActionPerformed
        int rowIndex = tableAddresses.getSelectedRow();
        int pk = selectedCustomer.getAddresses().get(rowIndex).getId();
        
        customerAddressModel.removeRow(rowIndex);
        // ensure we update our address array on selected customer
        selectedCustomer.getAddresses().remove(rowIndex);
        
        try
        {
            AddressDbEntity.DELETE_RECORD(pk);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error occurred while deleting address record");
        }
    }//GEN-LAST:event_btnDeleteAddressActionPerformed

    private void txtFirstNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFirstNameFocusLost
        if(txtFirstName.getText() != selectedCustomer.getFirstName())
            setDirty(true);
        selectedCustomer.setFirstName(txtFirstName.getText());
    }//GEN-LAST:event_txtFirstNameFocusLost

    private void txtLastNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLastNameFocusLost
        if(txtLastName.getText() != selectedCustomer.getLastName())
                    setDirty(true);
        selectedCustomer.setLastName(txtLastName.getText());
    }//GEN-LAST:event_txtLastNameFocusLost

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        if(txtEmail.getText() != selectedCustomer.getEmail())
            setDirty(true);
        selectedCustomer.setEmail(txtEmail.getText());
        
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneFocusLost
        if(txtPhone.getText() != selectedCustomer.getPhone())
            setDirty(true);
        selectedCustomer.setPhone(txtPhone.getText());
    }//GEN-LAST:event_txtPhoneFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAddress;
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnDeleteAddress;
    private javax.swing.JButton btnDeleteCustomer;
    private javax.swing.JButton btnSaveCustomer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelFirst;
    private javax.swing.JLabel lblAddressErrorText;
    private javax.swing.JList<String> listCustomers;
    private javax.swing.JTable tableAddresses;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JLabel txtFirstName1;
    private javax.swing.JLabel txtFirstName2;
    private javax.swing.JLabel txtFirstName3;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
