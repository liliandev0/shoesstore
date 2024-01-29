package customer.sales;

import java.util.Date;

public class CustomerSales {

    private int id;
    private int productId;
    private int quantity;
    private float totalAmount;
    private Date saleDate;
    private int customerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public CustomerSales(int id, int productId, int quantity, float totalAmount, Date saleDate, int customerId) {
        setId(id);
        setProductId(productId);
        setQuantity(quantity);
        setTotalAmount(totalAmount);
        setSaleDate(saleDate);
        setCustomerId(customerId);
    }

    public String toString() {
        return "CustomerSale [ID=" + id + ", ProductID=" + productId + ", Quantity=" + quantity +
                ", TotalAmount=" + totalAmount + ", SaleDate=" + saleDate + ", CustomerID=" + customerId + "]";
    }
}
