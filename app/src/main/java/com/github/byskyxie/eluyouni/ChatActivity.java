package com.github.byskyxie.eluyouni;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends BaseActivity
        implements View.OnClickListener{

    public static final int TARGET_TYPE_DOCTOR = 0X0010;
    public static final int TARGET_TYPE_PATIENT = 0X0100;
    public static final int CHAT_ACTIVITY_CODE = 0X1005;

    private SendWatcher watcher = new SendWatcher();
    private int targetType;
    private int clickedPos;
    private Doctor doctorTalker;
    private Patient patientTalker;
    private RecyclerView recyclerChat;
    private EditText edit;
    private Button buttonSend;
    private ChatAdapter adapter;

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
        //顺便确定了adapter数据
        getInitialInfo(getIntent());
        //
        recyclerChat = findViewById(R.id.recycler_chat_show);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerChat.setAdapter(adapter);
        if(adapter.getItemCount() > 0)  //到达最近记录
            recyclerChat.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
    }

    private void getInitialInfo(Intent intent){
        if(intent == null)
            return;
        clickedPos = intent.getIntExtra("ClickedPos",-1);
        if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_DOCTOR){
            targetType = TARGET_TYPE_DOCTOR;
            doctorTalker = (Doctor) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(doctorTalker.getDname());
            getSupportActionBar().setSubtitle("医生");
            if(BaseActivity.mapEridToPosition.containsKey(doctorTalker.getDid())){
                adapter = new ChatAdapter(this, BaseActivity.chatRecordList
                        .get( BaseActivity.mapEridToPosition.get(doctorTalker.getDid()) ).getList());
            }
        }else if(intent.getIntExtra("TargetType",-1) == TARGET_TYPE_PATIENT){
            targetType = TARGET_TYPE_PATIENT;
            patientTalker = (Patient) intent.getSerializableExtra("Target");
            getSupportActionBar().setTitle(patientTalker.getPname());
            getSupportActionBar().setSubtitle("患者");
            if(BaseActivity.mapEridToPosition.containsKey(patientTalker.getPid())){
                adapter = new ChatAdapter(this, BaseActivity.chatRecordList
                        .get( BaseActivity.mapEridToPosition.get(patientTalker.getPid()) ).getList());
            }
        }
        if(adapter == null)
            adapter = new ChatAdapter(this,null);
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
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ClickedPos",clickedPos);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        onStop();
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
                ChatItem item = new ChatItem(content, ChatItem.CHAT_TYPE_SELF, new Date(), userInfo.getPid(), 1);
                adapter.addData(item);
                recyclerChat.getLayoutManager().scrollToPosition(adapter.getItemCount()-1);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter == null || adapter.getItemCount()==0 )
            return;
        //保存聊天记录
        int pos = -1;
        ArrayList<ChatItem> list = adapter.getList();
        if(targetType == TARGET_TYPE_PATIENT ){
            if(!BaseActivity.mapEridToPosition.containsKey(patientTalker.getPid())){
                BaseActivity.mapEridToPosition.put(patientTalker.getPid(), BaseActivity.chatRecordList.size());
                BaseActivity.chatRecordList.add(new ChatRecord(userInfo.getPid(), patientTalker.getPid(), 1,null ) );
            }
            pos = BaseActivity.mapEridToPosition.get(patientTalker.getPid());
        }else if(targetType == TARGET_TYPE_DOCTOR){
            if(!BaseActivity.mapEridToPosition.containsKey(doctorTalker.getDid())){
                BaseActivity.mapEridToPosition.put(doctorTalker.getDid(), BaseActivity.chatRecordList.size());
                BaseActivity.chatRecordList.add(new ChatRecord(userInfo.getPid(), doctorTalker.getDid(), 2,null ));
            }
            pos = BaseActivity.mapEridToPosition.get(doctorTalker.getDid());
        }
        BaseActivity.chatRecordList.get(pos).clearChatItem();
        BaseActivity.chatRecordList.get(pos).addChatItem(list);
        //save data
        ContentValues content = new ContentValues();
        for(ChatRecord cr: BaseActivity.chatRecordList){
            //保存
            long id,erid,ertype;
            id = cr.getPid();
            erid = cr.getErid();
            ertype = cr.getErtype();
            for(ChatItem ci:cr.getList()){
                Cursor cursor = BaseActivity.userDatabaseRead.query("CHAT_RECORD",new String[]{"*"},
                        "ID=? AND ERID=? AND TIME=?", new String[]{id+"",erid+"", ci.getFormatTime()},
                        null, null, null);
                if(cursor.moveToFirst()){
                    cursor.close();
                    continue;   //说明有此记录
                }
                content.clear();
                content.put("ID",id);
                content.put("ERID",erid);
                content.put("ERTYPE",ertype);
                content.put("TIME",ci.getFormatTime());
                content.put("CHATTYPE",ci.getChatType());
                content.put("CONTENT",ci.getContent());
                BaseActivity.userDatabasewrit.insert("CHAT_RECORD",null,content);
            }
        }
        //返回参数
    }

}
