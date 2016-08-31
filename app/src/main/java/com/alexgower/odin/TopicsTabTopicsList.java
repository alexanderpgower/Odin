package com.alexgower.odin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TopicsTabTopicsList extends Fragment implements TopicClickListener {

    RecyclerView recList;
    TopicCardAdapter ca;

    ArrayList<String> topicFilesInPathArray = new ArrayList<>();
    ArrayList<Bitmap> topicImagesInPathArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.topics_tab_topics_list, container, false);

        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.topicsCardList);
        recList.setHasFixedSize(false);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        recList.setLayoutManager(glm);
        recList.setItemAnimator(new DefaultItemAnimator());
        ca = new TopicCardAdapter(createTopics(),this);
        ca.giveContext(getActivity());
        recList.setAdapter(ca);

        return v;
    }

    @Override
    public void callback(String topicNameIn) {

        final String topicName = topicNameIn;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.edit_topic_layout);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        ImageButton done = (ImageButton) dialog.findViewById(R.id.closeDialogButton);
        ImageButton selectImage = (ImageButton) dialog.findViewById(R.id.selectImageButton);
        ImageButton playTopic = (ImageButton) dialog.findViewById(R.id.playTopicButton);
        final EditText topicNameET = (EditText) dialog.findViewById(R.id.editTopicET);

        topicNameET.setText(topicName);
        //selectImage.setImageBitmap(topicImageIn);
        //selectImage.setBackgroundColor(Color.TRANSPARENT);

        playTopic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putString("topicName", topicName);

                final Fragment newFragment = new InTopic();
                newFragment.setArguments(bundle);
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.topics_tab_fragment_container, newFragment).commit();

                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String typedTopicName = topicNameET.getText().toString();
                if(!typedTopicName.equals(topicName)){
                    File topicFileOld = new File(getActivity().getFilesDir(),topicName.replace(' ','_'));
                    File topicFileNew = new File(getActivity().getFilesDir(),typedTopicName.replace(' ','_'));

                    File topicImageFileOld = new File(getActivity().getFilesDir(),topicName.replace(' ','_') + ".png");
                    File topicImageFileNew = new File(getActivity().getFilesDir(),typedTopicName.replace(' ','_') + ".png");

                    topicFileOld.renameTo(topicFileNew);
                    topicImageFileOld.renameTo(topicImageFileNew);

                }

                dialog.dismiss();
            }
        });


    }



    //DO IMAGE CHANGING AND TOPIC NAME CHANGING



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

