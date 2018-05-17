package com.github.byskyxie.eluyouni;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ChatActivity extends AppCompatActivity {

    public static final int TARGET_TYPE_DOCTOR = 0X0010;
    public static final int TARGET_TYPE_PATIENT = 0X0100;

    private int targetType;
    private Doctor doctor;
    private Patient patient;
    private RecyclerView recyclerChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        findViewById(R.id.button_chat_send).setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrayBlue));
        //
        getInitialInfo(getIntent());
        //
        recyclerChat = findViewById(R.id.recycler_chat_show);

    }

    private void getInitialInfo(Intent intent){
        if(intent == null)
            return;
        if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_DOCTOR){
            targetType = TARGET_TYPE_DOCTOR;
            doctor = (Doctor) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(doctor.getDname());
            getSupportActionBar().setSubtitle("医生");
        }else if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_PATIENT){
            targetType = TARGET_TYPE_PATIENT;
            patient = (Patient) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(patient.getPname());
            getSupportActionBar().setSubtitle("患者");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
