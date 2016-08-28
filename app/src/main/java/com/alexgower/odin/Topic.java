package com.alexgower.odin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Topic {
    Context context;
    String topicName;
    Bitmap topicImage;
    ArrayList<String> questionArray = new ArrayList<>();
    ArrayList<String> answerArray = new ArrayList<>();

    public Topic(String topicName,Bitmap topicImage, Context c){
        this.topicName = topicName;

        this.context = c;

        if(topicImage==null){
            this.topicImage = BitmapFactory.decodeResource(c.getResources(), R.drawable.topic_default);
        }else{
            this.topicImage = topicImage;
        }
    }

    public void save(Context c){
        try {
            FileOutputStream fos = c.openFileOutput(filenameOfTopic(topicName), Context.MODE_APPEND);

            for (int i = 0; i < questionArray.size(); i++) {
                String question = questionArray.get(i) + "\r\n";
                fos.write(question.getBytes());
                String answer = answerArray.get(i) + "\r\n";
                fos.write(answer.getBytes());
            }
            fos.close();

            String imageFileName = filenameOfTopic(topicName) + ".png";
            FileOutputStream fos2 = context.openFileOutput(imageFileName,Context.MODE_PRIVATE);
            topicImage.compress(Bitmap.CompressFormat.PNG, 100, fos2);
            fos.close();
        } catch(Exception e){
            Toast.makeText(c,topicName,Toast.LENGTH_LONG).show();
        }
    }


    public String filenameOfTopic(String topicNameIn){
        return topicNameIn.replace(' ','_');
    }

    public void addCard(String questionIn, String answerIn){
        questionArray.add(questionIn);
        answerArray.add(answerIn);
    }
}
