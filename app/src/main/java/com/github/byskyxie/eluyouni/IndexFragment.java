package com.github.byskyxie.eluyouni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.github.byskyxie.eluyouni.BaseActivity.IP_SERVER;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.BASE_INFO_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.COMMUNITY_ACCEPT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment implements ViewPager.OnPageChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LayoutInflater inflater;
    private ViewPager pager;
    private IndexPagerAdapter indexPagerAdapter;
    private IndexHandler handler = new IndexHandler(this);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IndexFragment() {
        // Required empty public constructor
    }

    protected static class IndexHandler extends Handler {
        private final WeakReference<IndexFragment> fragment;
        IndexHandler(IndexFragment fragment){
            this.fragment = new WeakReference<IndexFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COMMUNITY_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler COMMUNITY");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.addCommList((ArrayList<PatientCommunity>) msg.obj);
                    (fragment.get()).getBaseInfo((ArrayList<PatientCommunity>)msg.obj);
                    break;
                case BASE_INFO_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler BASE_INFO");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.notifyDataChanged();
                    break;
            }
        }

        @Override
        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            return super.sendMessageAtTime(msg, uptimeMillis);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        //设置pager内容
        pager = view.findViewById(R.id.view_pager);
        ArrayList<View> views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.pager_focus,pager,false));
        views.add(inflater.inflate(R.layout.pager_recommend,pager,false));
        views.add(inflater.inflate(R.layout.pager_patient_exper,pager,false));
        views.add(inflater.inflate(R.layout.pager_famous_doc,pager,false));
        views.add(inflater.inflate(R.layout.pager_community,pager,false));

        ArrayList<String> titles = new ArrayList<String>();
        titles.add(getString(R.string.pager_focus));
        titles.add(getString(R.string.pager_recommend));
        titles.add(getString(R.string.pager_patient_exper));
        titles.add(getString(R.string.pager_famous_doc));
        titles.add(getString(R.string.pager_community));
        indexPagerAdapter = new IndexPagerAdapter(getContext(),views,titles);
        pager.setAdapter(indexPagerAdapter);
        pager.addOnPageChangeListener(this);
        setPagerItemAdapter(pager);

        return view;
    }

    private void setPagerItemAdapter(ViewPager pager){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(((RecyclerView)pager.findViewById(R.id.recycler_community)).getAdapter()!=null ){
                            return;
                        }
                        ArrayList<PatientCommunity> commList = new ArrayList<PatientCommunity>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/community?"+"pid="+ BaseActivity.userInfo.getPid() +"&startpos="+indexPagerAdapter.communityCurrentPos;
                        URL url = null;
                        try {
                            //链接服务器请求验证
                            url = new URL(request);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStreamReader ins = new InputStreamReader( urlConnection.getInputStream());
                            BufferedReader br = new BufferedReader(ins);
                            //获得结果
                            String line = br.readLine();
                            if(line.matches("failed.*")){
                                //说明失败
                                return;
                            }else if(line.matches("accepted.*")){
                                //成功
                                line = br.readLine();
                                int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                                //更新位置
                                indexPagerAdapter.communityCurrentPos += num;
                                for(int i=0; i<num; i++){
                                    String temp;
                                    PatientCommunity pc = new PatientCommunity();
                                    //cid
                                    line = br.readLine();
                                    pc.setCid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
                                    //erid
                                    line = br.readLine();
                                    pc.setErId( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
                                    //ertype
                                    line = br.readLine();
                                    pc.setErType( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
                                    //time
                                    line = br.readLine();
                                    pc.setTime( line.substring(line.indexOf('=')+1) );
                                    //content
                                    line = br.readLine();
                                    temp = line.substring(line.indexOf('=')+1);
                                    do{
                                        line = br.readLine();
                                        if(line.matches("assent=.+"))
                                            break;
                                        temp += line;
                                    }while(true);
                                    pc.setCcontent( temp );
                                    //assent 直到遇见assent
                                    pc.setAssentNum( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
                                    commList.add(pc);
                                }
                                //结束读取数据
                                Message msg = new Message();
                                msg.obj = commList;
                                msg.what = COMMUNITY_ACCEPT;
                                handler.sendMessage(msg);
                            }
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    private void getBaseInfo(final ArrayList<PatientCommunity> list){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(list == null)
                    return;
                for(PatientCommunity pc: list){
                    ContentValues content = new ContentValues();
                    Cursor cursor;
                    if(pc.getErType() == 1){
                        cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                                , "PID=?",new String[]{""+pc.getErId()},null,null,null,null);
                        if(cursor.moveToFirst()){
                            cursor.close();
                            continue;
                        }
                        Patient patient = getPatientBaseInfo(pc.getErId());
                        content.put("PID",patient.getPid());
                        content.put("PNAME",patient.getPname());
                        content.put("PICON",patient.getPicon());
                        content.put("PSCORE",patient.getPscore());
                        BaseActivity.userDatabasewrit.insert("PATIENT_BASE_INFO", null, content);
                        cursor.close();
                    }else{
                        cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                                , "DID=?",new String[]{""+pc.getErId()},null,null,null,null);
                        if(cursor.moveToFirst()){
                            cursor.close();
                            continue;
                        }
                        Doctor doctor = getDoctorBaseInfo(pc.getErId());
                        content.put("DID",doctor.getDid());
                        content.put("DNAME",doctor.getDname());
                        content.put("DICON",doctor.getDicon());
                        content.put("DSECTION",doctor.getDsection());
                        content.put("DGRADE",doctor.getDgrade());
                        BaseActivity.userDatabasewrit.insert("DOCTOR_BASE_INFO", null, content);
                        cursor.close();
                    }

                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    protected Patient getPatientBaseInfo(long pid) {
        Patient patient = new Patient();
        String request = "http://"+IP_SERVER+":8080/"+"eluyouni/baseinfo?"+"reqid="+BaseActivity.userInfo.getPid()+"&erid="+pid+"&ertype="+1;
        URL url = null;
        try {
            //链接服务器请求验证
            url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader ins = new InputStreamReader( urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(ins);
            //获得结果
            String line = br.readLine();
            if(line.matches("failed.*")){
                //说明失败
                return null;
            }else if(line.matches("accepted.*")){
                //成功
                for(int i=0; ;i++){
                    line = br.readLine();
                    if(line == null)
                        break;
                    switch (i){
                        case 0:
                            patient.setPid(Long.parseLong( line.substring(line.indexOf('=')+1) ));
                            break;
                        case 1:
                            patient.setPname( line.substring(line.indexOf('=')+1 ));
                            break;
                        case 2:
                            String s = line.substring(line.indexOf('=')+1 );
                            if(!s.equals("null")){
                                patient.setPicon(null);
                            }else
                                //TODO:图片路径，应该保存到本地
                                patient.setPicon( s );
                            break;
                        case 3:
                            patient.setPscore( Integer.parseInt(line.substring(line.indexOf('=')+1 )) );
                            break;
                    }
                }
            }
        }catch (IOException ioe){
            Log.e(".BaseActivity",ioe.toString());
            return  null;
        }
        return patient;
    }

    protected Doctor getDoctorBaseInfo(long did) {
        Doctor doctor = new Doctor();
        String request = "http://"+IP_SERVER+":8080/"+"eluyouni/baseinfo?"+"reqid="+BaseActivity.userInfo.getPid()+"&erid="+did+"&ertype="+2;
        URL url = null;
        try {
            //链接服务器请求验证
            url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader ins = new InputStreamReader( urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(ins);
            //获得结果
            String line = br.readLine();
            if(line.matches("failed.*")){
                //说明失败
                return null;
            }else if(line.matches("accepted.*")){
                //成功
                for(int i=0;;i++){
                    line = br.readLine();
                    if(line == null)
                        break;
                    switch (i){
                        case 0:
                            doctor.setDid(Long.parseLong( line.substring(line.indexOf('=')+1)) );
                            break;
                        case 1:
                            doctor.setDname( line.substring(line.indexOf('=')+1 ) );
                            break;
                        case 2:
                            String s = line.substring(line.indexOf('=')+1 );
                            if(!s.equals("null")){
                                doctor.setDicon(null);
                            }else
                                //TODO:图片路径，应该保存到本地
                                doctor.setDicon( s );
                            break;
                        case 3:
                            doctor.setDsection( line.substring( line.indexOf('=')+1 ) );
                            break;
                        case 4:
                            doctor.setDgrade( Integer.parseInt( line.substring(line.indexOf('=')+1 ) ) );
                            break;
                    }
                }
            }
        }catch (IOException ioe){
            Log.e(".BaseActivity",ioe.toString());
            return  null;
        }
        return doctor;
    }

}
