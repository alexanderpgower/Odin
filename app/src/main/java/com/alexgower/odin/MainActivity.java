package com.alexgower.odin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

    //Layout variables
    ViewPager pager;
    ViewPagerAdapter adapter;
    CharSequence Titles[]={"Random","Topics"};
    int NumberOfTabs = 2;

    //Topic image selection variables
    int IMAGE_PICKER_SELECT = 0;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.dismiss();
            }
        });

        newQuestion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                dialog.dismiss();

                try {
                    Thread.sleep(300);
                    dialog.show();
                } catch (InterruptedException e) {

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


        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.dismiss();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_SELECT);

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
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, false);

                selectImage.setImageBitmap(resizedBitmap);
                selectImage.setBackgroundColor(Color.TRANSPARENT);

            }catch(FileNotFoundException e){
                Toast.makeText(context,"Could not find image file there",Toast.LENGTH_LONG);
            }
        }

    }

}
