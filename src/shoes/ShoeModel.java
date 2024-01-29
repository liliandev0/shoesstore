package shoes;

public class ShoeModel {

    private int ID;
    private String model;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ShoeModel(int ID, String model){
        setID(ID);
        setModel(model);
    }

    public String toString() {
        return "[" + "ID: " + ID + " ShoeModel: " + model + "]";
    }
}
