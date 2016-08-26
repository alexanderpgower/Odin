package com.alexgower.odin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class RandomTab extends Fragment {

    // SwipeRefreshLayout swipeContainer;
    RecyclerView recList;
    QuestionCardAdapter ca;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.random_tab, container, false);

        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setItemAnimator(new DefaultItemAnimator());

        ca = new QuestionCardAdapter(createQuestions());
        recList.setAdapter(ca);

        return v;
    }

    private List<QuestionCardInfo> createQuestions() {

        List<QuestionCardInfo> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            QuestionCardInfo ci = new QuestionCardInfo("Question", "Answer");
            result.add(ci);
        }

        return result;
    }
}

