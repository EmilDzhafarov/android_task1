package ua.nure.dzhafarov.task1.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ua.nure.dzhafarov.task1.R;


public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "ua.nure.dzhafarov.date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(LocalDate date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle saved) {
        LocalDate date = (LocalDate) getArguments().getSerializable(ARG_DATE);
        
        if (date == null) {
            date = LocalDate.now();
        }
        
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_layout, null);
        
        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        mDatePicker.init(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.select_birthday_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth() + 1;
                        int day = mDatePicker.getDayOfMonth();
                        
                        sendResult(Activity.RESULT_OK, LocalDate.of(year, month, day));
                    }
                })
                .create();
    }
    

    private void sendResult(int resultCode, LocalDate date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
