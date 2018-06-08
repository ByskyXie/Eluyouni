package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerDoc;
    private RecyclerView recyclerFile;
    private ConsultDoctorAdapter doctorAdapter;
    private ConsultDataAdapter dataAdapter;
    private Button buttonOpenConsult;
    private EditText descri;    //病情描述

    private OnFragmentInteractionListener mListener;

    public ConsultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultFragment newInstance(String param1, String param2) {
        ConsultFragment fragment = new ConsultFragment();
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
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
//        view.findViewById(R.id.button_consult_submit).setBackgroundColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        descri = view.findViewById(R.id.edit_text_consult_describe);
        buttonOpenConsult = view.findViewById(R.id.button_consult_submit);
        buttonOpenConsult.setOnClickListener(this);

        recyclerDoc = view.findViewById(R.id.recycler_consult_doc_list);
        recyclerDoc.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        doctorAdapter = new ConsultDoctorAdapter( (BaseActivity) getContext());
        recyclerDoc.setAdapter(doctorAdapter);

        recyclerFile = view.findViewById(R.id.recycler_consult_data);
        recyclerFile.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false) );
        dataAdapter = new ConsultDataAdapter(getContext(), null);
        recyclerFile.setAdapter(dataAdapter);
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

    protected void addDataItem(String uri){
        if(uri==null || uri.isEmpty())
            return;
        dataAdapter.addData(uri);
        dataAdapter.notifyDataSetChanged();
    }

    protected void notifyDoctorList(){
        if(recyclerDoc == null || recyclerDoc.isComputingLayout())
            return;
        doctorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_consult_submit:
                if(doctorAdapter.getDoctorCount() == 0){
                    Toast.makeText(getContext(), "你还未选择会诊医生", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("TargetType",ChatActivity.TARGET_TYPE_CONSULT);
                    intent.putExtra("DESCRI", descri.getText().toString());
                    startActivity(intent);
                }
                break;
        }
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
