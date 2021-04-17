package models;

import orm.annotations.ForeignKey;
import orm.annotations.Id;
import orm.annotations.NotMapped;
import persistence.ApplicationDbContext;

public class OrderItem
{
    @ForeignKey(referenceClass = CustomerOrder.class, backreferenceVariableName = "customerOrder")
    private int orderId;

    @NotMapped
    private CustomerOrder customerOrder;

    @ForeignKey(referenceClass = Product.class, backreferenceVariableName = "product")
    private int productId;

    @NotMapped
    private Product product;

    private double units;

    @Id
    private int id;

    public void setUnits(double value) { units = value; }
    public void setOrderId(int value) { orderId = value; }
    public void setId(int value) { id = value; }
    public void setProductId(int value) { productId = value;}

    public void setOrder(CustomerOrder customerOrder) { this.customerOrder = customerOrder; }
    public void setProduct(Product product) { this.product = product; }

    public CustomerOrder getOrder() { return customerOrder; }
    
    public Product getProduct() 
    {
        if(product != null)
            return product;
        
        product = ApplicationDbContext.getInstance().products.find(productId);
        return product;
    }
    
    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
    public double getUnits() { return units; }
    
    @Override
    public String toString()
    {
        if(product == null)
        {
            product = ApplicationDbContext.getInstance().products.find(productId);                       
        }
        
        return product.getName() + ", " + units + " units";
    }
}
