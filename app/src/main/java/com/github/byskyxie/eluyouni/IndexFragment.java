package com.github.byskyxie.eluyouni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import static com.github.byskyxie.eluyouni.BaseActivity.IP_SERVER;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.ARTICLE_DOCTOR_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.ARTICLE_PATIENT_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.ARTICLE_RECOMMEND_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.BASE_INFO_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.COMMUNITY_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.COMMUNITY_ICON_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.DOCTOR_ICON_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.FOCUS_EVENT_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.FOCUS_ICON_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.PATIENT_ICON_ACCEPT;
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.RECOMMEND_ICON_ACCEPT;


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
    private ViewPager pager;
    private TabLayout tab;
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
            this.fragment = new WeakReference<>(fragment);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FOCUS_EVENT_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler Focus");
                        break;
                    }
                    fragment.get().indexPagerAdapter.addFocusList((ArrayList<Object>) msg.obj);
                    (fragment.get()).getBaseInfo_focus((ArrayList<Object>)msg.obj);
                    break;
                case COMMUNITY_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler COMMUNITY");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.addCommList((ArrayList<PatientCommunity>) msg.obj);
                    (fragment.get()).getBaseInfo_community((ArrayList<PatientCommunity>)msg.obj);
                    break;
                case BASE_INFO_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler BASE_INFO");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.commitDataChanged();
                    break;
                case ARTICLE_DOCTOR_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler ARTICLE_DOC");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.addDocList((ArrayList<ArticleDoctor>)msg.obj );
                    (fragment.get()).getDoctorBaseInfo((ArrayList<ArticleDoctor>)msg.obj );
                    break;
                case ARTICLE_PATIENT_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler ARTICLE_DOC");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.addPatiList((ArrayList<ArticlePatient>)msg.obj );
                    (fragment.get()).getPatientBaseInfo((ArrayList<ArticlePatient>)msg.obj );
                    break;
                case ARTICLE_RECOMMEND_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("indexFragmentHandler","indexFragment has recycler ARTICLE_DOC");
                        break;
                    }
                    (fragment.get()).indexPagerAdapter.addRecomList((ArrayList<ArticleRecommend>)msg.obj );
                    (fragment.get()).getBaseInfo_recom((ArrayList<ArticleRecommend>)msg.obj );
                    break;
                case RECOMMEND_ICON_ACCEPT:
                    try {
                        while(fragment.get().indexPagerAdapter.recomRecycler.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().indexPagerAdapter.recomRecycler.getAdapter().notifyItemChanged((int)msg.obj);
                    break;
                case COMMUNITY_ICON_ACCEPT:
                    try {
                        while(fragment.get().indexPagerAdapter.commRecycler.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().indexPagerAdapter.commRecycler.getAdapter().notifyItemChanged((int)msg.obj);
                    break;
                case DOCTOR_ICON_ACCEPT:
                    try {
                        while(fragment.get().indexPagerAdapter.docRecycler.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().indexPagerAdapter.docRecycler.getAdapter().notifyItemChanged((int)msg.obj);
                    break;
                case PATIENT_ICON_ACCEPT:
                    try {
                        while(fragment.get().indexPagerAdapter.patiRecycler.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().indexPagerAdapter.patiRecycler.getAdapter().notifyItemChanged((int)msg.obj);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        tab = view.findViewById(R.id.tab_view_pager);
        pager = view.findViewById(R.id.view_pager);
        //设置pager内容
        initialUI();
        return view;
    }

    private void initialUI(){
        ArrayList<View> views = new ArrayList<>();
        LinearLayoutManager llm;
        View item;
        RecyclerView.Adapter adapter;
        RecyclerView recycler;
        //focus
        llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        item = LayoutInflater.from(getContext()).inflate(R.layout.pager_focus,pager,false);
        adapter = new FocusAdapter(getContext(), null, handler);
        recycler = item.findViewById(R.id.recycler_focus);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL) );
        views.add(item);
        //recommend
        llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        item = LayoutInflater.from(getContext()).inflate(R.layout.pager_recommend, pager,false);
        adapter = new ArticleRecommendAdapter(getContext(), null, handler);
        recycler = item.findViewById(R.id.recycler_recommend);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL) );
        views.add(item);
        //article patient
        llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        item = LayoutInflater.from(getContext()).inflate(R.layout.pager_article_patient,pager,false);
        adapter = new ArticlePatientAdapter(getContext(), null, handler);
        recycler = item.findViewById(R.id.recycler_article_patient);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL) );
        views.add(item);
        //article doctor
        llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        item = LayoutInflater.from(getContext()).inflate(R.layout.pager_article_doctor,pager,false);
        adapter = new ArticleDoctorAdapter(getContext(), null, handler);
        recycler = item.findViewById(R.id.recycler_article_doctor);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL) );
        views.add(item);
        //community
        llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        item = LayoutInflater.from(getContext()).inflate(R.layout.pager_community,pager,false);
        adapter = new CommunityAdapter(getContext(), null, handler);
        recycler = item.findViewById(R.id.recycler_community);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL) );
        views.add(item);
        //finished

        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.pager_focus));
        titles.add(getString(R.string.pager_recommend));
        titles.add(getString(R.string.pager_patient_exper));
        titles.add(getString(R.string.pager_famous_doc));
        titles.add(getString(R.string.pager_community));

        indexPagerAdapter = new IndexPagerAdapter(getContext(),views,titles);
        pager.setAdapter(indexPagerAdapter);
        pager.addOnPageChangeListener(this);
        pager.setCurrentItem( 0 );    //设置主页
        onPageSelected(0);

        tab.setupWithViewPager(pager);
        tab.setTabMode(TabLayout.MODE_FIXED);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indexPagerAdapter.checkedPage = position;
        switch (position){
            case 0:
                //更新关注
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.userInfo == null)
                            return;
                        ArrayList<Object> focusList = new ArrayList<>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/patient/focusevent?"+"pid="+ BaseActivity.userInfo.getPid()
                                +"&startpos="+0+"&ndbody=false";    //TODO:startpos
                        URL url;
                        try {
                            //链接服务器请求验证
                            url = new URL(request);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStreamReader ins = new InputStreamReader( urlConnection.getInputStream());
                            BufferedReader br = new BufferedReader(ins);
                            //获得结果
                            String line = br.readLine();
                            if(line == null){
                                //说明失败
                                return;
                            }
                            //成功 读取数量
                            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                            for(int i=0; i<num; i++){
                                line = br.readLine();
                                if(getContext() == null){
                                    Log.e("IndexFrag","getContext() has revoke");
                                    return;
                                }//下载推荐文章
                                if(line.matches(".*ap"))
                                    focusList.add( ((BaseActivity)getContext()).downloadOneArticlePatient(br) );
                                else if(line.matches(".*ad"))
                                    focusList.add( ((BaseActivity)getContext()).downloadOneArticleDoctor(br) );
                                else if(line.matches(".*ac"))
                                    focusList.add( ((BaseActivity)getContext()).downloadOnePatientCommunity(br) );
                                else
                                    Log.e("IndexFrag","error focus type:"+line);
                            }
                            //结束读取数据
                            br.close();
                            Message msg = new Message();
                            msg.obj = focusList;
                            msg.what = FOCUS_EVENT_ACCEPT;
                            handler.sendMessage(msg);
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                        //function end
                    }
                }).start();
                break;
            case 1:
                //推荐更新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.userInfo == null)
                            return;
                        ArrayList<ArticleRecommend> recomList = new ArrayList<>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/recommend?"+"pid="+ BaseActivity.userInfo.getPid()
                                +"&startpos="+(1+indexPagerAdapter.patiRecycler.getAdapter().getItemCount())+"&ndbody=true";
                        URL url;
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
                            }else if(!line.matches("accepted.*")){
                                //其他情况
                                return;
                            }
                            //成功
                            line = br.readLine();
                            //数量
                            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                            for(int i=0; i<num; i++){
                                if(getContext() == null){
                                    Log.e("IndexFrag","getContext() has revoke");
                                    return;
                                }//下载推荐文章
                                recomList.add( ((BaseActivity)getContext()).downloadOneArticleRecommend(br) );
                            }
                            //结束读取数据
                            br.close();
                            Message msg = new Message();
                            msg.obj = recomList;
                            msg.what = ARTICLE_RECOMMEND_ACCEPT;
                            handler.sendMessage(msg);
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                        //function end
                    }
                }).start();
                break;
            case 2:
                //患者文章更新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.userInfo == null)
                            return;
                        ArrayList<ArticlePatient> patList = new ArrayList<>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/patientarticle?"+"pid="+ BaseActivity.userInfo.getPid()
                                +"&startpos="+(1+indexPagerAdapter.patiRecycler.getAdapter().getItemCount())+"&ndbody=true";
                        URL url;
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
                            }else if(!line.matches("accepted.*")){
                                //其他情况
                                return;
                            }
                            //成功
                            line = br.readLine();
                            //数量
                            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                            for(int i=0; i<num; i++){
                                if(getContext() == null){
                                    Log.e("IndexFrag","getContext() has revoke");
                                    return;
                                }//下载患者文章
                                patList.add( ((BaseActivity)getContext()).downloadOneArticlePatient(br) );
                            }
                            //结束读取数据
                            br.close();
                            Message msg = new Message();
                            msg.obj = patList;
                            msg.what = ARTICLE_PATIENT_ACCEPT;
                            handler.sendMessage(msg);
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                        //function end
                    }
                }).start();
                break;
            case 3:
                //医生文章更新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.userInfo == null)
                            return;
                        ArrayList<ArticleDoctor> docList = new ArrayList<>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/doctorarticle?"+"pid="+ BaseActivity.userInfo.getPid()
                                +"&startpos="+(1+indexPagerAdapter.docRecycler.getAdapter().getItemCount())+"&ndbody=true";
                        URL url;
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
                            }else if(!line.matches("accepted.*")){
                                //其他情况
                                return;
                            }
                            //成功
                            line = br.readLine();
                            //获得数量
                            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                            for(int i=0; i<num; i++){
                                if(getContext() == null){
                                    Log.e("IndexFrag","getContext() has revoke");
                                    return;
                                }//下载医生文章
                                docList.add( ((BaseActivity)getContext()).downloadOneArticleDoctor(br) );
                            }
                            //结束读取数据
                            br.close();
                            Message msg = new Message();
                            msg.obj = docList;
                            msg.what = ARTICLE_DOCTOR_ACCEPT;
                            handler.sendMessage(msg);
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                        //function end
                    }
                }).start();
            case 4:
                //说说更新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.userInfo == null)
                            return;
                        ArrayList<PatientCommunity> commList = new ArrayList<>();
                        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/community?"+"pid="+ BaseActivity.userInfo.getPid()
                                +"&startpos="+(1+indexPagerAdapter.commRecycler.getAdapter().getItemCount());
                        URL url;
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
                            }else if(!line.matches("accepted.*")){
                                //其他情况
                                return;
                            }
                            //成功
                            line = br.readLine();
                            //数量
                            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                            for(int i=0; i<num; i++){
                                if(getContext() == null){
                                    Log.e("IndexFrag","getContext() has revoke");
                                    return;
                                }//下载状态
                                commList.add( ((BaseActivity)getContext()).downloadOnePatientCommunity(br) );
                            }
                            //结束读取数据
                            br.close();
                            Message msg = new Message();
                            msg.obj = commList;
                            msg.what = COMMUNITY_ACCEPT;
                            handler.sendMessage(msg);
                        }catch (IOException ioe){
                            ioe.getStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    private void getBaseInfo_focus(final ArrayList<Object> list){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int renewNum = 0;   //更新信息数
                ContentValues content = new ContentValues();
                if(list == null)
                    return;
                for(Object obj: list){
                    Cursor cursor;
                    content.clear();
                    if( obj instanceof ArticlePatient || (obj instanceof PatientCommunity && ((PatientCommunity) obj).getErType()==1) ){
                        long pid = (obj instanceof ArticlePatient)? ((ArticlePatient) obj).getPid(): ((PatientCommunity) obj).getErId();
                        cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                                , "PID=?",new String[]{""+pid},null,null,null,null);
                        if(cursor.moveToFirst() || getContext() == null){
                            cursor.close();
                            continue;
                        }
                        cursor.close();
                        Patient patient = getPatientBaseInfo( pid );
                        if(((BaseActivity)getContext()).writePatientBaseInfo(patient))
                            renewNum++;
                        //下载头像
                        if(patient.getPicon()!=null && !patient.getPicon().isEmpty() &&
                                ((BaseActivity)getContext()).isPiconExists(patient.getPicon()))
                            ((BaseActivity)getContext()).downloadPicon(patient);
                    }else if( obj instanceof ArticleDoctor|| (obj instanceof PatientCommunity && ((PatientCommunity) obj).getErType()==2)){
                        long did = (obj instanceof ArticleDoctor)? ((ArticleDoctor) obj).getDid(): ((PatientCommunity) obj).getErId();
                        cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                                , "DID=?",new String[]{""+did},null,null,null,null);
                        if(cursor.moveToFirst() || getContext() == null){
                            cursor.close();
                            continue;
                        }
                        cursor.close();
                        Doctor doctor = getDoctorBaseInfo( did );
                        if(((BaseActivity)getContext()).writeDoctorBaseInfo(doctor))
                            renewNum++;
                        //下载头像
                        if(doctor.getDicon()!=null && !doctor.getDicon().isEmpty() &&
                                ((BaseActivity)getContext()).isDiconExists(doctor.getDicon()))
                            ((BaseActivity)getContext()).downloadDicon(doctor);
                    }
                }
                //如果一条信息都未更新，不必刷新视图
                if(renewNum == 0){
                    return;
                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();
        //function end
    }

    private void getBaseInfo_recom(final ArrayList<ArticleRecommend> list){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int renewNum = 0;   //更新信息数
                ContentValues content = new ContentValues();
                if(list == null)
                    return;
                for(ArticleRecommend ar: list){
                    Cursor cursor;
                    content.clear();
                    if(ar.getErtype() == 1){
                        cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                                , "PID=?",new String[]{""+ar.getErid()},null,null,null,null);
                        if(cursor.moveToFirst() || getContext() == null){
                            cursor.close();
                            continue;
                        }
                        cursor.close();
                        Patient patient = getPatientBaseInfo(ar.getErid());
                        if(((BaseActivity)getContext()).writePatientBaseInfo(patient))
                            renewNum++;
                        //下载头像
                        if(patient.getPicon()!=null && !patient.getPicon().isEmpty() &&
                                ((BaseActivity)getContext()).isPiconExists(patient.getPicon()))
                            ((BaseActivity)getContext()).downloadPicon(patient);
                    }else{
                        cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                                , "DID=?",new String[]{""+ar.getErid()},null,null,null,null);
                        if(cursor.moveToFirst() || getContext() == null){
                            cursor.close();
                            continue;
                        }
                        cursor.close();
                        Doctor doctor = getDoctorBaseInfo(ar.getErid());
                        if(((BaseActivity)getContext()).writeDoctorBaseInfo(doctor))
                            renewNum++;
                        //下载头像
                        if(doctor.getDicon()!=null && !doctor.getDicon().isEmpty() &&
                                ((BaseActivity)getContext()).isDiconExists(doctor.getDicon()))
                            ((BaseActivity)getContext()).downloadDicon(doctor);
                    }
                }
                //如果一条信息都未更新，不必刷新视图
                if(renewNum == 0){
                    return;
                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();
        //function end
    }

    private void getBaseInfo_community(final ArrayList<PatientCommunity> list){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int renewNum = 0;   //更新信息数
                ContentValues content = new ContentValues();
                if(list == null)
                    return;
                for(PatientCommunity pc: list){
                    Cursor cursor;
                    content.clear();
                    if(pc.getErType() == 1){
                        cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                                , "PID=?",new String[]{""+pc.getErId()},null,null,null,null);
                        if(cursor.moveToFirst()){
                            cursor.close();
                            continue;
                        }
                        Patient patient = getPatientBaseInfo(pc.getErId());
                        if(patient == null){
                            Log.e("getBaseInfo","patient error:"+pc.getErId());
                            continue;
                        }
                        content.put("PID",patient.getPid());
                        content.put("PSEX",patient.getPsex());
                        content.put("PNAME",patient.getPname());
                        content.put("PICON",patient.getPicon());
                        content.put("PSCORE",patient.getPscore());
                        BaseActivity.userDatabasewrit.insert("PATIENT_BASE_INFO", null, content);
                        cursor.close();
                        renewNum++;
                        //下载头像
                        if(getContext() != null && patient.getPicon()!=null && !patient.getPicon().isEmpty() &&
                                ((BaseActivity)getContext()).isPiconExists(patient.getPicon()))
                            ((BaseActivity)getContext()).downloadPicon(patient);
                    }else{
                        cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                                , "DID=?",new String[]{""+pc.getErId()},null,null,null,null);
                        if(cursor.moveToFirst()){
                            cursor.close();
                            continue;
                        }
                        Doctor doctor = getDoctorBaseInfo(pc.getErId());
                        if(doctor== null){
                            Log.e("getBaseInfo","doctor error");
                            continue;
                        }
                        content.put("DID",doctor.getDid());
                        content.put("DSEX",doctor.getDsex());
                        content.put("DNAME",doctor.getDname());
                        content.put("DICON",doctor.getDicon());
                        content.put("DILLNESS",doctor.getDillness());
                        content.put("DHOSPITAL",doctor.getDhospital());
                        content.put("DGRADE",doctor.getDgrade());
                        BaseActivity.userDatabasewrit.insert("DOCTOR_BASE_INFO", null, content);
                        cursor.close();
                        renewNum++;
                        //下载头像
                        if(getContext() != null && doctor.getDicon()!=null && !doctor.getDicon().isEmpty() &&
                                ((BaseActivity)getContext()).isDiconExists(doctor.getDicon()))
                            ((BaseActivity)getContext()).downloadDicon(doctor);
                    }

                }
                //如果一条信息都未更新，不必刷新视图
                if(renewNum == 0){
                    return;
                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();
        //function end
    }

    protected void getPatientBaseInfo(final ArrayList<ArticlePatient> list){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int renewNum = 0;   //更新信息数
                ContentValues content = new ContentValues();
                if(list == null)
                    return;
                for(ArticlePatient ap: list){
                    content.clear();
                    Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                            , "PID=?",new String[]{""+ap.getPid()},null,null,null,null);
                    if(cursor.moveToFirst()){
                        cursor.close();
                        continue;
                    }
                    cursor.close();
                    Patient patient = getPatientBaseInfo(ap.getPid());
                    if(patient == null || getContext() == null){
                        Log.e("getPatientBaseInfo","failed:"+ap.getPid());
                        continue;
                    }
                    if(((BaseActivity)getContext()).writePatientBaseInfo(patient))
                        renewNum++;
                    //下载头像
                    if(patient.getPicon()!=null && !patient.getPicon().isEmpty() &&
                            ((BaseActivity)getContext()).isPiconExists(patient.getPicon()))
                        ((BaseActivity)getContext()).downloadPicon(patient);
                }
                //如果一条信息都未更新，不必刷新视图
                if(renewNum == 0){
                    return;
                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();
        //function end
    }

    protected Patient getPatientBaseInfo(long pid) {
        if(BaseActivity.userInfo == null)
            return null;
        Patient patient = null;
        String request = "http://"+IP_SERVER+":8080/"+"eluyouni/baseinfo?"+"reqid="+BaseActivity.userInfo.getPid()+"&erid="+pid+"&ertype="+1;
        URL url;
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
                if(getContext() == null)
                    return null;
                patient = ((BaseActivity)getContext()).downloadOnePatientBaseInfo(br);
            }
            br.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
            return  null;
        }
        //下载头像
        if( patient!=null && patient.getPicon()!=null && !patient.getPicon().isEmpty() &&
                ((BaseActivity)getContext()).isPiconExists(patient.getPicon()))
            ((BaseActivity)getContext()).downloadPicon(patient);
        return patient;
    }

    protected void getDoctorBaseInfo(final ArrayList<ArticleDoctor> list){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int renewNum = 0;   //更新信息数
                if(list == null)
                    return;
                for(ArticleDoctor ad: list){
                    Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                            , "DID=?",new String[]{""+ad.getDid()},null,null,null,null);
                    if(cursor.moveToFirst()){
                        cursor.close();
                        continue;
                    }
                    cursor.close();
                    Doctor doctor = getDoctorBaseInfo(ad.getDid());
                    if(doctor == null || getContext() == null){
                        Log.e("getDoctorBaseInfo","error");
                        continue;
                    }
                    if(((BaseActivity)getContext()).writeDoctorBaseInfo(doctor))
                        renewNum++;
                    //下载头像
                    if(doctor.getDicon()!=null && !doctor.getDicon().isEmpty() &&
                            ((BaseActivity)getContext()).isDiconExists(doctor.getDicon()))
                        ((BaseActivity)getContext()).downloadDicon(doctor);
                }
                //如果一条信息都未更新，不必刷新视图
                if(renewNum == 0){
                    return;
                }
                Message msg = new Message();
                msg.what = BASE_INFO_ACCEPT;
                handler.sendMessage(msg);
            }
        }).start();
        //function end
    }

    protected Doctor getDoctorBaseInfo(long did) {
        if(BaseActivity.userInfo == null)
            return null;
        Doctor doctor = null;
        String request = "http://"+IP_SERVER+":8080/"+"eluyouni/baseinfo?"+"reqid="+BaseActivity.userInfo.getPid()+"&erid="+did+"&ertype="+2;
        URL url;
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
                if(getContext() == null)
                    return null;
                doctor = ((BaseActivity)getContext()).downloadOneDoctorBaseInfo(br);
            }
            br.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
            return  null;
        }
        //下载头像
        if(doctor != null && doctor.getDicon()!=null && !doctor.getDicon().isEmpty() &&
                ((BaseActivity)getContext()).isDiconExists(doctor.getDicon()))
            ((BaseActivity)getContext()).downloadDicon(doctor);
        return doctor;
    }

}
