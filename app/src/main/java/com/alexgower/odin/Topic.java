package com.alexgower.odin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Topic {
    Context context;

    String topicName;
    String topicFileName;
    Bitmap topicImage;

    public Topic(String topicName,Bitmap topicImage,Context c){
        this.topicName = topicName;
        this.topicFileName = topicName.replace(' ','_');
        this.context = c;

        if(topicImage==null){
            BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
            mBitmapOptions.inSampleSize = 4;

            this.topicImage = BitmapFactory.decodeResource(c.getResources(), R.drawable.topic_default, mBitmapOptions);
        }else{
            this.topicImage = topicImage;
        }
    }

    public void make(){
        try {
            FileOutputStream fos = context.openFileOutput(topicFileName, Context.MODE_APPEND);
            fos.close();

            String imageFileName = topicFileName + ".png";
            FileOutputStream fos2 = context.openFileOutput(imageFileName,Context.MODE_PRIVATE);
            topicImage.compress(Bitmap.CompressFormat.PNG, 100, fos2);
            fos2.close();
        } catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void saveNewCard(String questionIn, String answerIn){
        try {
            FileOutputStream fos = context.openFileOutput(topicFileName, Context.MODE_APPEND);

            String question = questionIn + "\r\n";
            fos.write(question.getBytes());
            String answer = answerIn + "\r\n";
            fos.write(answer.getBytes());
            fos.close();

        } catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void readTopic(ArrayList<String> questionsArray, ArrayList<String> answersArray){
        try {
            FileInputStream ins = context.openFileInput(topicFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;

            int questionOrAnswer = 2;
            int lineNumber = 2;
            while ((line = reader.readLine()) != null) {
                switch (lineNumber % questionOrAnswer) {
                    case 0:
                        questionsArray.add(line);
                        break;
                    case 1:
                        answersArray.add(line);
                        break;
                }
                lineNumber++;
            }

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(questionsArray.size()!=answersArray.size()) {
            Toast.makeText(context, "Error occurred, different number of questions and answers", Toast.LENGTH_LONG).show();
        }
    }


}
