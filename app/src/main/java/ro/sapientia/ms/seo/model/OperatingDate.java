package ro.sapientia.ms.seo.model;

import java.util.Date;

public class OperatingDate {
    private Date startDate;
    private Date finishDate;
    private boolean isTheOperationStarted;

    public OperatingDate(Date startDate, Date finishDate, boolean isTheOperationStarted) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isTheOperationStarted = isTheOperationStarted;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public boolean isTheOperationStarted() {
        return isTheOperationStarted;
    }

    public void setTheOperationStarted(boolean theOperationStarted) {
        isTheOperationStarted = theOperationStarted;
    }
}
