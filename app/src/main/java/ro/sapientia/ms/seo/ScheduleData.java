package ro.sapientia.ms.seo;

//This class is for the ScheduleFragment
public class ScheduleData {
    private String name;
    private OperatingDate operationDate;

    public ScheduleData(String name, OperatingDate operationDate) {
        this.name = name;
        this.operationDate = operationDate;
    }

    public String getName() {
        return name;
    }

    public OperatingDate getOperationDate() {
        return operationDate;
    }
}
