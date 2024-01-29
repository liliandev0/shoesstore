package shoes;

public class ShoeType {

    private int ID;
    private String shoeType;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }

    public ShoeType(int id, String shoeType){
        setID(id);
        setShoeType(shoeType);
    }

    public String toString() {
        return "[" + "ID: " + ID + " ShoeType: " + shoeType + "]";
    }
}
