package com.example.randomlocks.foursquare.user;

/**
 * Created by randomlocks on 11/12/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.randomlocks.foursquare.MainActivity;
import com.example.randomlocks.foursquare.R;


public class Login extends Activity {
    VivzAdapter vivz;

    EditText name,pass;
    Button save,next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(EditText)findViewById(R.id.editText);
        pass=(EditText)findViewById(R.id.editText2);
        save=(Button)findViewById(R.id.button);
        next=(Button)findViewById(R.id.register);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



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


    public void login(View v)
    {
        vivz=new VivzAdapter(this);
        //first check if the record is in the database
        String user=name.getText().toString();
        String password = pass.getText().toString();
        // Toast.makeText(this,user+password,Toast.LENGTH_SHORT).show();
        String str=vivz.selectLogin(user, password);
        //    String str1=vivz.SelectQuery();
        //   Toast.makeText(this,str1,Toast.LENGTH_SHORT).show();
        if(str.matches(""))
        {
            //   Toast.makeText(this,"in the IF loop",Toast.LENGTH_SHORT).show();
        }

        else
        {

            //  Toast.makeText(this,"in the ELSE loop",Toast.LENGTH_SHORT).show();
            Intent it1 = new Intent(this,MainActivity.class);
            it1.putExtra("1",str);
            startActivity(it1);
        }




    }

    public void next(View v)
    {
        //vivz=new VivzAdapter(this);
//String data=vivz.SelectQuery();

        //Toast.makeText(this,data,Toast.LENGTH_LONG).show();


        Intent it = new Intent(Login.this,Register.class);
        startActivity(it);

    }

}

