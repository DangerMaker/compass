package com.ez08.compass.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTool {

    public static boolean isInTrade() {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        String currentTime = format
                .format(new Date(System.currentTimeMillis()));


        int startH = 9;
        int startM = 30;
        int endH = 15;
        int endM = 30;

        String[] hm = currentTime.split("-");

        int curH = Integer.valueOf(hm[0]);
        int curM = Integer.valueOf(hm[1]);

        if (curH < startH) {
            return false;
        } else if (curH == startH) {
            if (curM < startM) {
                return false;
            }
        }

        if (curH > endH) {
            return false;
        } else if (curH == endH) {
            if (curM > endM) {
                return false;
            }
        }

        return true;

    }

    /**
     * 8点到集合竞价阶段
     *
     * @return
     */
    public static boolean isBeforeTotalTrade() {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        String currentTime = format
                .format(new Date(System.currentTimeMillis()));


        int startH = 9;
        int startM = 15;

        String[] hm = currentTime.split("-");

        int curH = Integer.valueOf(hm[0]);
        int curM = Integer.valueOf(hm[1]);
        if (curH == 8) {
            return true;
        }
        if (curH == startH) {
            if (curM <= startM) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合竞价阶段
     *
     * @return
     */
    public static boolean isInTotalTrade() {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        String currentTime = format
                .format(new Date(System.currentTimeMillis()));


        int startH = 9;
        int startM = 00;
        int endM = 15;

        String[] hm = currentTime.split("-");

        int curH = Integer.valueOf(hm[0]);
        int curM = Integer.valueOf(hm[1]);
        if (curH == startH) {
            if (curM >= startM && curM < endM) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合竞价阶段
     *
     * @return
     */
    public static boolean isInTotalTradePlate() {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        String currentTime = format
                .format(new Date(System.currentTimeMillis()));


        int startH = 9;
        int startM = 00;
        int endM = 25;

        String[] hm = currentTime.split("-");

        int curH = Integer.valueOf(hm[0]);
        int curM = Integer.valueOf(hm[1]);
        if (curH == startH) {
            if (curM >= startM && curM < endM) {
                return true;
            }
        }
        return false;
    }

    public static String formatMin(String date, String time) {
        String time1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);

            Date currentDate = sdf.parse(date);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentDate);
            int year1 = cal1.get(Calendar.YEAR);
            int month1 = cal1.get(Calendar.MONTH) + 1;
            int date1 = cal1.get(Calendar.DAY_OF_MONTH);
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);


            if (time != null && time.length() == 6) {
                time1 = time.substring(0, 2) + ":" + time.substring(2, 4);

                if(year != year){
                    return year1 + "-" + month1 + "-" + date1 + "  " + time1;
                }

                if(day != day1){
                    return month1 + "-" + date1 + "  " + time1;
                }

                return time1;
            }
        } catch (Exception e) {
            return date;
        }

        return date;
    }
}
