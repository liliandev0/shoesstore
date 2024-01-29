package shoes;

public class ShoeBrand {

    private int ID;
    private String brandName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public ShoeBrand(int ID, String brandName){
        setID(ID);
        setBrandName(brandName);
    }

    public String toString() {
        return "[" + "ID: " + ID + " BrandName: " + brandName + "]";
    }
}
