package project3;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RV extends CampSite {

private int power;

    public RV() {
    }

    public RV(String guestName, GregorianCalendar checkIn, GregorianCalendar estimatedCheckOut, GregorianCalendar actualCheckOut, int power) {
        super(guestName, checkIn, estimatedCheckOut, actualCheckOut);
        this.power = power;
        }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public double getCost(GregorianCalendar checkOut) {
        long days = 0;
        GregorianCalendar date = (GregorianCalendar) checkIn.clone();

        while(!date.equals(checkOut)){
            days++;
            date.add(Calendar.DAY_OF_MONTH, 1);
        }

        double cost = 10;
        if (this.power > 1000) {
            return (30 * days) + cost;
        } else {
            return (20 * days) + cost;
        }
    }

    @Override
    public String toString() {
        return "RV{" +
                "power=" + power +
                super.toString() +
                '}';
    }
}