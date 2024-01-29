package customer;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private int loyaltyCard;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(int loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public Customer(int id, String firstName, String lastName, int loyaltyCard) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setLoyaltyCard(loyaltyCard);
    }

    public String toString() {
        return "Customer [ First Name=" + firstName + ", Last Name=" + lastName + ", Loyalty Card=" + loyaltyCard + "]";
    }
}

