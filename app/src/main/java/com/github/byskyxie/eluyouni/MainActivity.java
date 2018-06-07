package com.github.byskyxie.eluyouni;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,IndexFragment.OnFragmentInteractionListener ,ConsultFragment.OnFragmentInteractionListener
                    ,MedicineFragment.OnFragmentInteractionListener, PriDocFragment.OnFragmentInteractionListener
                    ,CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private static final int CHECKED_NONE = 0;
    private static final int CHECKED_INDEX = 1;
    private static final int CHECKED_CONSULT = 2;
    private static final int CHECKED_MEDICINE = 3;
    private static final int CHECKED_DOCTOR = 4;

    private static final int ACCEPT_ICON = 20;

    private int checkedOpt = CHECKED_NONE;

    private NavigationView navView;
    private View navHeader;
    private RadioButton radioButtonIndex;
    private RadioButton radioButtonConsult;
    private RadioButton radioButtonMedic;
    private RadioButton radioButtonDoc;

    private Dialog dialogExit;
    private Dialog dialogEmerg;
    private IndexFragment indexFragment;
    private ConsultFragment consultFragment;
    private MedicineFragment medicineFragment;
    private PriDocFragment priDocFragment;
    private MainHandler handler = new MainHandler(this);

    class MainHandler extends Handler{
        private MainActivity activity;

        public MainHandler(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ACCEPT_ICON:
                    if(msg.arg1 == 1)
                        ((ImageView)activity.navHeader.findViewById(R.id.image_view_icon)).setImageBitmap( BitmapFactory.decodeFile(
                                getFilesDir().getAbsolutePath()+"/icon/picon/"+userInfo.getPicon() ) );
                    else
                        ((ImageView)activity.navHeader.findViewById(R.id.image_view_icon)).setImageDrawable(ContextCompat
                                .getDrawable(activity,R.mipmap.patient)  );
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLogin())
            readUserInfo();
        if(chatRecordList == null){
            getChatRecord();
        }
        //进入主页

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_menu_index);
        setSupportActionBar(toolbar);
        setDefaultFragment();
        //设置点击事件
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navHeader = navView.inflateHeaderView(R.layout.nav_header_main);
        setNavHeaderData(navHeader);
        navHeader.findViewById(R.id.heart_beat_view).setOnClickListener(this);

        radioButtonIndex = findViewById(R.id.radio_button_index);
        radioButtonConsult = findViewById(R.id.radio_button_consult);
        radioButtonMedic = findViewById(R.id.radio_button_medicine);
        radioButtonDoc = findViewById(R.id.radio_button_private_doc);

        radioButtonIndex.setChecked(true);  //默认选中
        radioButtonIndex.setOnCheckedChangeListener(this);
        radioButtonConsult.setOnCheckedChangeListener(this);
        radioButtonMedic.setOnCheckedChangeListener(this);
        radioButtonDoc.setOnCheckedChangeListener(this);


        //TODO:setDrawableTopSize(radioButtonMedic,92);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected void setNavHeaderData(View header){
        //用户头像
        if(userInfo.getPicon()==null || userInfo.getPicon().equalsIgnoreCase("null"))
            ((ImageView)header.findViewById(R.id.image_view_icon)).setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.patient)  );
        else{
            //查看picon有没有下载
            if( new File(this. getFilesDir().getAbsolutePath()+"/icon/picon/"+userInfo.getPicon()).exists() ){
                ((ImageView)header.findViewById(R.id.image_view_icon)).setImageBitmap( BitmapFactory.decodeFile(
                        getFilesDir().getAbsolutePath()+"/icon/picon/"+userInfo.getPicon() ) );
            }else{//下载并notify
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(downloadPicon(userInfo.getPicon())){
                            Message msg = new Message();
                            msg.what = ACCEPT_ICON;
                            msg.arg1 = 1;
                            handler.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = ACCEPT_ICON;
                            msg.arg1 = 0;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        }
        //pname
        ((TextView)header.findViewById(R.id.text_view_nav_name)).setText(userInfo.getPname());
        //e币
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N )
            ((TextView)header.findViewById(R.id.text_view_e_coin)).setText(Html.fromHtml("<font color=#f00>"
                    +userInfo.getEcoin()+"</font>"+"e币", Html.FROM_HTML_MODE_COMPACT) );
        else
            ((TextView)header.findViewById(R.id.text_view_e_coin)).setText(Html.fromHtml("<font color=#FFA500>"+userInfo.getEcoin()+"</font>"+" e币") );
        //等级
        ((TextView)header.findViewById(R.id.text_view_score)).setText(userInfo.getGradeName());
        //血压
        ((TextView)header.findViewById(R.id.text_view_blood_press)).setText("血压：\n101\nKpa");
        //血脂
        ((TextView)header.findViewById(R.id.text_view_blood_fat)).setText("血脂：\n3.4\nmmol/L");
        //血糖
        ((TextView)header.findViewById(R.id.text_view_blood_sugar)).setText("血糖：\n5.0\nmmol/L");
        //睡眠
        ((TextView)header.findViewById(R.id.text_view_sleep)).setText("睡眠：\n7小时");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
//        tran.remove(indexFragment);
//        tran.remove(consultFragment);
//        tran.remove(medicineFragment);
//        tran.remove(priDocFragment);
    }

    /**
     * set size of top drawer
     * */
    private void setDrawableTopSize(TextView view, int size){
        Drawable[] ds =  view.getCompoundDrawables();
        ds[1].setBounds(0,0,size,size);
        view.setCompoundDrawables(null,ds[1],null,null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
//            super.onBackPressed();
        }
    }

    protected void showExitDialog(){
        if(dialogExit != null){
            dialogExit.show();
            return;
        }
        dialogExit = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog);
        dialogExit.setContentView(R.layout.dialog_exit);
        ((TextView)dialogExit.findViewById(R.id.text_view_exit_hint)).setText(getString(R.string.exit_hint));
        dialogExit.findViewById(R.id.button_exit_cancel).setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        dialogExit.findViewById(R.id.button_exit_confirm).setBackgroundColor(ContextCompat.getColor(this,R.color.colorGrayBlue));
        dialogExit.findViewById(R.id.button_exit_cancel).setOnClickListener(this);
        dialogExit.findViewById(R.id.button_exit_confirm).setOnClickListener(this);
        //设置位置
        Window window = dialogExit.getWindow();
        if(window != null)
            window.setGravity(Gravity.CENTER);
        dialogExit.show();
    }

    protected void showEmergeDialog(){
        if(dialogEmerg != null){
            dialogEmerg.show();
            return;
        }
        dialogEmerg = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog);
        dialogEmerg.setContentView(R.layout.dialog_emerg);
        dialogEmerg.findViewById(R.id.button_emerg_cancel).setOnClickListener(this);
        //position
        Window window = dialogEmerg.getWindow();
        if(window != null)
            window.setGravity(Gravity.CENTER);
        dialogEmerg.show();
    }

    /**
     * function:Top main menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * function: 小菜单点击事件
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_emergency) {
            showEmergeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_menu_my_keep:
                //我的收藏
                intent = new Intent(this, MyKeepActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_visited:
                //浏览记录
                intent = new Intent(this, VisitedActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_my_article:
                //我的文章
                intent = new Intent(this, MyArticleActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_my_health_document:
                //健康文档
                intent = new Intent(this, HealthFileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_my_consult_record:
                //会诊记录
                intent = new Intent(this, ConsultRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_my_health_manage:
                //健康管理
                intent = new Intent(this, HealthManageActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_my_relate_account:
                //亲情账号
                intent = new Intent(this, RelativeActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO:多个fragment 的交流实现
        Log.e("MainActivity","has changed Fragment.");
    }

    private void setDefaultFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        indexFragment = new IndexFragment();
        transaction.replace(R.id.fragment_show, indexFragment);
        transaction.commit();
        checkedOpt = CHECKED_INDEX;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ChatActivity.CHAT_ACTIVITY_CODE ){
            if(resultCode != RESULT_OK && data == null){
                Log.e("MainActivity","data is null");
                return;
            }
            int pos = data.getIntExtra("ClickedPos",-1);
            //更新信息
            if(pos!=-1)
                priDocFragment.recyclerPri.getAdapter().notifyItemChanged(pos,"null");
        }else if(requestCode == ConsultDataAdapter.CONSULT_DATA_ADAPTER){
            //获得图片URL
            if(resultCode != RESULT_OK || data == null || data.getData()==null)
                return;
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if(cursor == null)
                return;
            cursor.moveToFirst();
            consultFragment.addDataItem( cursor.getString( cursor.getColumnIndex(MediaStore.Images.Media.DATA) ) );
            cursor.close();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        菜单选中,切换fragment
        //        先checked 后 清除pre checked
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (buttonView.getId()){
            case R.id.radio_button_index:
                if(isChecked && checkedOpt != CHECKED_INDEX){
                    getSupportActionBar().setTitle(R.string.main_menu_index);
                    checkedOpt = CHECKED_INDEX;
                    if(indexFragment == null){
                        indexFragment = new IndexFragment();
                        transaction.add(R.id.fragment_show, indexFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(indexFragment);
                    transaction.commit();
                }
                break;
            case R.id.radio_button_consult:
                if(isChecked && checkedOpt != CHECKED_CONSULT){
                    getSupportActionBar().setTitle(R.string.main_menu_consult);
                    checkedOpt = CHECKED_CONSULT;
                    if(consultFragment == null){
                        consultFragment = new ConsultFragment();
                        transaction.add(R.id.fragment_show, consultFragment);
                    }
                    consultFragment.notifyDoctorList();
                    hideFragment(transaction);
                    transaction.show(consultFragment);
                    transaction.commit();
                }
                break;
            case R.id.radio_button_medicine:
                if(isChecked && checkedOpt != CHECKED_MEDICINE){
                    getSupportActionBar().setTitle(R.string.main_menu_medicine);
                    checkedOpt = CHECKED_MEDICINE;
                    if(medicineFragment == null){
                        medicineFragment = new MedicineFragment();
                        transaction.add(R.id.fragment_show, medicineFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(medicineFragment);
                    transaction.commit();
                }
                break;
            case R.id.radio_button_private_doc:
                if(isChecked && checkedOpt != CHECKED_DOCTOR){
                    getSupportActionBar().setTitle(R.string.main_menu_private_doc);
                    checkedOpt = CHECKED_DOCTOR;
                    if(priDocFragment == null){
                        priDocFragment = new PriDocFragment();
                        transaction.add(R.id.fragment_show, priDocFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(priDocFragment);
                    transaction.commit();
                }
                break;
        }
    }

    private void hideFragment(FragmentTransaction transaction){
        if(indexFragment != null){
            transaction.hide(indexFragment);
        }
        if(consultFragment != null){
            transaction.hide(consultFragment);
        }
        if(medicineFragment != null){
            transaction.hide(medicineFragment);
        }
        if(priDocFragment != null){
            transaction.hide(priDocFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.heart_beat_view:
                ((HeartBeatView)v).startAnim();
                break;
            case R.id.button_exit_cancel:
                dialogExit.dismiss();
                break;
            case R.id.button_exit_confirm:
                finish();
                System.exit(0);
                break;
            case R.id.button_emerg_cancel:
                dialogEmerg.dismiss();
                break;
        }
    }

    public void setRadioButtonChecked(int position){
        switch (position){
            case 0:
                if(!radioButtonIndex.isChecked())
                    radioButtonIndex.setChecked(true);
                break;
            case 1:
                if(!radioButtonConsult.isChecked())
                    radioButtonConsult.setChecked(true);
                break;
            case 2:
                if(!radioButtonMedic.isChecked())
                    radioButtonMedic.setChecked(true);
                break;
            case 3:
                if(!radioButtonDoc.isChecked())
                    radioButtonDoc.setChecked(true);
                break;
        }
    }
}
