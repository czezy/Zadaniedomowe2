package com.example.zadaniedomowe2v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnDetailsInteractionListener {

    public static String DATA_CHANGED_KEY = "dataSetChanged";
    private boolean imgChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imgChanged = false;
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(DATA_CHANGED_KEY, imgChanged);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public void setImgChanged(boolean val)
    {
        imgChanged = val;
    }

    @Override
    public void changeImage(Track track, String image)
    {
        MainActivity.trackList.get(track.getId()).setImage(image);
        setImgChanged(true);
    }

    @Override
    public void setPhotoPath(String photoPath) {

    }
}
