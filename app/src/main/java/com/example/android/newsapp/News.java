package com.example.android.newsapp;


import android.graphics.Bitmap;

public class News {

    private String mSectionName;
    private String mWebTitle;
    private String mContributor;
    private String mWebPublicationDate;
    private String mWebUrl;
    private String mThumbnail;
    private Bitmap mThumbnailBitmap;

    /**
     * Constructs a new {@Link News} object.
     * @param title is the title of the news item
     * @param contributor is the contributor of the news item
     * @param publicationDate is the date of publication
     * @param webUrl is the url for the news item
     * @param thumbnail is the url for the thumbnail of news item
     * @param thumbnailBitmap is the bitmap image of the news item
     */
    public News(String sectionName, String title, String contributor, String publicationDate, String webUrl, String thumbnail, Bitmap thumbnailBitmap) {
        mSectionName = sectionName;
        mWebTitle = title;
        mContributor = contributor;
        mWebPublicationDate = publicationDate;
        mWebUrl = webUrl;
        mThumbnail = thumbnail;
        mThumbnailBitmap = thumbnailBitmap;
    }

    /**
     * Getter for the mSectionName property
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Getter for the mWebTitle property
     */
    public String getWebTitle() {
        return mWebTitle;
    }

    /**
     * Getter for the mContributor property
     */
    public String getContributor() {
        return mContributor;
    }

    /**
     * Getter for the mWebPublicationDate property
     */
    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    /**
     * Getter for the mWebUrl property
     */
    public String getWebUrl() {
        return mWebUrl;
    }

    /**
     * Getter for the mThumbnail property
     */
    public String getThumbnail() {
        return mThumbnail;
    }

    /**
     * Getter for the mThumbnailBitmap property
     */
    public Bitmap getThumbnailBitmap() {
        return mThumbnailBitmap;
    }

    /**
     * Setter for the mThumbnailBitmap property
     */
    public void setThumbnailBitmap(Bitmap bitmapNews) {
        mThumbnailBitmap = bitmapNews;
    }
}
