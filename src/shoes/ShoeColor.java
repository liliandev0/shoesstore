package shoes;

public class ShoeColor {

    private int ID;
    private String color;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ShoeColor(int id, String color) {
        setID(id);
        setColor(color);
    }
}
