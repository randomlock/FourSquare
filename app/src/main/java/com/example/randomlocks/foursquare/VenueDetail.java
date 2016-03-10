package com.example.randomlocks.foursquare;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;


public class VenueDetail extends AppCompatActivity {

    Toolbar toolbar;
    TextView Title,noreview;
    Button Rating;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> photos;
    SmartImageView image1,image2,image3;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);
        toolbar= (Toolbar) findViewById(R.id.mytoolbar2);
        Title= (TextView) findViewById(R.id.mytext);
        Rating= (Button) findViewById(R.id.mybutton);
        image1= (SmartImageView) findViewById(R.id.imageView11);
        image2= (SmartImageView) findViewById(R.id.imageView12);
        image3= (SmartImageView) findViewById(R.id.imageView13);
        listView = (ListView) findViewById(R.id.listView2);
        noreview= (TextView) findViewById(R.id.textView6);
        Rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VenueDetail.this,"Rating",Toast.LENGTH_SHORT).show();
            }
        });
      setSupportActionBar(toolbar);
       android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);


        /************************** MATERIAL DESIGN *****************************/


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        if (getIntent().getExtras().getString("TITLE")!=null) {
            Title.setText(getIntent().getExtras().getString("TITLE"));
        }
        ArrayList<String> list= null;
        if (getIntent().getExtras().getStringArrayList("REVIEW")!=null) {
            list = getIntent().getExtras().getStringArrayList("REVIEW");
        }
        if(list.isEmpty()){
            noreview.setText("No Reviews/Tips");
        }
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        if (getIntent().getExtras().getString("RATING")!=null) {
            Rating.setText(getIntent().getExtras().getString("RATING"));

            if(Double.parseDouble((String) Rating.getText())>8.5){
                Rating.setBackgroundColor(Color.parseColor("#ff4e61ff"));
            }

            else if(Double.parseDouble((String) Rating.getText())>6){
                Rating.setBackgroundColor(Color.parseColor("#fffaff6d"));
            }

            else if(Double.parseDouble((String) Rating.getText())>3){
                Rating.setBackgroundColor(Color.parseColor("#ffff282c"));
            }


        }

        if (getIntent().getExtras().getStringArrayList("PHOTOS")!=null) {
            photos=getIntent().getExtras().getStringArrayList("PHOTOS");
        }

        if (photos.size()>0) {
            image1.setImageUrl(photos.get(0),R.drawable.defaultphoto);
        }

        else {
            image1.setImageResource(R.drawable.defaultphoto);
        }
        if (photos.size()>1) {
            image2.setImageUrl(photos.get(1),R.drawable.defaultphoto);
        }else {
            image1.setImageResource(R.drawable.defaultphoto);
        }
        if (photos.size()>2) {
            image3.setImageUrl(photos.get(2), R.drawable.defaultphoto);
        }else {
            image1.setImageResource(R.drawable.defaultphoto);
        }


    } //oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_venue_detail, menu);
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
}
