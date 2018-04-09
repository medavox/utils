package com.medavox.util.io;

import java.util.Calendar;
import java.util.Locale;

/**@author Adam Howard
 * on 04/05/2016
 * Static methods for pretty-printing a unix-epoch time in various brief but human-readable formats.
 * Uses 24-hour UK date formats, where the units are listed in ascending order of size.
 * For instance: 23:59:59.999 on 31/12/2020.
 */
public abstract class DateTime {

    private static Calendar cal = Calendar.getInstance();

    public enum TimeFormat {
        MINUTES(0),
        SECONDS(10),
        MILLISECONDS(20);

        private int precision;
        TimeFormat(int precisionLevel) {
            this.precision = precisionLevel;
        }

        public int precision() {
            return precision;
        }
    }

    public static String getTime(long timeInMillis, TimeFormat timeFormat) {
        cal.setTimeInMillis(timeInMillis);
        String output = "";
        //always include hour and minutes
        output += String.format(Locale.UK, "%02d", cal.get(Calendar.HOUR_OF_DAY));
        output += ":"+String.format(Locale.UK, "%02d", cal.get(Calendar.MINUTE));

        if(timeFormat.precision() >= TimeFormat.SECONDS.precision()) {
            //if we're more precise than simple, include seconds
            output += ":"+String.format(Locale.UK, "%02d", cal.get(Calendar.SECOND));
        }
        if(timeFormat.precision() >= TimeFormat.MILLISECONDS.precision()) {
            //if we're at least as precise as MILLISECONDS, include milliseconds
            output += "."+String.format(Locale.UK, "%03d", cal.get(Calendar.MILLISECOND));
        }

        return output;
    }

    public enum DateFormat {
        /**eg 31/12*/
        NUMBERS(0),

        /**eg 31 Dec*/
        BRIEF(10),

        /**eg Tue 31 Dec*/
        BRIEF_WITH_DAY(20),

        /**eg 31 December*/
        LONG(30),

        /**eg Tuesday 31 December*/
        LONG_WITH_DAY(40);

        private int dateLength;
        DateFormat(int len) {
            this.dateLength = len;
        }

        public int length() {
            return dateLength;
        }
    }

    public static String getDate(long timeInMillis, DateFormat len) {
        cal.setTimeInMillis(timeInMillis);
        String output = "";

        boolean withWeekday = (len == DateFormat.BRIEF_WITH_DAY || len == DateFormat.LONG_WITH_DAY);

        String dayOfWeek = (withWeekday ? cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.UK)+" " : "");
        String day = ""+cal.get(Calendar.DAY_OF_MONTH);
        String month = "";

        switch (len) {
            case NUMBERS:
                month = "/"+cal.get(Calendar.MONTH);
                break;
            case BRIEF:
            case BRIEF_WITH_DAY:

                month = " "+cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
                break;
            case LONG:
            case LONG_WITH_DAY:
                month = " "+cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK);
                break;
        }
        return dayOfWeek+day+month;

    }

    public static String get(long timeInMillis, TimeFormat precision) {
        return get(timeInMillis, precision, DateFormat.BRIEF);
    }

    public static String get(long timeInMillis, TimeFormat precision, DateFormat dateLen) {
        return getDate(timeInMillis, dateLen) + " " + getTime(timeInMillis, precision);
    }

    public static String get2(long timeInMillis, TimeFormat precision, DateFormat dateLen) {
        return getTime(timeInMillis, precision)+" on "+getDate(timeInMillis, dateLen);
    }

    /**Print the duration of something in human-readable format,
     * displaying only the 2 largest non-zero time units.*/
    public static String getDuration(long duration) {
        long dur = Math.abs(duration); //even if it's in the past, make it positive
        int[] amounts = {0, 0, 0};
        String[] unitNames = {"day", "hour", "minute"};
        amounts[0] = (int) (dur / (24 * 60 * 60 * 1000));//days
        amounts[1] = (int) ((dur / (1000*60*60)) % 24);//hours
        amounts[2] = (int) ((dur / (1000*60)) % 60);//minutes
        //amounts[3] = (int) (dur  / 1000) % 60 ;//seconds

        int rawSeconds = (int)(dur / 1000);

        //if it's less than 2 minutes, just return this as seconds
        if(rawSeconds <= 120) {
            return unitString(rawSeconds, "second");
        }

        //only display minutes or larger
        int unitsCounted = 0;
        String ret = "";
        for(int i = 0; i < amounts.length && unitsCounted < 2; i++) {
            if(amounts[i] > 0) {
                //if(i == amounts.length-1 && amounts[i-1] >= 5)//if we're dealing with >5 minutes
                ret += unitString(amounts[i], unitNames[i])+" ";
                unitsCounted++;
            }
        }
        return ret;
    }

    private static String unitString(int amount, String unit) {
        String ret = (amount> 0 ? amount+" "+unit : "");
        ret += (amount > 1 ? "s" : "");
        return ret;
    }
}
