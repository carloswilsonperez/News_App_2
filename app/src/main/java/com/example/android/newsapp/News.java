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

    public Bitmap getmThumbnailBitmap() {
        return mThumbnailBitmap;
    }

    public void setmThumbnailBitmap(Bitmap bitmapNews) {
        mThumbnailBitmap = bitmapNews;
    }
}
