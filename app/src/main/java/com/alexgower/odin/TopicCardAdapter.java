package com.alexgower.odin;




import android.content.Context;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class TopicCardAdapter extends RecyclerView.Adapter<TopicCardAdapter.TopicCardViewHolder> {

    protected List<TopicCardInfo> topicCardList;
    protected Context context;
    TopicClickListener topicClickListener;

    public TopicCardAdapter(List<TopicCardInfo> topicCardList, TopicClickListener topicClickListener) {
        this.topicCardList = topicCardList;
        this.topicClickListener = topicClickListener;
    }

    @Override
    public int getItemCount() {
        return topicCardList.size();
    }

    @Override
    public void onBindViewHolder(TopicCardViewHolder topicCardViewHolder, int i) {
        TopicCardInfo ci = topicCardList.get(i);
        topicCardViewHolder.vTopicName.setText(ci.topicName);
        topicCardViewHolder.vTopicImage.setImageBitmap(ci.topicImage);
    }

    @Override
    public TopicCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topic_card_layout, viewGroup, false);
        return new TopicCardViewHolder(itemView);
    }


    public class TopicCardViewHolder extends RecyclerView.ViewHolder {
        public View view;

        protected TextView vTopicName;
        protected ImageView vTopicImage;

        public TopicCardViewHolder(View v) {
            super(v);

            vTopicName =  (TextView) v.findViewById(R.id.topicNameTextView);
            vTopicImage = (ImageView) v.findViewById(R.id.topicImageView);

            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String clickedTopicName = vTopicName.getText().toString();
                    topicClickListener.callback(clickedTopicName);

                }

            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    removeAt(getAdapterPosition());
                    return true;

                }

                });

        }

    }

    public void removeAt(int position) {
        if(position>-1) {
            String topicNameString = topicCardList.get(position).topicName;
            String topicFileName = topicNameString.replace(' ','_');;
            boolean deletedTopic = new File(context.getFilesDir() + "/" + topicFileName).delete();
            boolean deletedImage = new File(context.getFilesDir() + "/" + topicFileName + ".png").delete();
            if(deletedTopic&&deletedImage){
                Toast.makeText(context,"Deleted " + topicNameString,Toast.LENGTH_SHORT).show();
            }

            topicCardList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, topicCardList.size());
        }
    }

    public void giveContext(Context c){
        this.context = c;
    }

}