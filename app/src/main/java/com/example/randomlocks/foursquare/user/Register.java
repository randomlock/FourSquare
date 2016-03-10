package com.example.randomlocks.foursquare.user;

/**
 * Created by randomlocks on 11/12/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.randomlocks.foursquare.R;


public class Register extends Activity{




    VivzAdapter viz;
    EditText Name , Email , User , Pass;
    Button register;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name=(EditText)findViewById(R.id.editText3);
        Email=(EditText)findViewById(R.id.editText4);
        User=(EditText)findViewById(R.id.editText5);
        Pass=(EditText)findViewById(R.id.editText6);
        register=(Button)findViewById(R.id.button3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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


    public void Reg(View v){

        viz=new VivzAdapter(this);

        if(Name.getText().toString().matches("")||Email.getText().toString().matches("")||User.getText().toString().matches("")||Pass.getText().toString().matches(""))
        {
            Toast.makeText(this,"Please Fill the form completely",Toast.LENGTH_LONG).show();

        }

        else if(viz.userCheck(User.getText().toString()))
        {
            Toast.makeText(this,"Username already Registered",Toast.LENGTH_SHORT).show();
        }





        else {

            Toast.makeText(this,"Successfully registered",Toast.LENGTH_LONG).show();
            Intent it = new Intent(Register.this,Login.class);
            startActivity(it);

            //have to save the record in the database;

            String username=User.getText().toString();
            String password=Pass.getText().toString();
            viz=new VivzAdapter(this);

            long id = viz.InsertQuery(username,password);
            if(id<0)
            {
               // Toast.makeText(this,"Error during insertion",Toast.LENGTH_SHORT).show();
            }

            // else {
            //     Toast.makeText(this,"Successfully inserted",Toast.LENGTH_SHORT).show();
            // }




        } //else

    } //function


}

