package com.example.android.myapplication2222222;


public class News {
//    private double mMagnitude;
//    private String mLocation;
//    private Long mTimeInMilliseconds;
//    private String mUrl;

    private String mSectionName;
    private String mWebTitle;
    private String mContributor;
    private String mWebPublicationDate;
    private String mWebUrl;
    private String mThumbnail;

    /**
     * Constructs a new {@Link News} object.
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the city location of the earthquake
     * @param time is the date the earthquake happened, in milliseconds
     * @param url is the url for the earthquake
     */
    /*public News(double magnitude, String location, long time, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
    }*/

    /**
     * Constructs a new {@Link News} object.
     * @param title is the magnitude (size) of the earthquake
     * @param contributor is the city location of the earthquake
     * @param publicationDate is the date the earthquake happened, in milliseconds
     * @param webUrl is the url for the earthquake
     * @param thumbnail is the url for the earthquake
     */
    public News(String sectionName, String title, String contributor, String publicationDate, String webUrl, String thumbnail) {
        mSectionName = sectionName;
        mWebTitle = title;
        mContributor = contributor;
        mWebPublicationDate = publicationDate;
        mWebUrl = webUrl;
        mThumbnail = thumbnail;
    }

    /*public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation () {
        return mLocation;
    }

    public Long getTimeInMilliseconds () {
        return mTimeInMilliseconds;
    }

    public String getUrlForEarthquake () {
        return mUrl;
    }*/

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmContributor() {
        return mContributor;
    }

    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }
}
