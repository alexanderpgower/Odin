package com.alexgower.odin;

import android.graphics.Bitmap;

public class TopicCardInfo {
    protected String topicName;
    protected Bitmap topicImage;

    public TopicCardInfo(String topicNameIn, Bitmap topicImageIn) {
        this.topicName = topicNameIn;
        this.topicImage = topicImageIn;
    }

    public TopicCardInfo(String topicNameIn) {
        this.topicName = topicNameIn;
    }

}