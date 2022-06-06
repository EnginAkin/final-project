package org.norma.finalproject.common.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException();
    }


    public static Date get30DaysAgo(Date today) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);// default a month getting activities
        Date aMonthAgo = cal.getTime();
        return aMonthAgo;
    }


}
