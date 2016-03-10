package com.example.randomlocks.foursquare;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;



/**
 * Created by randomlocks on 11/9/2015.
 */
public class CustomAdapter extends ArrayAdapter {

private ArrayList<ListElement> list;
 Context context;
    private TextView TITLE,TIPS,PHONE,URL,ADDRESS,DISTANCE;
    private Button RATING;
    SmartImageView IMAGE;




 /******************************************CONSTRUCTOR *****************************************/
public CustomAdapter(Context context , ArrayList<ListElement> list){
    super(context,R.layout.venue_search,list);
    this.context=context;
    this.list=list;

}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            LayoutInflater l=LayoutInflater.from(getContext());
            v = l.inflate(R.layout.venue_search, parent, false);
        }

        TITLE= (TextView) v.findViewById(R.id.title);
        PHONE = (TextView) v.findViewById(R.id.phone);
        RATING= (Button) v.findViewById(R.id.rating);
        IMAGE = (SmartImageView) v.findViewById(R.id.my_image);
    //    URL = (TextView) v.findViewById(R.id.url);
        ADDRESS = (TextView) v.findViewById(R.id.address);
        DISTANCE=(TextView)v.findViewById(R.id.distance);
        RATING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"CheckIn Count",Toast.LENGTH_SHORT).show();
            }
        });


        ListElement element = list.get(position);

        TITLE.setText(element.getTITLE());
        TITLE.setTextColor(Color.parseColor("#E91E63"));
       // TIPS.setText(element.getTIPS());
        PHONE.setText("Ph:"+element.getPHONE());
        if(PHONE.getText().toString().length()<5){
            PHONE.setText("");
        }
        RATING.setText(String.valueOf(element.getRATING()));
        if(element.getRATING()>200){
            RATING.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbup,0,0,0);
        } else {
            RATING.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumbsdown,0,0,0);
        }
        IMAGE.setImageUrl(element.getIMAGE());
     //   URL.setText(element.getURL());
        ADDRESS.setText(element.getADDRESS());

        if(element.getDISTANCE()!=-1){
            DISTANCE.setText(String.valueOf(element.getDISTANCE())+" km");
        }else {
            DISTANCE.setText("");
        }




        return v;
    }
}
