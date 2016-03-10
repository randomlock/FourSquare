package com.example.randomlocks.foursquare;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.randomlocks.foursquare.Interface.PhotoInterface;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by randomlocks on 11/11/2015.
 */
public class GetVenuePhoto extends AsyncTask<Void,Void,JSONObject> {

    String CLIENT_ID;
    String CLIENT_PASSWORD;
    String VENUE_ID;
    ArrayList<String> photolist = new ArrayList<>();
    public static final String RESPONSE="response";
    public static final String PHOTOS="photos";
    public static final String ITEMS="items";
    Context context;
    public PhotoInterface delegate = null;
    String title;
    String phone;
    String rating;
    String address;
    String tips;
    double distance;


    public GetVenuePhoto(String title, String phone, String rating, String address, String tips, Context context,String VENUE_ID, String CLIENT_PASSWORD, String CLIENT_ID,double distance) {
        this.title = title;
        this.phone = phone;
        this.rating = rating;
        this.address = address;
        this.tips = tips;
        this.context = context;
        this.VENUE_ID = VENUE_ID;
        this.CLIENT_PASSWORD = CLIENT_PASSWORD;
        this.CLIENT_ID = CLIENT_ID;
        this.distance=distance;
    }





    @Override
    protected JSONObject doInBackground(Void... params) {

        String url = "https://api.foursquare.com/v2/venues/"+VENUE_ID+"/photos?v=20151111&client_id="+CLIENT_ID+"&client_secret="+CLIENT_PASSWORD;

        try {
            JSONObject photosJson = executeHttpGet(url);
            return photosJson;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }

    @Override
    protected void onPostExecute(JSONObject photosJson) {
        //Toast.makeText(context,photosJson.toString(),Toast.LENGTH_LONG).show();
        try {

            if (photosJson.has(RESPONSE)) {
                JSONObject object = photosJson.getJSONObject(RESPONSE);
                if (object.has(PHOTOS)) {
                    JSONObject photos = object.getJSONObject(PHOTOS);
                    if (photos.has(ITEMS)) {
                        JSONArray array = photos.getJSONArray(ITEMS);

                      if(array.length()>0) {
                          for (int i = 0; i < array.length(); i++) {


                              JSONObject ob = array.getJSONObject(i);
                              String photo = null;
                              if (ob.has("prefix") && ob.has("suffix")) {
                                  photo = ob.getString("prefix") + "100x100" + ob.getString("suffix");
                                  photolist.add(photo);
                              }
                              //  Toast.makeText(context,photo,Toast.LENGTH_LONG).show();


                          }
                      }

                    } else {
                     //   Toast.makeText(context,"OOPS 1 now",Toast.LENGTH_LONG).show();
                    }
                } else {
                   // Toast.makeText(context,"OOPS 2 now",Toast.LENGTH_LONG).show();
                }
            } else {
               // Toast.makeText(context,"OOPS 3 now",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
if(photolist.size()==0){
    photolist.add("https://irs1.4sqi.net/img/general/100x100/YHDiM0MnvzpPDjsOWXjczuUFVnj418jLilD2Zy5Jt9c.jpg");
}

        delegate.processFinish(photolist,title,phone,rating,address,tips,distance,VENUE_ID);

    }    //onpost execute


    private JSONObject executeHttpGet(String uri) throws Exception {
        HttpGet req = new HttpGet(uri);

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
    }


}
