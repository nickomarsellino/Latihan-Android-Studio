package com.example.nickomarsellino.scheduling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Home_Page extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScheduleDBHelper dbHelper;
    private ScheduleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        //Untuk Nampilin Data dari recycle view nya.
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dbHelper = new ScheduleDBHelper(this);
        adapter = new ScheduleAdapter(dbHelper.schedulesList(), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }
}
