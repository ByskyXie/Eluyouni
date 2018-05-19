package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import static com.github.byskyxie.eluyouni.IndexPagerAdapter.COMMUNITY_ACCEPT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriDocFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriDocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriDocFragment extends Fragment {

    private static final int PRIVATE_DOCTOR_ACCEPT = 0x002;

    private PriHandler handler;
    private PriDocAdapter adapter;
    protected RecyclerView recyclerPri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PriDocFragment() {
        // Required empty public constructor
    }

    protected static class PriHandler extends Handler{
        private final WeakReference<PriDocFragment> fragment;

        public PriHandler(WeakReference<PriDocFragment> fragment) {
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PRIVATE_DOCTOR_ACCEPT:
                    if(fragment.get() == null){
                        Log.e("PriFragment","weakreference is recycle");
                        return;
                    }
                    if(!fragment.get().adapter.compareDataSetSame((ArrayList<Doctor>) msg.obj))
                        fragment.get().adapter.addData((ArrayList<Doctor>) msg.obj);
                    fragment.get().adapter.notifyDataSetChanged();
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
     * @return A new instance of fragment PriDocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriDocFragment newInstance(String param1, String param2) {
        PriDocFragment fragment = new PriDocFragment();
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
        if(handler == null)
            handler = new PriHandler(new WeakReference<PriDocFragment>(this) );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pri_doc, container, false);
        recyclerPri = view.findViewById(R.id.recycler_pri_doc);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerPri.setLayoutManager(llm);
        adapter = new PriDocAdapter(getContext(),null);
        recyclerPri.setAdapter( adapter );
        getPrivateList();
        return view;
    }

    private void getPrivateList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Doctor> docList = new ArrayList<>();
                String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/privatedoc?"+"pid="+ BaseActivity.userInfo.getPid()
                        +"&startpos="+(1+adapter.getItemCount());
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
                        String temp;
                        Doctor doctor = new Doctor();
                        //did
                        line = br.readLine();
                        doctor.setDid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
                        //dsex
                        line = br.readLine();
                        doctor.setDsex( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
                        //dname
                        line = br.readLine();
                        doctor.setDname( line.substring(line.indexOf('=')+1) );
                        //dicon
                        line = br.readLine();
                        doctor.setDicon( line.substring(line.indexOf('=')+1) );
                        //dillness
                        line = br.readLine();
                        doctor.setDillness( line.substring(line.indexOf('=')+1) );
                        //dgrade
                        line = br.readLine();
                        doctor.setDgrade( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
                        //dhospital
                        line = br.readLine();
                        doctor.setDhospital( line.substring(line.indexOf('=')+1) );
                        docList.add(doctor);
                    }
                    //结束读取数据
                    Message msg = new Message();
                    msg.obj = docList;
                    msg.what = PRIVATE_DOCTOR_ACCEPT;
                    handler.sendMessage(msg);
                }catch (IOException ioe){
                    ioe.getStackTrace();
                }
            }
        }).start();
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
}
