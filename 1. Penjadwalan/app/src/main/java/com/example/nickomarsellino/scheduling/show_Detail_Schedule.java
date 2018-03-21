package com.example.nickomarsellino.scheduling;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class show_Detail_Schedule extends AppCompatActivity {

    //Inisialisasi Atribut
    private TextView title,content,date;
    private ImageView image;

    //Inisialisasi database
    private ScheduleDBHelper dbHelper;
    private long receivedScheduleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__detail__schedule);


        //Inisialisasi
        title = (TextView) findViewById(R.id.detail_Title);
        content = (TextView) findViewById(R.id.detail_Content);
        date = (TextView) findViewById(R.id.detail_Date);
        image = (ImageView) findViewById(R.id.detail_Image);


        dbHelper = new ScheduleDBHelper(this);

        try{
            receivedScheduleId = getIntent().getLongExtra("USER_ID", 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Get Data Dari Id nya
        Schedule schedule = dbHelper.getSchedule(receivedScheduleId);

        //nampilin Datanya
        title.setText(schedule.getTitle());
        content.setText(schedule.getContent());
        date.setText(schedule.getDate());

//
//        Uri uriFromPath = Uri.fromFile(new File(schedule.getImage()));
//
//        image.setImageURI(uriFromPath);

    }
}