package project3;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public abstract class CampSite implements Serializable {
    //Serializable converts the state of an object int a byte stream. Deserialization reverses this.

    // Version control for our class. If anything changes a new serialVersionUID will be used and
    // trying to deserialize an older version will result in an error, I think.
    private static final long serialVersionUID = 1L;

    protected String guestName;
    protected GregorianCalendar checkIn;
    protected GregorianCalendar estimatedCheckOut;
    protected GregorianCalendar actualCheckOut;
    protected int daysOverdue;

    public CampSite() {
    }

    public abstract double getCost(GregorianCalendar checkOut);

    public CampSite(String guestName,
                    GregorianCalendar checkIn,
                    GregorianCalendar estimatedCheckOut,
                    GregorianCalendar actualCheckOut) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.estimatedCheckOut = estimatedCheckOut;
        this.actualCheckOut = actualCheckOut;
        this.daysOverdue = 0;
    }

    public int getDaysOverdue() {
        return this.daysOverdue;
    }

    public void setDaysOverdue(GregorianCalendar date) {
        long days = (date.getTimeInMillis() - this.estimatedCheckOut.getTimeInMillis()) / (1000 * 60 * 60 * 24);

        if (days <= 0) {
            this.daysOverdue = 0;
        } else {
            this.daysOverdue = (int) days;
        }
    }
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public GregorianCalendar getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(GregorianCalendar checkIn) {
        this.checkIn = checkIn;
    }

    public GregorianCalendar getEstimatedCheckOut() {
        return estimatedCheckOut;
    }

    public void setEstimatedCheckOut(GregorianCalendar estimatedCheckOut) {
        this.estimatedCheckOut = estimatedCheckOut;
    }

    public GregorianCalendar getActualCheckOut() {
        return actualCheckOut;
    }

    public void setActualCheckOut(GregorianCalendar actualCheckOut) {
        this.actualCheckOut = actualCheckOut;
    }

    // following code used for debugging only
    // IntelliJ using the toString for displaying in debugger.
    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        String checkInOnDateStr;
        if (getCheckIn() == null)
            checkInOnDateStr = "";
        else
            checkInOnDateStr = formatter.format(getCheckIn().getTime());

        String  estCheckOutStr;
        if (getEstimatedCheckOut() == null)
            estCheckOutStr = "";
        else
            estCheckOutStr = formatter.format(getEstimatedCheckOut().getTime());

        String checkOutStr;
        if (getActualCheckOut() == null)
            checkOutStr = "";
        else
            checkOutStr = formatter.format(getActualCheckOut().getTime());

        return "CampSite{" +
                "guestName='" + guestName + '\'' +
                ", checkIn=" + checkInOnDateStr +
                ", estimatedCheckOut=" + estCheckOutStr +
                ", actualCheckOut=" + checkOutStr +
                '}';
    }
}
