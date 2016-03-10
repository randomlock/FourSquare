package com.example.randomlocks.foursquare;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomlocks.foursquare.Interface.PhotoInterface;
import com.example.randomlocks.foursquare.user.Login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener , PhotoInterface,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,ListView.OnItemClickListener{

    GoogleApiClient googleApiClient;
    Location mLastLocation;
    Toolbar toolbar;
    private ListView listView;
    CustomAdapter adapter;
    SearchView searchView;
    public ArrayList<ListElement> list;
    public ArrayList<PhotoList> photolist = new ArrayList<>();

 double Latitude;
   double Longitude;
    ImageView restaurant,dance,wine,coffee,market;


    /****************************************PARAMETER VARIABLE ********************************/

    String title;
    String phone;
    String rating;
    String tips;
    String address="";
    String id;
    int size;
    double distance;
    TextView bye1,bye2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        buildGoogleApiClient();
restaurant= (ImageView) findViewById(R.id.restaurant);
        restaurant.setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        restaurant.setOnClickListener(this);
        dance=(ImageView)findViewById(R.id.dance);
                dance.setOnClickListener(this);
        wine= (ImageView) findViewById(R.id.wine);
        wine.setOnClickListener(this);
        coffee = (ImageView) findViewById(R.id.coffee);
        coffee.setOnClickListener(this);
        market = (ImageView) findViewById(R.id.shopping);
        market.setOnClickListener(this);
        bye1= (TextView) findViewById(R.id.textView8);
        bye2= (TextView) findViewById(R.id.textView9);


        /************************** MATERIAL DESIGN *****************************/


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

/*****************************************FILLING THE LIST ********************************/

        list = new ArrayList<>();
 //       list.add(new ListElement("Science Musuem of the world", 7, "https://irs1.4sqi.net/img/general/100x100/YHDiM0MnvzpPDjsOWXjczuUFVnj418jLilD2Zy5Jt9c.jpg", String.valueOf(67867867), "F-70 , Abul fazal", "www.google.com", "this museum is very good and one of the best",2.3,"34"));
 //       list.add(new ListElement("Macdonalds", 8, "https://irs1.4sqi.net/img/general/100x100/YHDiM0MnvzpPDjsOWXjczuUFVnj418jLilD2Zy5Jt9c.jpg", String.valueOf(67867867), "G-16 , East of Kailash", "www.facebook.com", "Lots of stuff at good price",1.4,"234"));


/****************************************SETTING LIST VIEW ********************************/

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(this, list);
        listView.setAdapter(adapter);
listView.setFocusable(true);
        listView.setOnItemClickListener(this);

        //listView.setOnItemLongClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu; this adds items to the action bar if it is present.

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchme).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(new ComponentName(getApplicationContext(), MainActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint("Search Places");
        searchView.setSubmitButtonEnabled(true);
        searchView.setMaxWidth(10000);
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        bye1.setText("");
        bye2.setText("");
        searchView.clearFocus();
        list.clear();
        photolist.clear();

        if(isConnectedToInternet()){
            new MainTask().execute(query);

        } else {
            Toast.makeText(this,"Not Connected to Internet",Toast.LENGTH_LONG).show();

        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation =  LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        //  Log.d("first",String.valueOf(mLastLocation.getLatitude()));
        if(mLastLocation!=null){

            Latitude=mLastLocation.getLatitude();
            Longitude=mLastLocation.getLongitude();
       //     Toast.makeText(this,String.valueOf(Latitude)+" "+String.valueOf(Longitude),Toast.LENGTH_LONG).show();
        }

        else
            Toast.makeText(this,"No location Found",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,"Connection failed",Toast.LENGTH_LONG).show();

    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();




    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        String str;
        list.clear();
        switch (v.getId()){
            case R.id.dance :
                Toast.makeText(this,"Dance",Toast.LENGTH_SHORT).show();
                str="dance";
                break;

            case R.id.wine :
                Toast.makeText(this,"wine",Toast.LENGTH_SHORT).show();
                str="wine";
                break;
            case R.id.coffee :
                Toast.makeText(this,"coffee",Toast.LENGTH_SHORT).show();
                str="coffee";
                break;

            case R.id.shopping :
                Toast.makeText(this,"shopping",Toast.LENGTH_SHORT).show();
                str="shopping";
                break;

            case R.id.restaurant :
                Toast.makeText(this,"restaurant",Toast.LENGTH_SHORT).show();
                str="restaurant";
                break;

                default:str="";

        }

        onQueryTextSubmit(str);

    }


    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            NetworkInfo[] ainfo = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //   Toast.makeText(this,String.valueOf(list.get(position).getVENUEID()),Toast.LENGTH_LONG).show();
        String title = list.get(position).getTITLE();
        String venueid = list.get(position).getVENUEID();
        new GetVenueDetail(this,MainTask.CLIENT_ID,MainTask.CLIENT_PASSWORD,venueid,title,photolist,position).execute();






    }




    /**
     * ***************************ASYNC TASK ***********************************************
     */

    class MainTask extends AsyncTask<String, Void, Void> {

        public static final String CLIENT_ID="FKETQOHIPSZZ1XU4AMN2RQ020ADZDNTT3DKNA1WWVQSELZEW";
        public static final String CLIENT_PASSWORD="PESWB30LR2QD1L5H4NVWC4VGYRGXOEGGU44MPTO0E1LRXJY4";
        private static final String TAG = "mytag";
        private static final String RESPONSE = "response";
        public static final String VENUE="venues";
        String url;
        StringBuilder builder;
        JSONObject jsonObject;
        JSONArray jsonArray;


        @Override
        protected Void doInBackground(String... query) {
         String q[] = query[0].split(" ");
            String str=q[0];

            for (int i = 1; i <q.length ; i++) {

                str+="%20"+q[i];

            }

         url="https://api.foursquare.com/v2/venues/search?ll="+Latitude+","+Longitude+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_PASSWORD+"&v=20151110&limit=20&query="+str;
           builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);

            try {
                HttpResponse response = client.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200) {

                    InputStream content = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    content.close();
                } else {
cancel(true);
                }
            } catch (IOException e) {
                cancel(true);

            }


          try {
              jsonObject = new JSONObject(builder.toString());
          } catch (JSONException e) {
              Log.e("JSON Parser", "Error parsing data " + e.toString());
               return  null;

          }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
           InternetDialog dialog = new InternetDialog();
            dialog.show(getSupportFragmentManager(),"my dialog");
        }

        @Override
        protected void onPostExecute(Void aVoid) {






            list.clear();         //clear the array list
            jsonArray = new JSONArray();
            try {

               if(jsonObject!=null) {
                   if (jsonObject.has(RESPONSE) && jsonObject.getJSONObject(RESPONSE).has(VENUE)) {
                       jsonArray = jsonObject.getJSONObject(RESPONSE).getJSONArray(VENUE);
                   } else {
                   }
                   size = jsonArray.length();

                   for (int i = 0; i < jsonArray.length(); i++) {

                       if (jsonArray.getJSONObject(i).has("name")) {
                           title = jsonArray.getJSONObject(i).getString("name");
                       } else {
                           title = "no title";
                       }


                       if (jsonArray.getJSONObject(i).getJSONObject("contact").has("formattedPhone")) {
                           phone = jsonArray.getJSONObject(i).getJSONObject("contact").getString("formattedPhone");
                       } else {
                           phone = "";
                       }

                       if (jsonArray.getJSONObject(i).getJSONObject("stats").has("checkinsCount")) {
                           rating = jsonArray.getJSONObject(i).getJSONObject("stats").getString("checkinsCount");
                       } else {
                           rating = "unknown";
                       }

                       if (jsonArray.getJSONObject(i).getJSONObject("location").has("formattedAddress")) {
                           JSONArray jsonaddress = jsonArray.getJSONObject(i).getJSONObject("location").getJSONArray("formattedAddress");
                           for (int j = 0; j < jsonaddress.length(); j++) {
                               address += " " + jsonaddress.get(j);
                           }
                       } else {
                           address = "Unknown";
                       }

                       // String url = jsonArray.getJSONObject(i).getString("url");

                       ;
                       if (jsonArray.getJSONObject(i).getJSONObject("hereNow").has("summary")) {
                           tips = jsonArray.getJSONObject(i).getJSONObject("hereNow").getString("summary");
                       } else {
                           tips = "Unknown";
                       }


                       if (jsonArray.getJSONObject(i).has("id")) {
                           id = jsonArray.getJSONObject(i).getString("id");
                       } else {
                           id = "4ffb42b5e4b04f8b129fc1c4";
                       }


                       if (jsonArray.getJSONObject(i).has("location") && jsonArray.getJSONObject(i).getJSONObject("location").has("distance")) {
                           distance = jsonArray.getJSONObject(i).getJSONObject("location").getDouble("distance") / 1000;
                       } else {
                           distance = -1;
                       }


                       GetVenuePhoto photo = new GetVenuePhoto(title, phone, rating, address, tips, MainActivity.this, id, CLIENT_PASSWORD, CLIENT_ID, distance);
                       photo.delegate = MainActivity.this;
                       photo.execute();


                       //    Toast.makeText(MainActivity.this, tips, Toast.LENGTH_LONG).show();


/*****************************************FILLING THE LIST ********************************/
                       //  list.add(new ListElement(title, Integer.parseInt(rating), R.drawable.ic_search_black_24dp, phone, address, "abc", tips));
                       address = "";
                   }

/****************************************SETTING LIST VIEW ********************************/
                   //  adapter = new CustomAdapter(MainActivity.this, list);
                   //  listView.setAdapter(adapter);
               }

            } catch (JSONException e) {
                Log.e("sdf","On post execute error");

            }


        } //on post execut method


    } // inner class


    @Override
    public void processFinish(ArrayList<String> output, String title, String phone, String rating, String address, String tips,double distance,String venueid) {

        photolist.add(new PhotoList(output));
        list.add(new ListElement(title, Integer.parseInt(rating), output.get(0), phone, address, "abc", tips,distance,venueid));

       // if (list.size()==size) {
         //   adapter = new CustomAdapter(MainActivity.this, list);
            adapter.notifyDataSetChanged();
       // }

      //  FoursquareApi api = new FoursquareApi();
       // api.

    }


} //outer class

