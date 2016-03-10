package com.example.randomlocks.foursquare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by randomlocks on 11/11/2015.
 */
public class GetVenueDetail extends AsyncTask<Void,Void,JSONObject> {

    Context context;
    private String TITLE;
    private String CLIENT_ID;
    private String CLIENT_PASSWORD;
    private String VENUE_ID;
    private String RATING;
    private ArrayList<String> reviews = new ArrayList<>();
    private ArrayList<String> photos;
    private ArrayList<PhotoList> photolist;
    private int position;

    public GetVenueDetail(Context context,String CLIENT_ID, String CLIENT_PASSWORD, String VENUE_ID,String TITLE,ArrayList<PhotoList> photolist,int position) {
        this.context = context;
        this.CLIENT_ID=CLIENT_ID;
        this.TITLE=TITLE;
        this.CLIENT_PASSWORD = CLIENT_PASSWORD;
        this.VENUE_ID = VENUE_ID;
        this.photolist=photolist;
        this.position=position;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {


        String url="https://api.foursquare.com/v2/venues/"+VENUE_ID+"?v=20151111&client_id="+CLIENT_ID+"&client_secret="+CLIENT_PASSWORD;

        try {
            JSONObject object = executeHttpGet(url);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onCancelled(JSONObject jsonObject) {
        Toast.makeText(context,"Check Your internet Connection",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {
            if (jsonObject.has("response")) {
                JSONObject object = jsonObject.getJSONObject("response");

                if (object.has("venue")) {
             //       Toast.makeText(context,"hello bhai",Toast.LENGTH_LONG).show();
                    JSONObject object1 = object.getJSONObject("venue");

                    if (object1.has("rating")) {

                        RATING = object1.getString("rating");
                    //    Toast.makeText(context,RATING,Toast.LENGTH_LONG).show();




                    } else {

                  //      Toast.makeText(context,"error3",Toast.LENGTH_LONG).show();
                        RATING = "6.8";
                    }

                    if (object1.has("tips") && object1.getJSONObject("tips").has("groups")) {
                        JSONArray array = object1.getJSONObject("tips").getJSONArray("groups");

                        if (array.length()>0 && array.getJSONObject(0).has("items")) {
                            JSONArray array1 = array.getJSONObject(0).getJSONArray("items");

                            for (int i = 0; i <array1.length() ; i++) {


                                JSONObject object2 = array1.getJSONObject(i);

                                if (object2.has("text")) {
                                    reviews.add(object2.getString("text"));
                                }


                            }

                      //      Toast.makeText(context,String.valueOf(reviews.size()),Toast.LENGTH_LONG).show();


                        } else {
                   //         Toast.makeText(context,"error1",Toast.LENGTH_LONG).show();
                        }


                    }else {
                      //  Toast.makeText(context,"error2",Toast.LENGTH_LONG).show();
                    }


                } else {
                   // Toast.makeText(context,"error4",Toast.LENGTH_LONG).show();
                }


            } else {
             //   Toast.makeText(context,"error5",Toast.LENGTH_LONG).show();
            }

            //Toast.makeText(context,TITLE+RATING+reviews.get(0),Toast.LENGTH_LONG).show();
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", TITLE);
            bundle.putString("RATING", RATING);
         //   bundle.putInt("POSITION",position);
            bundle.putStringArrayList("REVIEW",reviews);

          photos = photolist.get(position).getPhotolist();
            bundle.putStringArrayList("PHOTOS",photos);

            Intent it = new Intent(context,VenueDetail.class);
            it.putExtras(bundle);
            context.startActivity(it);



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private JSONObject executeHttpGet(String uri)  {
        HttpGet req = new HttpGet(uri);

        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse resLogin = client.execute(req);
            BufferedReader r = new BufferedReader(new InputStreamReader(resLogin
                    .getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = r.readLine()) != null) {
                sb.append(s);
            }

            return new JSONObject(sb.toString());
        } catch (IOException e) {
            cancel(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }








}
