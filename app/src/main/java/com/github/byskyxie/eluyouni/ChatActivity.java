package com.github.byskyxie.eluyouni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class ChatActivity extends BaseActivity
        implements View.OnClickListener{

    public static final int TARGET_TYPE_DOCTOR = 0X0010;
    public static final int TARGET_TYPE_PATIENT = 0X0100;

    private SendWatcher watcher = new SendWatcher();
    private int targetType;
    private Doctor doctorTalker;
    private Patient patientTalker;
    private RecyclerView recyclerChat;
    private EditText edit;
    private Button buttonSend;
    private ChatAdapter adapter = new ChatAdapter(this, null);

    class SendWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("changed ","");
            if(s.length() == 0){
                buttonSend.setEnabled(false);
            }else{
                buttonSend.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonSend = findViewById(R.id.button_chat_send);
        buttonSend.setOnClickListener(this);
        buttonSend.setEnabled(false);
        edit = findViewById(R.id.edit_text_chat_send);
        edit.addTextChangedListener(watcher);
        //
        getInitialInfo(getIntent());
        //
        recyclerChat = findViewById(R.id.recycler_chat_show);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerChat.setAdapter(adapter);
    }

    private void getInitialInfo(Intent intent){
        if(intent == null)
            return;
        if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_DOCTOR){
            targetType = TARGET_TYPE_DOCTOR;
            doctorTalker = (Doctor) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(doctorTalker.getDname());
            getSupportActionBar().setSubtitle("医生");
        }else if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_PATIENT){
            targetType = TARGET_TYPE_PATIENT;
            patientTalker = (Patient) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(patientTalker.getPname());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_chat_send:
                String content = edit.getText().toString();
                edit.getText().clear();
                buttonSend.setEnabled(false);
                if(content.isEmpty()){
                    Log.e("ChatActivity","False state");
                    return;
                }
                ChatItem item = new ChatItem(content, ChatItem.CHAT_TYPE_SELF, new Date(), userInfo.getPicon());
                adapter.addData(item);
                recyclerChat.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
                break;
        }
    }

}
