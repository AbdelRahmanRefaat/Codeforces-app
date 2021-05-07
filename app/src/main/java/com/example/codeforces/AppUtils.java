package com.example.codeforces;

import android.graphics.Color;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public final class AppUtils {

    public static final int CODE_USERS_FRAGMENT = 1;
    public static final int CODE_CONTESTS_FRAGMENT = 2;
    public static final int CODE_PROBLEMS_FRAGMENT = 3;
    public static final int getRatingColor(int rating){

        int retColor = Color.parseColor("#000000");

        if(rating < 1200 && rating > 0){
            retColor = Color.parseColor("#808080");

        }else if(rating >= 1200 && rating < 1400){
            retColor = Color.parseColor("#008000");


        }else if(rating >= 1400 && rating < 1600){
            retColor = Color.parseColor("#03A89E");


        }else if(rating >= 1600 && rating < 1900){
            retColor = Color.parseColor("#0000FF");

        }else if(rating >= 1900 && rating < 2100){
            retColor = Color.parseColor("#AA00AA");


        }else if(rating >= 2100 && rating < 2300){
            retColor = Color.parseColor("#FF8C00");


        }else if(rating >= 2300 && rating < 2400){
            retColor = Color.parseColor("#FF8C00");


        }else if(rating >= 2400 && rating < 2600){
            retColor = Color.parseColor("#FF0000");

        }else if(rating >= 2600 && rating < 3000){
            retColor = Color.parseColor("#FF0000");


        }else if(rating >= 3000){
            retColor = Color.parseColor("#FF0000");


        }else{ // <= 0
            retColor = Color.parseColor("#000000");
        }

        return retColor;
    }
    public static final String unixToDate(long time) {
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(new java.util.Date(time * 1000));
        return date;
    }
    public static final String unixToDateHM(long time) {
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy - hh:mm aa")
                .format(new java.util.Date(time * 1000));
        return date;
    }

    public static final String secondToHours(long time) {

        String hours = "", minutes = "";
        hours = hours + String.valueOf(time / (60 * 60));
        time /= (60 * 60);
        minutes = minutes + String.valueOf(time / 60);

        if (minutes.length() == 1)
            minutes = "0" + minutes;

        return hours + ":" + minutes;
    }

    public static final List<Integer> createColors() {
        List<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        return colors;
    }

}
