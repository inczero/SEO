package ro.sapientia.ms.seo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class SmartOutlet {
    private String identificationNumber;
    private String name;
    private boolean status;
    private List<WeekDay> weekSchedule;
    private List<Integer> consumptionData;

    public SmartOutlet(String identificationNumber, String name) {
        this.identificationNumber = identificationNumber;
        this.name = name;
        status = false;
        weekSchedule = new ArrayList<>(7);
        for (int i=0; i<7; i++) {
            weekSchedule.add(new WeekDay());
        }
        consumptionData = new ArrayList<>(12);
    }

    public SmartOutlet() {
        this.identificationNumber = "NO DATA";
        this.name = "NO DATA";
        status = false;
        weekSchedule = new ArrayList<>(7);
        for (int i=0; i<7; i++) {
            weekSchedule.add(new WeekDay());
        }
        consumptionData = new ArrayList<>(12);
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

    public List<WeekDay> getWeekSchedule() {
        return weekSchedule;
    }

    public WeekDay getWeekDay(int index) {
        if (index < 7 && index >= 0) {
            return weekSchedule.get(index);
        }
        return weekSchedule.get(0);
    }

    public List<Integer> getConsumptionData() {
        return consumptionData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean setOperationWeekDay(int weekDay, int startingHour, int startingMinute, int duration) {
        if (weekSchedule.get(weekDay).isThisDaySet()) {
            return false;
        } else {
            weekSchedule.get(weekDay).setStartingHourAndMinute(startingHour, startingMinute);
            weekSchedule.get(weekDay).setOperationDuration(duration);
            weekSchedule.get(weekDay).setThisDay(true);
            return true;
        }
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setWeekSchedule(List<WeekDay> weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public void setConsumptionData(ArrayList<Integer> consumptionData) {
        this.consumptionData = consumptionData;
    }

    public void setWeekDayStatus(int weekDay, boolean status) {
        weekSchedule.get(weekDay).setThisDay(status);
    }

    public void addConsumptionData(int data) {
        consumptionData.add(data);
    }

    public WeekDay operationToday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return weekSchedule.get(0);
            case Calendar.TUESDAY:
                return weekSchedule.get(1);
            case Calendar.WEDNESDAY:
                return weekSchedule.get(2);
            case Calendar.THURSDAY:
                return weekSchedule.get(3);
            case Calendar.FRIDAY:
                return weekSchedule.get(4);
            case Calendar.SATURDAY:
                return weekSchedule.get(5);
            case Calendar.SUNDAY:
                return weekSchedule.get(6);
        }

        return new WeekDay();
    }
}