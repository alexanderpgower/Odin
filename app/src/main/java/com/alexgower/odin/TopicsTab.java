package com.alexgower.odin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class TopicsTab extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.topics_tab, container, false);

        final Fragment TopicsTabTopicsList = new TopicsTabTopicsList();
        final FragmentTransaction initialTransaction = getChildFragmentManager().beginTransaction();
        initialTransaction.add(R.id.topics_tab_fragment_container, TopicsTabTopicsList).commit();

        return v;
    }

}