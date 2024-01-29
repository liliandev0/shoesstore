package supplier;

public class Supplier {

    private int id;
    private String supplierName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Supplier(int id, String supplierName){
        setId(id);
        setSupplierName(supplierName);
    }

    public String toString(){
        return "Name: " + this.supplierName;
    }

}
