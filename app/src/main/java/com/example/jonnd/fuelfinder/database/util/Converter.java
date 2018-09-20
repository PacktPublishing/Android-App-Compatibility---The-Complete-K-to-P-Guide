package com.example.jonnd.fuelfinder.database.util;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.TypeConverter;

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
}
