package com.example.zadaniedomowe2v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddActivity extends AppCompatActivity implements AddFragment.OnAddInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    public void addTrack(Track newTrack) {
        Intent intent = new Intent();
        intent.putExtra("track", newTrack);
        setResult(RESULT_OK, intent);
        finish();
    }
}
