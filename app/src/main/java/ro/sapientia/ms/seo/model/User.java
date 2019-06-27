package ro.sapientia.ms.seo.model;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private ArrayList<SmartOutlet> ownedProducts;

    public User() {
        ownedProducts = new ArrayList<>();
    }

    public User(String firstName, String lastName, ArrayList<SmartOutlet> ownedProducts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ownedProducts = ownedProducts;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        ownedProducts = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArrayList<SmartOutlet> getOwnedProducts() {
        return ownedProducts;
    }

    public SmartOutlet getSmartOutlet(int index) {
        if ((ownedProducts.size() > 0) && (index <= ownedProducts.size()-1)) {
            return ownedProducts.get(index);
        }
        return new SmartOutlet();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
