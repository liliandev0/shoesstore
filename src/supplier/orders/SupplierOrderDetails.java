package supplier.orders;

public class SupplierOrderDetails {

    private int orderSupplierID;
    private int shoeID;
    private int quantity;
    private float unitPrice;

    public int getOrderSupplierID() {
        return orderSupplierID;
    }

    public void setOrderSupplierID(int orderSupplierID) {
        this.orderSupplierID = orderSupplierID;
    }

    public int getShoeID() {
        return shoeID;
    }

    public void setShoeID(int shoeID) {
        this.shoeID = shoeID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public SupplierOrderDetails(int orderSupplierID, int shoeID, int quantity, float unitPrice){
        setOrderSupplierID(orderSupplierID);
        setShoeID(shoeID);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }
}
