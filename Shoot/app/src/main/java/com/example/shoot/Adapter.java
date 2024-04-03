package com.example.shoot;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {

    private ArrayList<Model>mExampleList;
    Context context;



    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView textView2;
        public TextView textView3;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }

   public Adapter(ArrayList<Model>exampleList){
        mExampleList=exampleList;

   }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        Model currentItem = mExampleList.get(position);
        holder.textView.setText(currentItem.getTex1());
        holder.textView2.setText(currentItem.getTex2());
        holder.textView3.setText(currentItem.getTex3());


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
