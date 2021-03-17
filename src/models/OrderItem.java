package models;

public class OrderItem
{
    private int orderId;
    private Order order;

    private int productId;
    private Product product;

    private int id;

    public void setOrderId(int value) { orderId = value; }
    public void setId(int value) { id = value; }
    public void setProductId(int value) { productId = value;}

    public void setOrder(Order order) { this.order = order; }
    public void setProduct(Product product) { this.product = product; }

    public Order getOrder() { return order; }
    public Product getProduct() { return product; }
    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getProductId() { return productId; }
}
