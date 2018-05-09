package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class IndexPagerAdapter extends PagerAdapter {

    static final int COMMUNITY_ACCEPT = 0x010;
    static final int BASE_INFO_ACCEPT = 0x0100;

    private Context context;
    private ArrayList<View> list;
    private ArrayList<String> titles;

    public int communityCurrentPos = 1;   //record position
    private ArrayList<PatientCommunity> commList;
    private RecyclerView commRecycler;

    IndexPagerAdapter(Context context, ArrayList<View> list, ArrayList<String> titles) {
        this.context = context;
        this.list = list;
        this.titles = titles;
    }

    public void addCommList(ArrayList<PatientCommunity> commList) {
        if(this.commList == null || this.commList.get(0).getCcontent().equals(commList.get(0).getCcontent()))
            this.commList = commList;
        else{
            //加入新元素
            this.commList.addAll(commList);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        RecyclerView recyclerView;
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
                //community
                if(commRecycler == null)
                    commRecycler = container.findViewById(R.id.recycler_community);
                //排除无内容显示的情况
                if(commList==null){
                    break;
                }
                CommunityAdapter adapter = new CommunityAdapter(context,commList);
                LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayout.VERTICAL, false);
                llm.setSmoothScrollbarEnabled(true);
                commRecycler.setLayoutManager(llm);
                commRecycler.setAdapter(adapter );
                adapter.notifyDataSetChanged();
//                commRecycler.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
                break;
        }
    }

    protected  void notifyDataChanged(){
        if(commRecycler == null || commRecycler.getAdapter() == null)
            return;
        commRecycler.getAdapter().notifyDataSetChanged();
    }

}
