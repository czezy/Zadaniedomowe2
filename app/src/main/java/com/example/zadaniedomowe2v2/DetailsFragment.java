package com.example.zadaniedomowe2v2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private OnDetailsInteractionListener mListener;
    private ImageView imageViewLarge;
    private TextView nameView;
    private TextView artistView;
    private TextView yearView;
    private Intent intent;
    public static final int REQUEST_IMAGE_CAPTURE_DET = 5;
    private String mCurrentPhotoPath;
    private Track mDisplayedTrack;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        imageViewLarge = rootView.findViewById(R.id.imageViewLarge);
        nameView = rootView.findViewById(R.id.nameView);
        artistView = rootView.findViewById(R.id.artistView);
        yearView = rootView.findViewById(R.id.yearView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsInteractionListener) {
            mListener = (OnDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = mDisplayedTrack.getName() + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        mListener.setPhotoPath(mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onClick(View v) {
        if(mDisplayedTrack!=null)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null)
            {
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }
                catch(IOException ex)
                {

                }
                if(photoFile!=null)
                {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), getString(R.string.myFileprovider), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_DET);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.imageViewLarge).setOnClickListener(this);
        intent = getActivity().getIntent();
        if(intent != null)
        {
            Track track = intent.getParcelableExtra("track");
            if(track != null) displayTrack(track);
        }
    }

    public void displayTrack(Track track)
    {
        mDisplayedTrack = track;
        nameView.setText(track.getName());
        artistView.setText(track.getArtist());
        yearView.setText(track.getYear());

        if(track.getImage() != null && !track.getImage().isEmpty()) {
            if (track.getImage().contains("drawable")) {
                switch (track.getImage()) {
                    case "drawable 1":
                        imageViewLarge.setImageDrawable(getResources().getDrawable(R.drawable.circle_drawable_green));
                        break;
                    case "drawable 2":
                        imageViewLarge.setImageDrawable(getResources().getDrawable(R.drawable.circle_drawable_red));
                        break;
                    case "drawable 3":
                        imageViewLarge.setImageDrawable(getResources().getDrawable(R.drawable.circle_drawable_orange));
                        break;
                    default:
                        imageViewLarge.setImageDrawable(getResources().getDrawable(R.drawable.circle_drawable_grey));
                        break;
                }
            }
            else{
                Handler handler = new Handler();
                imageViewLarge.setVisibility(View.INVISIBLE);
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run()
                    {
                        imageViewLarge.setVisibility(View.VISIBLE);
                        Bitmap cameraImage = PicUtils.decodePic(mDisplayedTrack.getImage(), imageViewLarge.getWidth(), imageViewLarge.getHeight());
                        imageViewLarge.setImageBitmap(cameraImage);
                    }
                }, 200);
            }
        }
        else {
            imageViewLarge.setImageDrawable(getResources().getDrawable(R.drawable.circle_drawable_grey));
        }
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent data)
    {
        if(request_code == REQUEST_IMAGE_CAPTURE_DET && result_code == RESULT_OK)
        {
            FragmentActivity holdingActivity = getActivity();
            if(holdingActivity != null)
            {
                ImageView trackImage = holdingActivity.findViewById(R.id.imageViewLarge);
                Bitmap cameraImage = PicUtils.decodePic(mCurrentPhotoPath, trackImage.getWidth(), trackImage.getHeight());
                trackImage.setImageBitmap(cameraImage);
                mDisplayedTrack.setImage(mCurrentPhotoPath);
                mListener.changeImage(mDisplayedTrack, mCurrentPhotoPath);
            }
        }
    }

    public interface OnDetailsInteractionListener {
        void changeImage(Track track, String image);
        void setPhotoPath(String photoPath);
    }
}
