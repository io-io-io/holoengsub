package umn.ac.id.holoengsub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.VideoDetails;
import umn.ac.id.holoengsub.R;
import umn.ac.id.holoengsub.VideoPlay;

public class MyCustomAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;
    LayoutInflater inflater;


    public MyCustomAdapter(Activity activity, ArrayList<VideoDetails> videoDetailsArrayList) {
        this.activity = activity;
        this.videoDetailsArrayList  = videoDetailsArrayList;
    }

    @Override
    public int getCount() {
        return this.videoDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.videoDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater = this.activity.getLayoutInflater();
        }

        if(convertView == null){
            convertView = inflater.inflate(R.layout.custom_item,null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageViewVideo);
        TextView textView = (TextView)convertView.findViewById(R.id.videotitle);
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.root);
        VideoDetails videoDetails = (VideoDetails)this.videoDetailsArrayList.get(position);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VideoPlay.class);
                i.putExtra("videoId",videoDetails.getVideoId());
                activity.startActivity(i);
            }
        });


        Picasso.get().load(videoDetails.getUrl()).into(imageView);
        textView.setText(videoDetails.getTitle());
        return convertView;
    }
}
