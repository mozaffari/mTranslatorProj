package dev.mozaffari.mtranslator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import dev.mozaffari.mtranslator.R;
import dev.mozaffari.mtranslator.models.Translation;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    OnItemClickListener onItemClickListener;
    Context context;
    List<Translation> translations;


    public HistoryAdapter(List<Translation> translations, Context context) {
        this.translations = translations;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final MyViewHolder holder = myViewHolder;
        Translation translation = translations.get(i);

        holder.tvTranslateFrom.setText(translation.getOrignalText());
        holder.tvTranslateTo.setText(translation.getTranslatedText());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener onItemClickListener;

        ImageView ivDelete;
        TextView tvTranslateFrom,tvTranslateTo;




        public MyViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {

            super(itemView);
            itemView.setOnClickListener(this);

            tvTranslateFrom = itemView.findViewById(R.id.tv_from);
            tvTranslateTo = itemView.findViewById(R.id.tv_to);
            ivDelete =itemView.findViewById(R.id.ic_delete);

            ivDelete.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;


        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,getAdapterPosition());
//            if(v.getId() == R.id.ic_delete)
//                deletHistory(translations.get(getAdapterPosition()).getId());
        }
    }

    private void deletHistory(int id) {
        Toast.makeText(context, "Going to delete history by id of "+id, Toast.LENGTH_SHORT).show();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }
}
