package com.example.demo.utils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return target.equals("")? true : matcher.find();
    }

    public static boolean isRegexPhone(String target) {
        Pattern pattern = Pattern.compile("\\d{3}-?\\d{4}-?\\d{4}");
        Matcher matcher = pattern.matcher(target);
        if (matcher.matches() || target.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isRegexBirth(String birth) {
        int birthYear = Integer.parseInt(birth.substring(0,4));
        int birthMonth = Integer.parseInt(birth.substring(4,6));
        int birthDay = Integer.parseInt(birth.substring(6,8));
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age > 13 ? true : false;
    }
}

