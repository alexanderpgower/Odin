package com.alexgower.odin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class RandomTab extends Fragment {

    RecyclerView recList;
    QuestionCardAdapter ca;
    ArrayList<String> allQuestions = new ArrayList<>();
    ArrayList<String> allAnswers = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.random_tab, container, false);

        getAllCards();
        setUpLayout(v);

        return v;
    }

    private void setUpLayout(View v){
        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.questionsCardList);
        recList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setItemAnimator(new DefaultItemAnimator());
        ca = new QuestionCardAdapter(createQuestions());
        recList.setAdapter(ca);
    }

    private void getAllCards() {

        MainActivity mainActivity = ((MainActivity)getActivity());

        ArrayList<String> topicFileNamesInPath = mainActivity.getTopicFilesNames();

        for(int i = 0; i<topicFileNamesInPath.size();i++) {
            Topic topic = new Topic(topicFileNamesInPath.get(i), null, getContext());
            topic.readTopic(allQuestions, allAnswers);

        }
    }

    private List<QuestionCardInfo> createQuestions() {

        List<QuestionCardInfo> result = new ArrayList<>();

        for (int i = 0; i < allQuestions.size(); i++) {
            QuestionCardInfo ci = new QuestionCardInfo(allQuestions.get(i), allAnswers.get(i));
            result.add(ci);
        }

        return result;
    }
}

