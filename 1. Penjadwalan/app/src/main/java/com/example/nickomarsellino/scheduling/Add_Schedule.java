package com.example.nickomarsellino.scheduling;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Add_Schedule extends AppCompatActivity {

    //untuk binding jadi
    @BindView(R.id.container_gallery)
    LinearLayout mContainerGallery;



    //Inisialisasi Atribut input
    EditText titleData, contentData;
    Button saveButton;
    FloatingActionButton loadImage;
    /////////////////////////////////////////////////////

    private final static int REQ_PERMISSION = 1;
    String realPath;
    private Context mContext;

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

    //String Builder Supay bisa nyimpan path gambar supaya banyak
    StringBuilder sbPicture;
    //////////////////////////////////////////////////////


    //Supaya bisa simpen gambar lebih dari 1
    private List<String> imgs = new ArrayList<String>();
    //


    //Create Button Delete lebih dari satu
    private List<Button> delete_imgs = new ArrayList<Button>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__schedule);
        ButterKnife.bind(this);





        //Inisialisasi Atribut input
        titleData = findViewById(R.id.input_title);
        contentData = findViewById(R.id.input_content);
        saveButton = (Button) findViewById(R.id.button_save);
        loadImage = (FloatingActionButton) findViewById(R.id.fab_create_image);
        ///////////////////////////////////////////////////////////

        sbPicture = new StringBuilder();

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

            setImageViews(realPath);
        }
    }

    private void setImageViews(final String realPath) {
        final Uri uriFromPath = Uri.fromFile(new File(realPath));

//        imageView.setImageURI(uriFromPath);


        //supaya dia bisa generate lebih dari 1 gambar
        final ImageView ivPicture = new ImageView(this);



        final Button buttonImg = new Button(this);
        buttonImg.setText("Delete Image");

        final LinearLayout ContainerContent = new LinearLayout(this);
        ContainerContent.setVerticalGravity(1);



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 400);
        LinearLayout.LayoutParams cancel = new LinearLayout.LayoutParams(600, 130);


        ivPicture.setLayoutParams(params);
        ivPicture.setImageURI(uriFromPath);
        buttonImg.setLayoutParams(cancel);


        ContainerContent.addView(buttonImg);
        ContainerContent.addView(ivPicture);

        mContainerGallery.addView(ContainerContent);


        //Pada saat "Delete Image" di tekan
        buttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Iterator<String> iterator = imgs.iterator();
                while(iterator.hasNext())
                {
                    String value = iterator.next();
                    if (realPath.equals(value))
                    {
                        ContainerContent.removeAllViews();
                        iterator.remove();
                        break;
                    }
                }
            }

        });


        //Nyimpen Path Gambarnya ke image databasenya
        imgs.add(realPath);
        delete_imgs.add(buttonImg);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

                Schedule schedule = new Schedule(title, content, date, imgs);

                long idSchedule = dbHelper.saveNewSchedule(schedule);

                //foreach untuk nyimpen datanya sesuai banyak yang dimasukin
                    for(String img:schedule.getImages()){

                        ScheduleImage scheduleImage = new ScheduleImage();
                        scheduleImage.setIdSchedule(idSchedule);
                        scheduleImage.setImage(img);

                        dbHelper.saveNewScheduleImage(scheduleImage);
                    }
                //

                Toast.makeText(this, "Input Sukses", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Add_Schedule.this, Home_Page.class));


                NotificationGenerator.openActivityNotification(getApplicationContext(), title, date);


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
