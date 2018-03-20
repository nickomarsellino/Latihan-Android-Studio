package com.example.nickomarsellino.scheduling;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class Add_Schedule extends AppCompatActivity {


    //Inisialisasi Atribut input
    EditText titleData, contentData;
    Button saveButton;
    FloatingActionButton loadImage;
    ImageView imageView;
    /////////////////////////////////////////////////////

    private final static int REQ_PERMISSION = 1;
    String realPath;

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
        loadImage = (FloatingActionButton) findViewById(R.id.fab_create_image);
        imageView = (ImageView) findViewById(R.id.showImageSelected);
        ///////////////////////////////////////////////////////////



        //Jika Tombol Add Image Di Tekan
        loadImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                reqPermission();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);

            }
        });

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



    //Minta Permission Untuk akses ke gallery

    public void reqPermission(){
        int reqEX = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);

        if(reqEX != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == REQ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//        }
//    }

    ///////////////////////////////////////////////////////////////////


    //Untuk Masukin Gambar ke Form

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == Add_Schedule.RESULT_OK && data != null){


                // SDK >= 11 && SDK < 19
            if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            setImageViews(data.getData().getPath(),realPath);
        }
    }

    private void setImageViews(String uriPath,String realPath) {


        Uri uriFromPath = Uri.fromFile(new File(realPath));

        imageView.setImageURI(uriFromPath);


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Untuk Save Data Schedule
        private void saveSchedule(){
            String title = titleData.getText().toString().trim();
            String content = contentData.getText().toString().trim();
            String date = text_Calendar.getText().toString().trim();
            String image = realPath;

            dbHelper = new ScheduleDBHelper(this);

            if(title.isEmpty() && content.isEmpty()){
                Toast.makeText(this, "You must enter the data", Toast.LENGTH_SHORT).show();
            }
            else{
                Schedule schedule = new Schedule(title, content, date, image);
                dbHelper.saveNewSchedule(schedule);
                Toast.makeText(this, "Input Sukses", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Add_Schedule.this, Home_Page.class));
            }
        }

    /////////////////////////////////////////////////////////////////////////////////////////////




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
