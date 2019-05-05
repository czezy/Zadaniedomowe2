package com.example.zadaniedomowe2v2;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zadaniedomowe2v2.TrackFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private final List<Track> trackList;
    private final OnListFragmentInteractionListener mListener;


    public MyTrackRecyclerViewAdapter(List<Track> trackList, OnListFragmentInteractionListener mListener) {
        this.trackList = trackList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Context context = holder.mView.getContext();
        holder.mItem = trackList.get(position);
        holder.nameText.setText(trackList.get(position).getName());

        if(trackList.get(position).getImage().contains("drawable"))
        {
            switch(trackList.get(position).getImage())
            {
                case "drawable 1":
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_drawable_green));
                    break;
                case "drawable 2":
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_drawable_red));
                    break;
                case "drawable 3":
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_drawable_orange));
                    break;
                default:
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_drawable_green));
                    break;
            }
        }
        else
        {
            Bitmap cameraImage = PicUtils.decodePic(holder.mItem.getImage(), 60, 80);
            holder.imageView.setImageBitmap(cameraImage);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDetails(holder.mItem);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle(R.string.deleteTitle).setMessage(R.string.deleteDialog).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mListener.deleteTrack(holder.mItem);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton(R.string.cancel, null).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView nameText;
        public final ImageButton deleteButton;
        public Track mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameText = (TextView) view.findViewById(R.id.name);
            imageView = (ImageView) view.findViewById(R.id.image);
            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameText.getText() + "'";
        }
    }
}
