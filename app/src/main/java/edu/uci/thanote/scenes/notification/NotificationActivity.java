package edu.uci.thanote.scenes.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.*;
import androidx.fragment.app.DialogFragment;
import edu.uci.thanote.R;
import edu.uci.thanote.scenes.main.fragments.setting.ImageTextItem;
import edu.uci.thanote.scenes.general.BaseActivity;

import java.util.Calendar;

public class NotificationActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    private static final int ALARM_REQUEST_CODE = 123;
    private TextView timeTextView;
    private NotificationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // hide action bar
        getSupportActionBar().hide();
        viewModel = new NotificationViewModel(getApplicationContext());
        setupViews();
    }

    private void setupViews() {
        Spinner apiSpinner = findViewById(R.id.spinner_apis);
        ApiSpinnerAdapter adapter = new ApiSpinnerAdapter(this, viewModel.getApis());
        apiSpinner.setAdapter(adapter);
        apiSpinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        apiSpinner.setSelection(viewModel.getDefaultCategorySpinnerPosition());

        Switch alarmSwitch = findViewById(R.id.switch_alarm);
        alarmSwitch.setOnCheckedChangeListener(this::onCheckedChanged);
        alarmSwitch.setChecked(viewModel.getDefaultIsAlarmAllowed());

        timeTextView = findViewById(R.id.text_view_time);
        timeTextView.setText(viewModel.getAlarmTime());
        timeTextView.setOnClickListener(viewOnClickListener);

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(viewOnClickListener);

        Button closeButton = findViewById(R.id.button_close);
        closeButton.setOnClickListener(viewOnClickListener);
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        viewModel.setAlarmAllowed(isChecked);
        if (isChecked) {
            startAlarm(viewModel.getHour(), viewModel.getMinute());
        } else {
            cancelAlarm();
        }
    }

    private AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ImageTextItem api = (ImageTextItem) parent.getItemAtPosition(position);
            viewModel.setCategory(api.getText());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_view_time:
                    openTimePickerDialog();
                    break;
                case R.id.button_save:
                    viewModel.save();
                    finish();
                    break;
                case R.id.button_close:
                    finish();
                    break;
                default:
                    showShortToast("Unknown operation for id:" + v.getId());
                    break;
            }
        }
    };

    private void openTimePickerDialog() {
        DialogFragment timePicker = new TimePickerFragment(
                viewModel.getHour(),
                viewModel.getMinute(),
                viewModel.is24HourFormat()
        );
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        viewModel.updateTime(hourOfDay, minute);
        timeTextView.setText(viewModel.getAlarmTime());
    }

    private void startAlarm(int hour, int minute) {
        Calendar calendar = viewModel.getCalendar(hour, minute);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            showShortToast("Add 1 day!");
        } else {
            showShortToast("No need to adjust day!");
        }

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
