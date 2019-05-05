package com.example.zadaniedomowe2v2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.zadaniedomowe2v2.DetailsActivity.DATA_CHANGED_KEY;
import static com.example.zadaniedomowe2v2.DetailsFragment.REQUEST_IMAGE_CAPTURE_DET;

// TODO: orientation change

public class MainActivity extends AppCompatActivity implements TrackFragment.OnListFragmentInteractionListener, DetailsFragment.OnDetailsInteractionListener {

    private static final int ADD_MESS = 0;
    private static final int CAM_MESS = 3;
    private static final int DET_MESS = 2;
    private final String TRACKS_SHARED_PREFS = "tracks_shared_prefs";
    private final String NUM_TRACKS = "num_of_tracks";
    private final String NAME = "name_";
    private final String ARTIST = "artist_";
    private final String IMAGE = "image_";
    private final String YEAR = "year_";
    private final String ID = "id_";
    private Track currentTrack;
    private final String CURRENT_TRACK_KEY = "current_track";
    public static List<Track> trackList = new ArrayList<>();
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Track newCamTrack;
    private boolean tracksChanged;

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if(currentTrack != null) outState.putParcelable(CURRENT_TRACK_KEY, currentTrack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) currentTrack = savedInstanceState.getParcelable(CURRENT_TRACK_KEY);
        setContentView(R.layout.activity_main);

        restoreTracks();
        if(trackList.size() == 0) populateTrackList();
        tracksChanged = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if(currentTrack != null) displayTrackInFragment(currentTrack);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        saveTracks();
    }

    public void populateTrackList() {
        trackList.add(new Track("Jedwab", "Stachursky","2008", "drawable 1", trackList.size()));
        trackList.add(new Track("Doskozzzzza", "Stachursky", "2019", "drawable 2", trackList.size()));
        trackList.add(new Track("Chciałem być","Krzysztof Krawczyk", "2004", "drawable 3", trackList.size()));
    }

    @Override
    public void showDetails(Track track) {
        currentTrack = track;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            displayTrackInFragment(track);
        }
        else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("track", track);
            startActivityForResult(intent, DET_MESS);
        }
    }

    @Override
    public void resetTracks() {
        saveTracks();
        restoreTracks();
    }

    @Override
    public void deleteTrack(Track deletedTrack) {
        trackList.remove(deletedTrack);
        resetTracks();
    }

    public void addTrack(Track newTrack) {
        trackList.add(newTrack);
        resetTracks();
    }

    public boolean checkTracksChanged()
    {
        return tracksChanged;
    }

    @Override
    public void setTracksChanged(boolean val) {
        tracksChanged = val;
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, ADD_MESS);
    }

    public void onCamClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, CAM_MESS);
    }

    public void displayTrackInFragment(Track track)
    {
        DetailsFragment detailsFragment = ((DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.rightFragment));
        if(detailsFragment!=null)
        {
            detailsFragment.displayTrack(track);
        }
    }

    private void saveTracks()
    {
        SharedPreferences tracks = getSharedPreferences(TRACKS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = tracks.edit();
        editor.clear();
        editor.putInt(NUM_TRACKS, trackList.size());
        for(int i=0; i<trackList.size(); i++)
        {
            Track track = trackList.get(i);
            editor.putString(NAME+i, track.getName());
            editor.putString(ARTIST+i, track.getArtist());
            editor.putString(YEAR+i, track.getYear());
            editor.putString(IMAGE+i, track.getImage());
            editor.putInt(ID+i, i);
        }
        editor.apply();
    }

    public void clearList()
    {
        trackList.clear();
    }

    private void restoreTracks()
    {
        SharedPreferences tracks = getSharedPreferences(TRACKS_SHARED_PREFS, MODE_PRIVATE);
        int numOfTracks = tracks.getInt(NUM_TRACKS, 0);
        if(numOfTracks != 0)
        {
            clearList();
            for(int i=0; i<numOfTracks; i++)
            {
                String name = tracks.getString(NAME+i, "0");
                String artist = tracks.getString(ARTIST+i, "0");
                String year = tracks.getString(YEAR+i, "0");
                String image = tracks.getString(IMAGE+i, "0");
                trackList.add(new Track(name, artist, year, image, i));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            switch(requestCode)
            {
                case ADD_MESS:
                    Track newTrack = data.getParcelableExtra("track");
                    addTrack(newTrack);
                    setTracksChanged(true);
                    break;
                case DET_MESS:
                    if(data.getBooleanExtra(DATA_CHANGED_KEY, false))
                    {
                        resetTracks();
                        setTracksChanged(true);
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    newCamTrack.setImage(mCurrentPhotoPath);
                    addTrack(newCamTrack);
                    setTracksChanged(true);
                    break;
                case REQUEST_IMAGE_CAPTURE_DET:
                    currentTrack.setImage(mCurrentPhotoPath);
                    setTracksChanged(true);
                    break;
                case CAM_MESS:
                    newCamTrack = data.getParcelableExtra("track");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takePictureIntent.resolveActivity(getPackageManager())!=null)
                    {
                        File photoFile = null;
                        try{
                            photoFile = createImageFile(newCamTrack.getName());
                        }
                        catch(IOException ex)
                        {

                        }
                        if(photoFile!=null)
                        {
                            Uri photoURI = FileProvider.getUriForFile(this, getString(R.string.myFileprovider), photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private File createImageFile(String name) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = name + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void setPhotoPath(String photoPath)
    {
        mCurrentPhotoPath = photoPath;
    }

    public String getPhotoPath()
    {
        return mCurrentPhotoPath;
    }

    @Override
    public void changeImage(Track track, String image) {
        track.setImage(image);
        setTracksChanged(true);
    }
}