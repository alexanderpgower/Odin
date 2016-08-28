package com.alexgower.odin;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class TopicsTab extends Fragment {

    RecyclerView recList;
    TopicCardAdapter ca;

    ArrayList<String> topicFilesInPathArray = new ArrayList<>();
    ArrayList<Bitmap> topicImagesInPathArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.topics_tab, container, false);

        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.topicsCardList);
        recList.setHasFixedSize(false);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        recList.setLayoutManager(glm);
        recList.setItemAnimator(new DefaultItemAnimator());
        ca = new TopicCardAdapter(createTopics());
        ca.giveContext(getActivity());
        recList.setAdapter(ca);


        return v;
    }

    private List<TopicCardInfo> createTopics() {

        MainActivity mainActivity = ((MainActivity)getActivity());

        topicFilesInPathArray = mainActivity.getTopicFilesNames();
        topicImagesInPathArray = mainActivity.getTopicImageFiles();

        List<TopicCardInfo> result = new ArrayList<>();

        for (int i = 0; i < topicFilesInPathArray.size(); i++) {
            TopicCardInfo ci = new TopicCardInfo(topicFilesInPathArray.get(i), topicImagesInPathArray.get(i));
            result.add(ci);
        }

        return result;
    }


}