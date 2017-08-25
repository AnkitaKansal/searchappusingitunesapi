package com.myapp.ankita.demoapplication;

/**
 * Created by ankita on 8/2/2017.
 */

public class JSONData {

    private String artworkUrl60;
    private String trackName;
    private String artistName;
    private String primaryGenreName;
    private String trackTimeMillis;
    private String trackPrice;
    private String previewUrl;

    public String getArtworkUrl60(){
        return artworkUrl60;
    }
    public void setArtworkUrl60(String artworkUrl60){
        this.artworkUrl60=artworkUrl60;
    }

    public String getTrackName(){
        return trackName;
    }
    public void setTrackName(String trackName){
        this.trackName=trackName;
    }

    public String getArtistName(){
        return artistName;
    }
    public void setArtistName(String artistName){
        this.artistName=artistName;
    }

    public String getPrimaryGenreName(){
        return primaryGenreName;
    }
    public void setPrimaryGenreName(String primaryGenreName){
        this.primaryGenreName=primaryGenreName;
    }

    public String getTrackTimeMillis(){
        return trackTimeMillis;
    }
    public void setTrackTimeMillis(String trackTimeMillis){
        this.trackTimeMillis=trackTimeMillis;
    }

    public String getTrackPrice(){
        return trackPrice;
    }
    public void setTrackPrice(String trackPrice){
        this.trackPrice=trackPrice;
    }

    public String getPreviewUrl(){
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }

    public JSONData(String artworkUrl60, String trackName, String artistName, String primaryGenreName, String trackTimeMillis, String trackPrice, String previewUrl){
        this.artworkUrl60=artworkUrl60;
        this.trackName=trackName;
        this.artistName=artistName;
        this.primaryGenreName=primaryGenreName;
        this.trackTimeMillis=trackTimeMillis;
        this.trackPrice=trackPrice;
        this.previewUrl = previewUrl;
    }
}
