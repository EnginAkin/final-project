package org.norma.finalproject.customer.core.utilities;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Calendar.*;

public class Utils {

    private Utils() {

    }

    public static List<String> SimpleGrantedAuthorityToListString(Collection<SimpleGrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }


    public static boolean isOver18YearsOld(Date date) {
        Calendar a = getCalendar(date);
        Calendar b = getCalendar(new Date());
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff >= 18;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
