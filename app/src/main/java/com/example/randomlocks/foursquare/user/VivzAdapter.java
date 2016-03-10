package com.example.randomlocks.foursquare.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by randomlocks on 11/12/2015.
 */
public class VivzAdapter {

    VivzHelper helper;
    Context context;

    //constructor
    VivzAdapter(Context context)
    {
        helper=new VivzHelper(context);
        this.context=context;
    }

    //method

    public long InsertQuery(String name,String pass){

        ContentValues cv = new ContentValues();

        cv.put(VivzHelper.NAME,name);
        cv.put(VivzHelper.PASS,pass);

        SQLiteDatabase db = helper.getWritableDatabase();
        long id=db.insert(VivzHelper.DATABASE_TABLE,null,cv);
        return id;

    }



    public String SelectQuery()
    {

        SQLiteDatabase db = helper.getWritableDatabase();
        String column[]={VivzHelper.UID,VivzHelper.NAME,VivzHelper.PASS};
        Cursor cursor =db.query(VivzHelper.DATABASE_TABLE, column, null, null, null, null, null);
        StringBuffer str=new StringBuffer();
        while (cursor.moveToNext())
        {

            int index = cursor.getInt(cursor.getColumnIndex(VivzHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(VivzHelper.NAME));
            String pass = cursor.getString(cursor.getColumnIndex(VivzHelper.PASS));
            str.append(index +" " +name +" " +pass +"\n");



        }

        return  str.toString();

    }


    public String selectLogin(String user , String pass) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String column[] = {VivzHelper.UID, VivzHelper.NAME, VivzHelper.PASS};
        Cursor cursor = db.query(VivzHelper.DATABASE_TABLE, column, null, null, null, null, null);
        StringBuffer str = new StringBuffer();
        String pas;
        int flag=0;
        while (cursor.moveToNext()) {


            String name = cursor.getString(cursor.getColumnIndex(VivzHelper.NAME));


            if (name.equals(user)) {
                pas = cursor.getString(cursor.getColumnIndex(VivzHelper.PASS));
                flag=1;

                if (pas.equals(pass)) {
                    str.append(name + " " + pas + "\n");

                    break;
                }

                else {

                    Toast.makeText(context, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    break;
                }



            }




        }

        if(flag==0)
            Toast.makeText(context,"No Record Found",Toast.LENGTH_SHORT).show();

        return str.toString(); //as str is object

    }

    public boolean userCheck(String user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String column[] = {VivzHelper.UID, VivzHelper.NAME, VivzHelper.PASS};
        Cursor cursor = db.query(VivzHelper.DATABASE_TABLE, column, null, null, null, null, null);
        StringBuffer str = new StringBuffer();

        boolean flag=false;
        while (cursor.moveToNext()) {


            String name = cursor.getString(cursor.getColumnIndex(VivzHelper.NAME));


            if (name.equals(user)) {
                flag=true;
                break;



            }




        }



        return flag;

    }



    //inner class
    class VivzHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "VivzDatabase";
        private static final String DATABASE_TABLE = "VIVZTABLE";
        private static final int DATABASE_VERSION = 26;
        private static final String UID = "_id";
        private static final String PASS = "Password";
        private static final String NAME = "Name";
        private static final String SELECT = "Select "+UID+"," +NAME +"," +PASS+ " from "+DATABASE_TABLE;
        private static final String CREATE_TABLE = "CREATE TABLE "+DATABASE_TABLE+"("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" VARCHAR(80),"+PASS+" VARCHAR(70));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

        public VivzHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            //creating table
            try {
                db.execSQL(CREATE_TABLE);
              //  Toast.makeText(context,"executed on create",Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
               // Toast.makeText(context,"Not creating a table",Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
             //   Toast.makeText(context,"executed on upgrade",Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
              //  Toast.makeText(context,"Not creating a table",Toast.LENGTH_SHORT).show();

            }
            onCreate(db);

        }
    }



}
