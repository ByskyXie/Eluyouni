package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineFragment extends Fragment implements View.OnClickListener{
    private static final int FAME_COLUMN_NUM = 3;
    private static final int SECTION_COLUMN_NUM = 4;
    private static final int ACCEPT_ILLNESS_LIST = 0x0010;
    private static final int ACCEPT_FAME_LIST = 0x0020;
    protected static final int ACCEPT_FAME_ICON = 0x0030;

    private TextView textMoreDoc;
    private RecyclerView recyclerFame;
    private RecyclerView recyclerIllness;
    private MedicineFameAdapter fameAdapter;
    private MedicineIllnessAdapter illnessAdapter;
    private ArrayList<Illness> illnessList;
    private ArrayList<Doctor> fameList;
    private MedicineHandler handler = new MedicineHandler(new WeakReference<MedicineFragment>(this));

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MedicineFragment() {
        // Required empty public constructor
    }

    static class MedicineHandler extends Handler {
        private WeakReference<MedicineFragment> fragment;

        MedicineHandler(WeakReference<MedicineFragment> fragment) {
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ACCEPT_ILLNESS_LIST:
                    if(fragment.get() == null || fragment.get().illnessAdapter.getItemCount()!=0)
                        return; //只接受一次，为0时
                    fragment.get().illnessAdapter.addData(fragment.get().illnessList);
                    try{
                    while(fragment.get().recyclerIllness.isComputingLayout())
                        Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().illnessAdapter.notifyDataSetChanged();
                    break;
                case ACCEPT_FAME_LIST:
                    if(fragment.get() == null || fragment.get().fameAdapter.getItemCount()!=0)
                        return;//只接受一次，为0时
                    fragment.get().fameAdapter.addData(fragment.get().fameList);
                    try{
                        while(fragment.get().recyclerFame.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().fameAdapter.notifyDataSetChanged();
                    break;
                case ACCEPT_FAME_ICON:
                    try {
                        while(fragment.get().recyclerFame.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    fragment.get().fameAdapter.notifyItemChanged((int)msg.obj);
                    break;
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineFragment newInstance(String param1, String param2) {
        MedicineFragment fragment = new MedicineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIllnessList();
        getFameList();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        recyclerFame = view.findViewById(R.id.recycler_medicine_famous);
        fameAdapter = new MedicineFameAdapter(getContext(), null, handler);
        GridLayoutManager glm = new GridLayoutManager(getContext(), FAME_COLUMN_NUM);
        recyclerFame.setLayoutManager( glm );
        recyclerFame.setNestedScrollingEnabled(true);  //禁止嵌套滑动，使有惯性
        recyclerFame.setAdapter(fameAdapter);

        recyclerIllness = view.findViewById(R.id.recycler_medicine_illness);
        illnessAdapter = new MedicineIllnessAdapter(getContext(), illnessList);
        recyclerIllness.setLayoutManager( new GridLayoutManager(getContext(), SECTION_COLUMN_NUM) );
        recyclerIllness.setNestedScrollingEnabled(true);   //禁止嵌套滑动，使有惯性
        recyclerIllness.setAdapter(illnessAdapter);

        textMoreDoc = view.findViewById(R.id.text_view_famous_more);
        textMoreDoc.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view_famous_more:
                Intent intent = new Intent(getContext(), DoctorListActivity.class);
                intent.putExtra("ILLNESS",new Illness("名医达人"));
                if(getContext() != null)
                    getContext().startActivity(intent);
                else
                    Log.e("MedicineFrag","getContext() Revoke.");
                break;
        }
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
        void onFragmentInteraction(Uri uri);
    }

    private void getIllnessList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(illnessList == null)
                    illnessList = new ArrayList<>();
                else
                    illnessList.clear();
                //下载
                String req = "http://"+ BaseActivity.IP_SERVER+":8080/"+"eluyouni/medicine?pid="+BaseActivity.userInfo.getPid()+"&req=illness";
                try{
                    URL url = new URL(req);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
                    String line = br.readLine();
                    if(line==null){
                        Log.e("MedicinFragment","connect medicine failed");
                        return;
                    }
                    int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                    for(int i=0;i<num;i++){
                        line = br.readLine();
                        illnessList.add( new Illness( line.substring(line.indexOf('=')+1) ) );
                    }
                    br.close();
                    Message msg = new Message();
                    msg.what = ACCEPT_ILLNESS_LIST;
                    handler.sendMessage(msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getFameList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(fameList == null)
                    fameList = new ArrayList<>();
                else
                    fameList.clear();
                //下载
                String req = "http://"+ BaseActivity.IP_SERVER+":8080/"+"eluyouni/medicine?pid="+BaseActivity.userInfo.getPid()+"&req=fame";
                try{
                    URL url = new URL(req);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
                    String line = br.readLine();
                    if(line==null){
                        Log.e("MedicinFragment","connect medicine failed");
                        return;
                    }
                    int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
                    for(int i=0;i<num;i++){
                        if(getContext() == null)
                            continue;
                        Doctor doctor = ((BaseActivity)getContext()).downloadOneDoctorBaseInfo(br);
                        if(doctor != null)
                            fameList.add( doctor );
                    }
                    //插入数据库
                    int i = 0;
                    if(getContext() != null)
                        i = ((BaseActivity)getContext()).writeDoctorBaseInfo(fameList);
                    Log.w("getList","get new info:"+i);
                    br.close();
                    Message msg = new Message();
                    msg.what = ACCEPT_FAME_LIST;
                    handler.sendMessage(msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
