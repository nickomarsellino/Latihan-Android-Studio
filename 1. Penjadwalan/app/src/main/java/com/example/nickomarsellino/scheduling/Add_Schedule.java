package com.example.nickomarsellino.scheduling;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Add_Schedule extends AppCompatActivity {


    //Inisialisasi Atribut input

    EditText titleData, contentData;
    Button saveButton;
    /////////////////////////////////////////////////////

    //Untuk Database nya
    private ScheduleDBHelper dbHelper;
    ////////////////////////////

    //Untuk Munculin Calendar
    TextView text_Calendar;
    Calendar mCurrentDate;
    int day, month, year;
    String dateData;
    /////////////////////////////////////////////////////

    //Untuk Notifikasi
    public ScheduleClient scheduleClient;
///////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__schedule);


        //Inisialisasi Atribut input
        titleData = (EditText) findViewById(R.id.input_title);
        contentData = (EditText) findViewById(R.id.input_content);
        saveButton = (Button) findViewById(R.id.button_save);
        ///////////////////////////////////////////////////////////


        //Untuk Calender
        text_Calendar = (TextView) findViewById(R.id.input_reminder);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;

//        text_Calendar.setText("Set Your Reminder");

        text_Calendar.setText("Reminder For: "+day+"-"+month+"-"+year);
        ////////////////////////////////////////////////////////////////////////////////





        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSchedule();
            }
        });

    }



    //Untuk Save Data Schedule
        private void saveSchedule(){
            String title = titleData.getText().toString().trim();
            String content = contentData.getText().toString().trim();
            String date = text_Calendar.getText().toString().trim();

            dbHelper = new ScheduleDBHelper(this);

            if(title.isEmpty() && content.isEmpty()){
                Toast.makeText(this, "You must enter the data", Toast.LENGTH_SHORT).show();
            }
            else{
                Schedule schedule = new Schedule(title, content, date);
                dbHelper.saveNewSchedule(schedule);
                Toast.makeText(this, "Input Sukses", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Add_Schedule.this, Home_Page.class));
            }
        }

    /////////////////////////////////////////




    //Untuk Calender
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Reminder(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Schedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;

                text_Calendar.setText("Schedule: "+day+"-"+month+"-"+year);
//                dateData = text_Calendar.getText().toString();

                //Calendar calendar = new Calendar.Builder().setDate(year, month, day).build();
            }
        },year,month,day);
        datePickerDialog.show();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
}
