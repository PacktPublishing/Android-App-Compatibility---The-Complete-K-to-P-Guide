package com.example.jonnd.fuelfinder.database.util;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.databinding.BindingConversion;
import android.databinding.InverseMethod;

import com.example.jonnd.fuelfinder.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {

    @TypeConverter
    public Long fromDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return new Date().getTime();
    }
    @TypeConverter
    public Date toDate(Long dateL) {
        if (dateL != null) {
            return new Date(dateL);
        }
        return new Date();
    }

    public static String convertCash(double cashVal) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(cashVal);
    }

    @BindingConversion
    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(date);
    }

    public static String timeDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(date);
    }

    @InverseMethod("convertIndexToFuelString")
    public static int convertFuelStringToIndex(Context context, String val) {
        String[] fuels = context.getResources().getStringArray(R.array.fuels);
        int retVal = 0;
        for (int i = 0; i < fuels.length; i++) {
            if(fuels[i].equals(val)) {
                retVal = i;
            }
        }
        return retVal;
    }

    public static String convertIndexToFuelString(Context context, int fuelType) {
        String[] stringArray = context.getResources().getStringArray(R.array.fuels);
        return fuelType < stringArray.length ? stringArray[fuelType] : stringArray[0];
    }
}
