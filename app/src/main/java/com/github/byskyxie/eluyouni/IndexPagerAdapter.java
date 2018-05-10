package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class IndexPagerAdapter extends PagerAdapter{

    static final int COMMUNITY_ACCEPT = 0x0010;
    static final int BASE_INFO_ACCEPT = 0x0100;
    static final int ARTICLE_DOCTOR_ACCEPT = 0x1000;
    public int communityCurrentPos = 1;   //record community position

    private Context context;
    private ArrayList<View> list;   //五个pager的示例
    private ArrayList<String> titles;   //五个pager的名字
    protected int checkedPage = -1;   //当前所在页面位置

    private RecyclerView focusRecycler;
    private RecyclerView recomRecycler;
    private RecyclerView patiRecycler;
    private RecyclerView docRecycler;
    private RecyclerView commRecycler;

    IndexPagerAdapter(Context context, ArrayList<View> list, ArrayList<String> titles) {
        this.context = context;
        this.list = list;
        this.titles = titles;
        focusRecycler = list.get(0).findViewById(R.id.recycler_focus);
        recomRecycler = list.get(1).findViewById(R.id.recycler_recommend);
        patiRecycler = list.get(2).findViewById(R.id.recycler_article_patient);
        docRecycler = list.get(3).findViewById(R.id.recycler_article_doctor);
        commRecycler = list.get(4).findViewById(R.id.recycler_community);
    }

    public void addDocList(ArrayList<ArticleDoctor> docList) {
        if(((DoctorArticleAdapter)docRecycler.getAdapter()).compareDataSetSame(docList))
            return;
        //加入新元素
        ((DoctorArticleAdapter)docRecycler.getAdapter()).addData(docList);
        commitDataChanged(3);
    }

    public void addCommList(ArrayList<PatientCommunity> commList) {
        if(((CommunityAdapter)commRecycler.getAdapter()).compareDataSetSame(commList))
            return; //无变化
        //加入新元素
        ((CommunityAdapter)commRecycler.getAdapter()).addData(commList);
        commitDataChanged(4);
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
        checkedPage = position;
        switch (checkedPage){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:
                if(docRecycler == null)
                    docRecycler = container.findViewById(R.id.recycler_article_doctor);
                docRecycler.getAdapter().notifyDataSetChanged();
                break;
            case 4:
                //community
                if(commRecycler == null)
                    commRecycler = container.findViewById(R.id.recycler_community);
                commRecycler.getAdapter().notifyDataSetChanged();
                break;
        }
    }

    protected void commitDataChanged(){
        //代表更新当前视角
        commitDataChanged(-1);
    }

    private void commitDataChanged(int pos){
        if(pos == -1)   //-1 代表当前视角
            pos = checkedPage;
        switch (pos){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                if(docRecycler == null || docRecycler.getAdapter() == null){
                    break;
                }
//                ((DoctorArticleAdapter)docRecycler.getAdapter()).setData(docList);
                docRecycler.getAdapter().notifyDataSetChanged();
                break;
            case 4:
                if(commRecycler == null || commRecycler.getAdapter() == null)
                    break;
//                ((CommunityAdapter)docRecycler.getAdapter()).setData(commList);
                commRecycler.getAdapter().notifyDataSetChanged();
                break;
        }

    }

}
