package ro.sapientia.ms.seo;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
//    private String profilePictureDownloadLink;
    private ArrayList<SmartSocket> ownedProducts;

    public User(String firstName, String lastName, ArrayList<SmartSocket> ownedProducts) {
        this.firstName = firstName;
        this.lastName = lastName;
        //this.profilePictureDownloadLink = profilePictureDownloadLink;
        this.ownedProducts = ownedProducts;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

//    public String getProfilePictureDownloadLink() {
//        return profilePictureDownloadLink;
//    }

    public ArrayList<SmartSocket> getOwnedProducts() {
        return ownedProducts;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public void setProfilePictureDownloadLink(String profilePictureDownloadLink) {
//        this.profilePictureDownloadLink = profilePictureDownloadLink;
//    }
}
