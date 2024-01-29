package shoes;

public class Shoes {
    private int ID;
    private int brand;
    private int model;
    private float size;
    private int shoeType;
    private int shoeColor;
    private float price;
    private int quantity;
    private float purchasePrice;
    private float sellingPrice;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getShoeType() {
        return shoeType;
    }

    public void setShoeType(int shoeType) {
        this.shoeType = shoeType;
    }

    public int getShoeColor() {
        return shoeColor;
    }

    public void setShoeColor(int shoeColor) {
        this.shoeColor = shoeColor;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Shoes(int id, int brand, int model, float size, int shoeColor, int shoeType, float price, int quantity, float purchasePrice, float sellingPrice){
        this.ID = id;
        this.brand = brand;
        this.model = model;
        this.size = size;
        this.shoeType = shoeType;
        this.shoeColor = shoeColor;
        this.price = price;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
    }

    public String toString(){
        return "[Brand: " + brand + " Model: " + model  + " Size: " +  size +
                " Type: " + shoeType + " Color: " + shoeColor + " Price: " + price +
                " Quantity: " + quantity + "]";
    }
}
