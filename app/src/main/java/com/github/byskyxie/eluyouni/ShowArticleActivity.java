package com.github.byskyxie.eluyouni;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ShowArticleActivity extends BaseActivity {

    private ArticleDoctor ad;   //二者中一个为传入内容
    private ArticlePatient ap;
    private Doctor doctor;
    private Patient patient;

    private TextView name;
    private TextView time;
    private TextView title;
    private TextView content;
    private TextView tag;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        Toolbar toolbar = findViewById(R.id.toolbar_show_art);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.text_view_show_art_name);
        time = findViewById(R.id.text_view_show_art_time);
        title = findViewById(R.id.text_view_show_art_title);
        content = findViewById(R.id.text_view_show_art_content);
        tag = findViewById(R.id.text_view_show_art_tag);
        icon = findViewById(R.id.image_view_show_art_icon);
        initialIntent(getIntent());

    }

    private void initialIntent(@NonNull Intent intent){
        String title = intent.getStringExtra("TITLE");
        Object obj = intent.getSerializableExtra("ARTICLE");
        getSupportActionBar().setTitle(title);
        if(obj instanceof ArticlePatient) {
            ap = (ArticlePatient)obj;
            patient = readPatientBaseInfo(ap.getPid());
            bindAPDate();
        }else {
            ad = (ArticleDoctor)obj;
            doctor = readDoctorBaseInfo(ad.getDid());
            bindADDate();
        }
    }

    private void bindAPDate(){
        if(ap == null)
            return;
        name.setText( patient.getPname() );
        time.setText( ap.getTime() );
        title.setText( ap.getTitle() );
        content.setText( ap.getContent() );
        tag.setText( patient.getGradeName() );
        //默认头像
        if(patient.getPsex() == 2)
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_patient_female));
        else
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_patient_male));
        if(patient.getPicon() != null && !patient.getPicon().isEmpty() ){
            //查看picon有没有下载
            if(! (new File(getFilesDir().getAbsolutePath()+"/icon/picon/"+patient.getPicon()).exists()) ){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadPicon(patient.getPicon());
                    }
                }).start();
            }
            else
                icon.setImageBitmap(BitmapFactory.decodeFile(this.getFilesDir().getAbsolutePath()+"/icon/picon/"+patient.getPicon()) );
        }
    }

    private void bindADDate(){
        if(ad == null)
            return;
        name.setText( doctor.getDname() );
        time.setText( ad.getTime() );
        title.setText( ad.getTitle() );
        content.setText( ad.getContent() );
        tag.setText( doctor.getGradeName() );
        //默认头像
        if( doctor.getDsex() == 2 )
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_doctor_female));//女默认
        else
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_doctor_male));   //男默认
        if( doctor.getDicon() != null && !doctor.getDicon().isEmpty() ){
            //查看dicon有没有下载
            if( !(new File(getFilesDir().getAbsolutePath()+"/icon/dicon/"+doctor.getDicon()).exists()) ){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDicon(doctor.getDicon());
                    }
                }).start();
            }
            else
                icon.setImageBitmap(BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/icon/dicon/"+doctor.getDicon()) );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();//进出方法
            overridePendingTransition(R.anim.activity_still, R.anim.activity_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
