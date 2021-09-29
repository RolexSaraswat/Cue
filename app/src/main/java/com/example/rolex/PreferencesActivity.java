package com.example.rolex;



import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.common.base.Optional;

import org.joda.time.LocalDate;

import com.example.rolex.period_days.PeriodDaysManager;
import com.example.rolex.predictor.PeriodCalculator;
import com.example.rolex.util.AppPreferences;
import com.example.rolex.util.OptionalUtil;
import com.example.rolex.validator.IntegerWithinRange;

public class PreferencesActivity extends AppCompatActivity implements DatePickerFragment.OnDateSetListener {

    private final PeriodRecalculateReceiver recalculateReceiver = new PeriodRecalculateReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        EditText menstruationLength = (EditText) findViewById(R.id.menstruation_length_value);
        EditText periodLength = (EditText) findViewById(R.id.period_length_value);
        EditText lastPeriodDate = (EditText) findViewById(R.id.last_period_date_value);
        SwitchCompat incomingPeriodNotification  = (SwitchCompat) findViewById(R.id.incoming_period_notification);
        SwitchCompat fertileDaysNotification = (SwitchCompat) findViewById(R.id.fertile_days_notification);
        SwitchCompat ovulationNotification = (SwitchCompat) findViewById(R.id.ovulation_notification);

        menstruationLength.addTextChangedListener(new IntegerWithinRange(menstruationLength, 1, 12));
        periodLength.addTextChangedListener(new IntegerWithinRange(periodLength, 18, 60));

        setFieldValue(menstruationLength, AppPreferences.MENSTRUATION_LENGTH_KEY,
                String.valueOf(AppPreferences.DEFAULT_MENSTRUATION_LENGTH));
        setFieldValue(periodLength, AppPreferences.PERIOD_LENGTH_KEY,
                String.valueOf(AppPreferences.DEFAULT_PERIOD_LENGTH));
        setFieldValue(lastPeriodDate, AppPreferences.LAST_PERIOD_DATE_KEY, AppPreferences.defaultLastPeriodDate());
        setFieldValue(incomingPeriodNotification, AppPreferences.INCOMING_PERIOD_NOTIFICATION_KEY);
        setFieldValue(fertileDaysNotification, AppPreferences.FERTILE_DAYS_NOTIFICATION_KEY);
        setFieldValue(ovulationNotification, AppPreferences.OVULATION_NOTIFICATION_KEY);

    }

    private void setFieldValue(EditText editText, String sharedPreferencesKey, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppPreferences.SHARED_PREFERENCES_FILE,
                MODE_PRIVATE);
        String value = sharedPreferences.getString(sharedPreferencesKey, defaultValue);
        editText.setText(value);
    }

    private void setFieldValue(SwitchCompat switchView, String sharedPreferencesKey) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppPreferences.SHARED_PREFERENCES_FILE,
                MODE_PRIVATE);
        switchView.setChecked(sharedPreferences.getBoolean(sharedPreferencesKey, false));
    }

    public void menstruationLengthPlusOne(View view) {
        changeNumericalFieldValueByDiff(R.id.menstruation_length_value, 1);
    }

    public void menstruationLengthMinusOne(View view) {
        changeNumericalFieldValueByDiff(R.id.menstruation_length_value, -1);
    }

    public void periodLengthPlusOne(View view) {
        changeNumericalFieldValueByDiff(R.id.period_length_value, 1);
    }

    public void periodLengthMinusOne(View view) {
        changeNumericalFieldValueByDiff(R.id.period_length_value, -1);
    }

    public void showDatePickerDialog(View view) {
        EditText lastPeriodDayValue = (EditText) findViewById(R.id.last_period_date_value);
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setInitiallySelectedDate(AppPreferences.convertStringToDate(lastPeriodDayValue.getText().toString(),
                new LocalDate()));
        newFragment.show(getFragmentManager(), AppPreferences.DATE_PICKER_DIALOG_TAG);

    }

    @Override
    public void onDateSet(LocalDate selectedDate) {
        EditText lastPeriodValue = (EditText) findViewById(R.id.last_period_date_value);
        lastPeriodValue.setText(AppPreferences.convertDateToString(selectedDate));
    }

    private void changeNumericalFieldValueByDiff(int fieldId, int diff) {
        EditText textView = (EditText) findViewById(fieldId);
        Optional<Integer> optionalFieldValue = OptionalUtil.parseInteger(textView.getText().toString());
        if (optionalFieldValue.isPresent()) {
            textView.setText(String.valueOf(optionalFieldValue.get() + diff));
        }
    }

    public void updatePreferences(View view) {
        SharedPreferences preferences = getSharedPreferences(AppPreferences.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        boolean userPreferencesAvailable = preferences
                .getBoolean(AppPreferences.BASIC_USER_PREFERENCES_AVAILABLE, false);
        SharedPreferences.Editor editor = preferences.edit();
        savePreferenceStringValue(R.id.menstruation_length_value, AppPreferences.MENSTRUATION_LENGTH_KEY, editor);
        savePreferenceStringValue(R.id.period_length_value, AppPreferences.PERIOD_LENGTH_KEY, editor);
        savePreferenceStringValue(R.id.last_period_date_value, AppPreferences.LAST_PERIOD_DATE_KEY, editor);
        savePreferenceBooleanValue(R.id.incoming_period_notification,
                AppPreferences.INCOMING_PERIOD_NOTIFICATION_KEY, editor);
        savePreferenceBooleanValue(R.id.fertile_days_notification,
                AppPreferences.FERTILE_DAYS_NOTIFICATION_KEY, editor);
        savePreferenceBooleanValue(R.id.ovulation_notification,
                AppPreferences.OVULATION_NOTIFICATION_KEY, editor);

        editor.putBoolean(AppPreferences.BASIC_USER_PREFERENCES_AVAILABLE, true);
        editor.apply();

        if (!userPreferencesAvailable) {
            recalculateReceiver.setPredictionService(this);
        }

        new PeriodCalculator(new PeriodDaysManager(this)).calculate();
        finish();
    }

    public void savePreferenceStringValue(int viewId, String preferenceKey, SharedPreferences.Editor editor) {
        EditText view = (EditText) findViewById(viewId);
        editor.putString(preferenceKey, view.getText().toString());
    }

    public void savePreferenceBooleanValue(int viewId, String preferenceKey, SharedPreferences.Editor editor) {
        SwitchCompat s = (SwitchCompat) findViewById(viewId);
        editor.putBoolean(preferenceKey, s.isChecked());
    }

    public void clearPeriodCalendar(View view) {
        new PeriodDaysManager(this).clearPeriodDates();
    }
}
