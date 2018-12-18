package ro.sapientia.ms.seo.model;

import java.util.List;

public class SmartOutlet {
    private String identificationNumber;
    private String name;
    private boolean status;
    private List<OperatingDate> operatingDates;

    public SmartOutlet(String identificationNumber, String name, boolean status, List<OperatingDate> operatingDates) {
        this.identificationNumber = identificationNumber;
        this.name = name;
        this.status = status;
        this.operatingDates = operatingDates;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public List<OperatingDate> getOperatingDates() {
        return operatingDates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void addNewOperatingDate(OperatingDate operatingDate) {
        operatingDates.add(operatingDate);
    }

    public void deleteOperatingDate(OperatingDate operatingDate) {
        operatingDates.remove(operatingDate);
    }
}
