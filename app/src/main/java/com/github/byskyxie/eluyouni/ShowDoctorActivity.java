package com.github.byskyxie.eluyouni;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ShowDoctorActivity extends BaseActivity
        implements View.OnClickListener{

    public static final int SHOW_DOCTOR_ACTIVITY_CODE = 0x0120;

    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor);
        Toolbar toolbar = findViewById(R.id.toolbar_show_doc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getInitialIntent(getIntent());
        initialUI();
    }

    protected void getInitialIntent(Intent intent){
        if(intent == null)
            return;
        this.doctor = (Doctor) intent.getSerializableExtra("DOCTOR");
        if(doctor == null)
            return;
        getSupportActionBar().setTitle(doctor.getDname());
    }

    protected void initialUI(){
        ((TextView)findViewById(R.id.text_view_show_doc_name)).setText(doctor.getDname());
        ((TextView)findViewById(R.id.text_view_show_doc_hospital)).setText(doctor.getDhospital());
        ((TextView)findViewById(R.id.text_view_show_doc_hot)).setText(""+doctor.getDhot_level());
        ((TextView)findViewById(R.id.text_view_show_doc_24h)).setText(""+ (doctor.getD24hreply()*100));
        ((TextView)findViewById(R.id.text_view_show_doc_marking)).setText(""+doctor.getDmarking());
        ((TextView)findViewById(R.id.text_view_show_profess_content)).setText(doctor.getDprofess());
        ((TextView)findViewById(R.id.text_view_show_career_content)).setText(doctor.getDcareer());
        findViewById(R.id.text_view_show_doc_query).setOnClickListener(this);
        findViewById(R.id.text_view_show_doc_focus).setOnClickListener(this);
        findViewById(R.id.text_view_show_doc_recipe).setOnClickListener(this);
        findViewById(R.id.text_view_show_doc_plan).setOnClickListener(this);
        findViewById(R.id.text_view_show_doc_consult).setOnClickListener(this);
        findViewById(R.id.text_view_show_doc_pri).setOnClickListener(this);
        if(isConsultContain(doctor.getDid()))
            findViewById(R.id.text_view_show_doc_consult).setEnabled(false);
        //设置默认头像
        ImageView img = findViewById(R.id.image_view_show_doc_icon);
        if(doctor.getDsex()==2 )
            img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_doctor_female));
        else
            img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_doctor_male));
        //头像
        if(doctor.getDicon()!=null && !doctor.getDicon().isEmpty()){
            String iconPath =  getFilesDir().getAbsolutePath()+"/icon/dicon/"+doctor.getDicon();
            File file = new File(iconPath);
            if(file.exists()){
                img.setImageBitmap( BitmapFactory.decodeFile(iconPath) );
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDicon(doctor);
                    }
                }).start();
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.text_view_show_doc_query:
                //咨询
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra("Target",doctor);
                intent.putExtra("TargetType",ChatActivity.TARGET_TYPE_DOCTOR);
                startActivity(intent);
                break;
            case R.id.text_view_show_doc_focus:
                //关注

                break;
            case R.id.text_view_show_doc_recipe:
                //开处方
                intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.text_view_show_doc_plan:
                //医疗方案
                intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.text_view_show_doc_consult:
                //添加会诊
                if(addConsultDoctor(doctor)){
                    v.setEnabled(false);
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "会诊室一次最多邀请"+BaseActivity.DOC_NUM_LIMIT+"位医生", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_view_show_doc_pri:
                //私人医生,弹窗要钱

                break;
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
