package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class MedicineFragment extends Fragment {
    private static final int FAME_COLUMN_NUM = 4;
    private static final int SECTION_COLUMN_NUM = 4;

    private RecyclerView recyclerFame;
    private RecyclerView recyclerSection;
    private MedicineFameAdapter fameAdapter;
    private MedicineIllnessAdapter sectionAdapter;
    private ArrayList<Illness> illnessList;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        recyclerFame = view.findViewById(R.id.recycler_medicine_famous);
        fameAdapter = new MedicineFameAdapter(getContext(), null);
        recyclerFame.setLayoutManager( new GridLayoutManager(getContext(), FAME_COLUMN_NUM) );
        recyclerFame.setAdapter(fameAdapter);

        recyclerSection = view.findViewById(R.id.recycler_medicine_illness);
        sectionAdapter = new MedicineIllnessAdapter(getContext(), illnessList);
        recyclerSection.setLayoutManager( new GridLayoutManager(getContext(), SECTION_COLUMN_NUM) );
        recyclerSection.setAdapter(sectionAdapter);
        return view;
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
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
