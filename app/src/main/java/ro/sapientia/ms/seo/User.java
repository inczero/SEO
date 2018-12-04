package ro.sapientia.ms.seo;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
//    private String profilePictureDownloadLink;
    private ArrayList<SmartOutlet> ownedProducts;

    public User(String firstName, String lastName, ArrayList<SmartOutlet> ownedProducts) {
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

    public ArrayList<SmartOutlet> getOwnedProducts() {
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

    //TODO : Check if an operation date is done -> remove it from the list
}
