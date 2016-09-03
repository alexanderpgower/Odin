package com.alexgower.odin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class TopicsTabTopicsList extends Fragment implements TopicClickListener {

    RecyclerView recList;
    TopicCardAdapter ca;

    ArrayList<String> topicFilesInPathArray = new ArrayList<>();
    ArrayList<Bitmap> topicImagesInPathArray = new ArrayList<>();

    AlertDialog dialog;
    int FRAGMENT_IMAGE_PICKER_SELECT = 0;
    Bitmap topicBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.topics_tab_topics_list, container, false);

        setUpLayout(v);

        return v;
    }

    private void setUpLayout(View v){
        //Create RecyclerView and LinearLayoutManager for that view of "All Cards"
        recList = (RecyclerView) v.findViewById(R.id.topicsCardList);
        recList.setHasFixedSize(false);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        recList.setLayoutManager(glm);
        recList.setItemAnimator(new DefaultItemAnimator());
        ca = new TopicCardAdapter(createTopics(),this);
        ca.giveContext(getActivity());
        recList.setAdapter(ca);

    }

    @Override
    public void callback(String topicNameIn) {

        final String topicName = topicNameIn;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.edit_topic_layout);
        dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        ImageButton done = (ImageButton) dialog.findViewById(R.id.closeDialogButton);
        ImageButton selectImage = (ImageButton) dialog.findViewById(R.id.selectImageButton);
        ImageView selectImageView = (ImageView) dialog.findViewById(R.id.selectImageView);
        ImageButton playTopic = (ImageButton) dialog.findViewById(R.id.playTopicButton);
        ImageButton deleteTopic = (ImageButton) dialog.findViewById(R.id.deleteTopicButton);
        final EditText topicNameET = (EditText) dialog.findViewById(R.id.editTopicET);

        topicNameET.setText(topicName);

        int topicPosition = topicFilesInPathArray.indexOf(topicName);
        Bitmap topicImage = topicImagesInPathArray.get(topicPosition);
        selectImageView.setImageBitmap(topicImage);

        selectImage.setBackgroundColor(Color.TRANSPARENT);

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

        deleteTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ca.removeLastClicked();
                dialog.dismiss();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), FRAGMENT_IMAGE_PICKER_SELECT);

            }
        });

        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String typedTopicName = topicNameET.getText().toString().replace("\n","");
                if(!typedTopicName.equals(topicName)){

                    MainActivity mainActivity = (MainActivity)getActivity();
                    if(!typedTopicName.equals("")&&!mainActivity.getTopicFilesNames().contains(typedTopicName)) {
                        File topicFileOld = new File(getActivity().getFilesDir(), topicName.replace(' ', '_'));
                        File topicFileNew = new File(getActivity().getFilesDir(), typedTopicName.replace(' ', '_'));

                        File topicImageFileOld = new File(getActivity().getFilesDir(), topicName.replace(' ', '_') + ".png");
                        File topicImageFileNew = new File(getActivity().getFilesDir(), typedTopicName.replace(' ', '_') + ".png");

                        topicFileOld.renameTo(topicFileNew);
                        topicImageFileOld.renameTo(topicImageFileNew);

                        if(!(topicBitmap==null)){
                            String imageFileName = typedTopicName.replace(' ','_') + ".png";
                            try {
                                File currentImageFile = new File(getActivity().getFilesDir(), imageFileName);
                                currentImageFile.delete();

                                FileOutputStream fos = getContext().openFileOutput(imageFileName, Context.MODE_PRIVATE);
                                topicBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                fos.close();
                            } catch(Exception e){
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                        dialog.dismiss();

                    }
                    else{
                        Toast.makeText(getContext(),"Error: Topic name is empty or already used.",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView selectImageView = (ImageView) dialog.findViewById(R.id.selectImageView);

        if (requestCode == FRAGMENT_IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);

                BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
                mBitmapOptions.inSampleSize = 4;
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageStream, null, mBitmapOptions);
                topicBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, false);

                selectImageView.setImageBitmap(topicBitmap);

            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Could not find image file there", Toast.LENGTH_LONG);
            }
        }
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

