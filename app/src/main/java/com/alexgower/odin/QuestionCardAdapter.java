package com.alexgower.odin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class QuestionCardAdapter extends RecyclerView.Adapter<QuestionCardAdapter.QuestionCardViewHolder> {

    protected List<QuestionCardInfo> questionCardList;

    public QuestionCardAdapter(List<QuestionCardInfo> questionCardList) {
        this.questionCardList = questionCardList;
    }

    @Override
    public int getItemCount() {
        return questionCardList.size();
    }

    @Override
    public void onBindViewHolder(QuestionCardViewHolder questionCardViewHolder, int i) {
        QuestionCardInfo ci = questionCardList.get(i);
        questionCardViewHolder.vQuestionAnswer.setText(ci.question);
        questionCardViewHolder.answer = ci.answer;


        questionCardViewHolder.vTick.setVisibility(View.INVISIBLE);
        questionCardViewHolder.vCross.setVisibility(View.INVISIBLE);

    }

    @Override
    public QuestionCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_card_layout, viewGroup, false);
        return new QuestionCardViewHolder(itemView);
    }


    public class QuestionCardViewHolder extends RecyclerView.ViewHolder {
        public View view;

        protected TextView vQuestionAnswer;
        protected ImageView vTick;
        protected ImageView vCross;
        protected String answer = "Error";

        public QuestionCardViewHolder(View v) {
            super(v);

            vQuestionAnswer =  (TextView) v.findViewById(R.id.questionAnswerTextView);
            vTick = (ImageView) v.findViewById(R.id.tickImage);
            vCross = (ImageView) v.findViewById(R.id.crossImage);

            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    vQuestionAnswer.setText(answer);
                    vTick.setVisibility(View.VISIBLE);
                    vCross.setVisibility(View.VISIBLE);

                }

            });

            view = vTick;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                }
            });

            view = vCross;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                }

            });


        }

    }

    public void removeAt(int position) {
        if(position>-1) {
            questionCardList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, questionCardList.size());
        }
    }


}