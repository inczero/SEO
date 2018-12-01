package ro.sapientia.ms.seo;

public class SmartOutlet {
    private String identificationNumber;
    private String name;

    public SmartOutlet(String identificationNumber, String name) {
        this.identificationNumber = identificationNumber;
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
