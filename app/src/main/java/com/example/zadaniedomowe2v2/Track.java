package com.example.zadaniedomowe2v2;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    private String name, artist, year, image;
    private int id;

    public Track() {
    }

    public Track(String name, String artist, String year, String image, int id) {
        this.name = name;
        this.artist = artist;
        this.year = year;
        int temp = (int) (Math.random()*3);
        if(image==null) {
            switch (temp % 3) {
                case 0:
                    this.image = "drawable 1";
                    break;
                case 1:
                    this.image = "drawable 2";
                    break;
                case 2:
                    this.image = "drawable 3";
                    break;
                default:
                    this.image = "drawable 1";
                    break;
            }
        }
        else
        {
            this.image = image;
        }
        this.id = id;
    }

    protected Track(Parcel in) {
        name = in.readString();
        artist = in.readString();
        year = in.readString();
        image = in.readString();
        id = in.readInt();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getName() { return name; }

    public String getArtist() { return artist; }

    public String getYear(){
        return year;
    }

    public String getImage(){
        return image;
    }

    public int getId() { return id; }

    public void setName(String name){
        this.name = name;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public void setYear(String year){
        this.year = year;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setId(int id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(year);
        dest.writeString(image);
        dest.writeInt(id);
    }
}

