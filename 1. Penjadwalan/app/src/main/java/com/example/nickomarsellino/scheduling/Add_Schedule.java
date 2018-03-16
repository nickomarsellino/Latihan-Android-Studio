package com.example.nickomarsellino.scheduling;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Add_Schedule extends AppCompatActivity {



    //Untuk Munculin Calendar
    TextView text_Calendar;
    Calendar mCurrentDate;
    int day, month, year;

    //Untuk Notifikasi
    public ScheduleClient scheduleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__schedule);

        //Untuk Calender
        text_Calendar = (TextView) findViewById(R.id.input_reminder);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;

        text_Calendar.setText("Set Your Reminder");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Reminder(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Schedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;

                text_Calendar.setText("Day: "+day+"\n"+"Month: "+month+"\n"+"Year: "+year);
            }
        },year,month,day);
        datePickerDialog.show();
    }
}
