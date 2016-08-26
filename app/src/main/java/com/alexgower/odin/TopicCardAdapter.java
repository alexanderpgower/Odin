package com.alexgower.odin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TopicCardAdapter extends RecyclerView.Adapter<TopicCardAdapter.TopicCardViewHolder> {

    protected List<TopicCardInfo> topicCardList;

    public TopicCardAdapter(List<TopicCardInfo> topicCardList) {
        this.topicCardList = topicCardList;
    }

    @Override
    public int getItemCount() {
        return topicCardList.size();
    }

    @Override
    public void onBindViewHolder(TopicCardViewHolder topicCardViewHolder, int i) {
        TopicCardInfo ci = topicCardList.get(i);
        topicCardViewHolder.vTopicName.setText(ci.topicName);


    }

    @Override
    public TopicCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topic_card_layout, viewGroup, false);
        return new TopicCardViewHolder(itemView);
    }


    public class TopicCardViewHolder extends RecyclerView.ViewHolder {
        public View view;

        protected TextView vTopicName;

        public TopicCardViewHolder(View v) {
            super(v);

            vTopicName =  (TextView) v.findViewById(R.id.topicNameTextView);

            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    vTopicName.setText("Clicked");

                }

            });

        }

    }

    public void removeAt(int position) {
        if(position>-1) {
            topicCardList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, topicCardList.size());
        }
    }


}