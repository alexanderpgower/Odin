package com.alexgower.odin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class InTopic extends Fragment {

    RecyclerView recList;
    QuestionCardAdapter ca;
    List<String> allQuestions = new ArrayList<>();
    List<String> allAnswers = new ArrayList<>();

    String topicName;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.in_topic, container, false);
        setHasOptionsMenu(true);

        topicName = getArguments().getString("topicName");
        getTopicCards();

        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.inTopicQuestionsCardList);
        recList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setItemAnimator(new DefaultItemAnimator());
        ca = new QuestionCardAdapter(createQuestions());
        recList.setAdapter(ca);


        return v;
    }

    public void getTopicCards() {
        String filename = topicName.replace(' ', '_');

        try {
            FileInputStream ins = getActivity().openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;

            int questionOrAnswer = 2;
            int lineNumber = 2;
            while ((line = reader.readLine()) != null) {
                switch (lineNumber % questionOrAnswer) {
                    case 0:
                        allQuestions.add(line);
                        break;
                    case 1:
                        allAnswers.add(line);
                        break;
                }
                lineNumber++;
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(allQuestions.size()!=allAnswers.size()) {
            Toast.makeText(getContext(), "Error occurred, different number of questions and answers", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_intopic, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.back_item:

                final Fragment newFragment = new TopicsTabTopicsList();
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.topics_tab_fragment_container, newFragment).commit();


                return true;
            default:
                return super.onOptionsItemSelected(item);
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

