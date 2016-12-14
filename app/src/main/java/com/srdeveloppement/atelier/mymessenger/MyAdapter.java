package com.srdeveloppement.atelier.mymessenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam on 06/03/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Discution> mDiscution;
    Drawable drawable;
    Context c;
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View View;
        public ViewHolder(View itemView) {
            super(itemView);
            View = itemView;

        }
    }
    private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();


    public MyAdapter(ArrayList<Discution> dicus) {
        this.mDiscution = dicus;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final FrameLayout myItem=(FrameLayout)holder.View.findViewById(R.id.myItem);
        TextView senderTv = (TextView) holder.View.findViewById(R.id.senderText);
        TextView reciverTv = (TextView) holder.View.findViewById(R.id.reciverText);
        TextView senderTime = (TextView) holder.View.findViewById(R.id.senderTime);
        TextView reciverTime = (TextView) holder.View.findViewById(R.id.reciverTime);
        EditText area=(EditText)holder.View.findViewById(R.id.my_edit_text);
        ImageView img = (ImageView)holder.View.findViewById(R.id.Simg) ;
        Button send = (Button)holder.View.findViewById(R.id.send);

        RelativeLayout SRl=(RelativeLayout)holder.View.findViewById(R.id.SRl);
        RelativeLayout RRl=(RelativeLayout)holder.View.findViewById(R.id.RRl);

        String s1=mDiscution.get(position).getSenderText();

        Editable s=new SpannableStringBuilder(s1);

        c=holder.View.getContext();
        emoticons.put(":tongue:", R.drawable.em2);
        emoticons.put(":glasses:", R.drawable.em3);
        emoticons.put(":sad:", R.drawable.em4);
        emoticons.put(":angry:", R.drawable.em5);
        emoticons.put(":laugh:", R.drawable.em6);
        emoticons.put(":happy:", R.drawable.em7);

        senderTv.setText(getSmiledText(s1.toString()));


        String r1=mDiscution.get(position).getReciverText();
        reciverTv.setText(getSmiledText(r1.toString()));

//        if(mDiscution.get(position).getImagePath().getName().split(".",2).equals("png") ||mDiscution.get(position).getImagePath().getName().split(".",2).equals("jpg")){
        if(mDiscution!=null){
            if(mDiscution.get(position).getImagePath().exists()){
                img.setVisibility(View.VISIBLE);
                String path = mDiscution.get(position).getImagePath().getAbsolutePath();
                Bitmap myBitmap = BitmapFactory.decodeFile(path);
                img.setImageBitmap(myBitmap);
            }
        }



        if(mDiscution.get(position).senderVS==true) {
            SRl.setVisibility(View.VISIBLE);
            senderTv.setBackgroundResource(R.drawable.s);
            RRl.setVisibility(View.GONE);

            int H=mDiscution.get(position).getC().get(Calendar.HOUR_OF_DAY);
            int M=mDiscution.get(position).getC().get(Calendar.MINUTE);
            String MM=""+M;
            if(M<=9 ){MM="0"+MM;}
            senderTime.setText(H + ":" + MM);
        }
        if(mDiscution.get(position).reciverVS == true) {
            RRl.setVisibility(View.VISIBLE);
            reciverTv.setBackgroundResource(R.drawable.r);
            SRl.setVisibility(View.GONE);
            int H=mDiscution.get(position).getC().get(Calendar.HOUR_OF_DAY);
            int M=mDiscution.get(position).getC().get(Calendar.MINUTE);
            String MM=""+M;
            if(M<=9 ){MM="0"+MM;}
            reciverTime.setText(H + ":" + MM);

        }



    }

    @Override
    public int getItemCount() {
        return mDiscution.size();
    }

    public Spannable getSmiledText(String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (emoticons.size() > 0) {
            int index;
            for (index = 0; index < builder.length(); index++) {
                if (Character.toString(builder.charAt(index)).equals(":")) {
                    for (Map.Entry<String, Integer> entry : emoticons.entrySet()) {
                        int length = entry.getKey().length();
                        if (index + length > builder.length())
                            continue;
                        if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
                            builder.setSpan(new ImageSpan(c, entry.getValue()), index, index + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            index += length - 1;
                            break;
                        }
                    }
                }
            }
        }
        return builder;
    }

    public void  loadNewData(ArrayList<Discution> Disc){
        mDiscution = Disc;
        notifyDataSetChanged();
    }

    public ArrayList<Discution> getmDiscution() {
        return mDiscution;
    }
}