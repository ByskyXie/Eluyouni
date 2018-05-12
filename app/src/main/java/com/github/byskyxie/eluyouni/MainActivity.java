package com.github.byskyxie.eluyouni;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,IndexFragment.OnFragmentInteractionListener ,ConsultFragment.OnFragmentInteractionListener
                    ,MedicineFragment.OnFragmentInteractionListener, PriDocFragment.OnFragmentInteractionListener
                    ,CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private static final int CHECKED_NONE = 0;
    private static final int CHECKED_INDEX = 1;
    private static final int CHECKED_CONSULT = 2;
    private static final int CHECKED_MEDICINE = 3;
    private static final int CHECKED_DOCTOR = 4;
    private int checkedOpt = CHECKED_NONE;

    private NavigationView navView;
    private View navHeader;
    private RadioButton radioButtonIndex;
    private RadioButton radioButtonConsult;
    private RadioButton radioButtonMedic;
    private RadioButton radioButtonDoc;

    private IndexFragment indexFragment;
    private ConsultFragment consultFragment;
    private MedicineFragment medicineFragment;
    private PriDocFragment priDocFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进入主页

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDefaultFragment();
        //设置点击事件
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navHeader = navView.inflateHeaderView(R.layout.nav_header_main);
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
     * set size of drawer
     * */
    private void setDrawableTopSize(TextView view, int size){
        Drawable[] ds =  view.getCompoundDrawables();
        ds[1].setBounds(0,0,size,size);
        view.setCompoundDrawables(null,ds[1],null,null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * function:Top main menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_menu_my_keep:
                //我的收藏
                break;
            case R.id.nav_menu_visited:
                //浏览记录
                break;
            case R.id.nav_menu_my_article:
                //我的文章
                break;
            case R.id.nav_menu_my_health_document:
                //健康文档
                break;
            case R.id.nav_menu_my_doctor:
                //私人医生
                break;
            case R.id.nav_menu_my_consult_record:
                //会诊记录
                break;
            case R.id.nav_menu_my_health_manage:
                //健康管理
                break;
            case R.id.nav_menu_my_relate_account:
                //亲情账号
                break;
        }
        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        菜单选中,切换fragment
        //        先checked 后 清除pre checked
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (buttonView.getId()){
            case R.id.radio_button_index:
                //Toast.makeText(this,"new Frag",Toast.LENGTH_SHORT).show();
                if(isChecked && checkedOpt != CHECKED_INDEX){
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
                    checkedOpt = CHECKED_CONSULT;
                    if(consultFragment == null){
                        consultFragment = new ConsultFragment();
                        transaction.add(R.id.fragment_show, consultFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(consultFragment);
                    transaction.commit();
                }
                break;
            case R.id.radio_button_medicine:
                if(isChecked && checkedOpt != CHECKED_MEDICINE){
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
        }
    }
}
