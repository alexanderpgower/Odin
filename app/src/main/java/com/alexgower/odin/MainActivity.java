package com.alexgower.odin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    final Context context = this;

    //Layout variables
    ViewPager pager;
    ViewPagerAdapter adapter;
    CharSequence Titles[]={"Random","Topics"};
    int NumberOfTabs = 2;

    //Topic image selection variables
    int IMAGE_PICKER_SELECT = 0;
    AlertDialog dialog;

    //Topic variables
    Bitmap topicBitmap;

    //New question variables
    Spinner topicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpExampleTopic();
        setUpLayout();
        setUpFloatingActionButtions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpLayout(){
        // Creating The Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles, NumberOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);
    }

    public void setUpFloatingActionButtions(){
        //FloatingActionButtons in FloatingActionMenu
        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        FloatingActionButton floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {setUpNewQuestionDialog();}});
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {setupNewTopicDialog();}});
    }

    public void setUpNewQuestionDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.new_question_layout);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        ImageButton done = (ImageButton) dialog.findViewById(R.id.closeDialogButton);
        ImageButton newQuestion = (ImageButton) dialog.findViewById(R.id.newQuestionButton);

        topicSpinner = (Spinner) dialog.findViewById(R.id.topicForQuestionSpinner);
        List<String> list = getTopicFilesNames();
        list.add(0, "Choose topic for question");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicSpinner.setAdapter(dataAdapter);
        topicSpinner.setSelection(list.indexOf("Choose topic for question"));


        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!topicSpinner.getSelectedItem().toString().equals("Choose topic for question")) {
                    EditText questionEditText = (EditText) dialog.findViewById(R.id.questionEditText);
                    EditText answerEditText = (EditText) dialog.findViewById(R.id.answerEditText);
                    Topic topic = new Topic(topicSpinner.getSelectedItem().toString());
                    topic.saveNewCard(context, questionEditText.getText().toString(), answerEditText.getText().toString());

                    questionEditText.setText("");
                    answerEditText.setText("");
                    dialog.dismiss();
                }else{
                    Toast.makeText(context, "Choose a topic for the question.",Toast.LENGTH_SHORT).show();
                }


            }
        });

        newQuestion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(!topicSpinner.getSelectedItem().toString().equals("Choose topic for question")) {
                    EditText questionEditText = (EditText) dialog.findViewById(R.id.questionEditText);
                    EditText answerEditText = (EditText) dialog.findViewById(R.id.answerEditText);
                    Topic topic = new Topic(topicSpinner.getSelectedItem().toString());
                    topic.saveNewCard(context, questionEditText.getText().toString(), answerEditText.getText().toString());

                    questionEditText.setText("");
                    answerEditText.setText("");
                    dialog.dismiss();

                    try {
                        Thread.sleep(300);
                        dialog.show();
                    } catch (InterruptedException e) {

                    }
                }else{
                    Toast.makeText(context, "Choose a topic for the question.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setupNewTopicDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.new_topic_layout);
        dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        ImageButton done = (ImageButton) dialog.findViewById(R.id.closeDialogButton);
        ImageButton selectImage = (ImageButton) dialog.findViewById(R.id.selectImageButton);

        selectImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_SELECT);

            }
        });

        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText newTopicET = (EditText) dialog.findViewById(R.id.newTopicET);
                String topicName = newTopicET.getText().toString();

                Topic topic = new Topic(topicName,topicBitmap,context);
                topic.make(context);

                dialog.dismiss();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton selectImage = (ImageButton) dialog.findViewById(R.id.selectImageButton);

        if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageStream);

                topicBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, false);

                selectImage.setImageBitmap(topicBitmap);
                selectImage.setBackgroundColor(Color.TRANSPARENT);

            }catch(FileNotFoundException e){
                Toast.makeText(context,"Could not find image file there",Toast.LENGTH_LONG);
            }
        }

    }

    public void setUpExampleTopic(){
        //Hardcoded file information just to check if already exists
        String filepath = getFilesDir() + "/Example_Topic";
        File exampleTopicFile = new File(filepath);

        if(!exampleTopicFile.exists()){
            Topic exampleTopic = new Topic("Example Topic",null,context);
            exampleTopic.make(context);
            for(int i=0; i<100;i++){
                String question = "Example Question " + i ;
                String answer = "Answer to example question " + i;
                exampleTopic.saveNewCard(context,question,answer);
            }
            Toast.makeText(context,"Auto-made 'Example Topic'.",Toast.LENGTH_LONG).show();
            }
        }

    public ArrayList<String> getTopicFilesNames(){
        File allFilesInPath[] = getFilesDir().listFiles();

        ArrayList<String> result = new ArrayList<>();

        //Remove files this app didn't make
        for(int i = 0; i< allFilesInPath.length; i++){
            if(!allFilesInPath[i].getName().equals("instant-run")&&!allFilesInPath[i].getName().equals("rList-com.alexgower.odin.MainActivity")&&!allFilesInPath[i].isDirectory()&&!allFilesInPath[i].getName().endsWith(".png")){
                result.add(allFilesInPath[i].getName().replace('_',' '));
            }
        }
        return result;
    }

    public ArrayList<File> getTopicFiles(){
        File allFilesInPath[] = getFilesDir().listFiles();

        ArrayList<File> result = new ArrayList<>();

        //Remove files this app didn't make
        for(int i = 0; i< allFilesInPath.length; i++){
            if(!allFilesInPath[i].getName().equals("instant-run")&&!allFilesInPath[i].getName().equals("rList-com.alexgower.odin.MainActivity")&&!allFilesInPath[i].isDirectory()&&!allFilesInPath[i].getName().endsWith(".png")){
                result.add(allFilesInPath[i]);
            }
        }
        return result;
    }

    public ArrayList<Bitmap> getTopicImageFiles(){
        File allFilesInPath[] = getFilesDir().listFiles();

        ArrayList<Bitmap> result = new ArrayList<>();

        //Remove files this app didn't make
        for(int i = 0; i< allFilesInPath.length; i++){
            if(!allFilesInPath[i].getName().equals("instant-run")&&!allFilesInPath[i].getName().equals("rList-com.alexgower.odin.MainActivity")&&!allFilesInPath[i].isDirectory()&&allFilesInPath[i].getName().endsWith(".png")){
                result.add(getBitmapOf(allFilesInPath[i]));
            }
        }
        return result;
    }


    public Bitmap getBitmapOf(File filePath) {
        Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.tick);
        try {
            FileInputStream fi = new FileInputStream(filePath);
            image = BitmapFactory.decodeStream(fi);
            return image;
        } catch (Exception e){

        }
        return image;
    }


}

