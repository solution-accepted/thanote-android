package edu.uci.thanote.scenes.notification;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private int hours;
    private int minutes;
    private boolean is24HourFormat;

    public TimePickerFragment() {
        this(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
    }

    public TimePickerFragment(int hours, int minutes, boolean is24HourFormat) {
        this.hours = hours;
        this.minutes = minutes;
        this.is24HourFormat = is24HourFormat;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(
                getActivity(),
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hours,
                minutes,
                is24HourFormat
        );
    }
}
