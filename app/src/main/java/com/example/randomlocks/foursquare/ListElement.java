package com.example.randomlocks.foursquare;

/**
 * Created by randomlocks on 11/9/2015.
 */
public class ListElement {

    private String TITLE;
    private int RATING;
    private String IMAGE;
    private String PHONE;
    private String ADDRESS;
    private String URL;
    private String TIPS;
    private String VENUEID;
    private double DISTANCE;

    public ListElement(String TITLE, int RATING, String IMAGE, String PHONE, String ADDRESS, String URL, String TIPS,double DISTANCE,String VENUEID) {
        this.TITLE = TITLE;
        this.RATING = RATING;
        this.IMAGE = IMAGE;
        this.PHONE = PHONE;
        this.ADDRESS = ADDRESS;
        this.URL = URL;
        this.TIPS = TIPS;
        this.DISTANCE=DISTANCE;
        this.VENUEID=VENUEID;
    }

    public String getVENUEID() {
        return VENUEID;
    }

    public double getDISTANCE() {
        return DISTANCE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getURL() {
        return URL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public int getRATING() {
        return RATING;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public String getTIPS() {
        return TIPS;
    }

    public String getPHONE() {
        return PHONE;
    }
}
