package ro.sapientia.ms.seo.model;

import java.util.Date;

public class WeekDay {
//    private Date startingHour;
    private int startingHour;
    private int startingMinute;
    private int operationDuration;
    private boolean isThisDaySet;

    public WeekDay() {
        isThisDaySet = false;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public int getStartingMinute() {
        return startingMinute;
    }

    public int getOperationDuration() {
        return operationDuration;
    }

    public boolean isThisDaySet() {
        return isThisDaySet;
    }

    public void setStartingHourAndMinute(int startingHour, int startingMinute) {
        this.startingHour = startingHour;
        this.startingMinute =startingMinute;
    }

    public void setOperationDuration(int operationDuration) {
        this.operationDuration = operationDuration;
    }

    public void setThisDay(boolean thisDaySet) {
        isThisDaySet = thisDaySet;
    }
}
