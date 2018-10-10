package com.example.jonnd.fuelfinder.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR);
        int minute = instance.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (getActivity() instanceof TimePickerDialog.OnTimeSetListener) {
            ((TimePickerDialog.OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay, minute);
        }

    }
}
