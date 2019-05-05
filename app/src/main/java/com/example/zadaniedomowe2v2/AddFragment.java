package com.example.zadaniedomowe2v2;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddFragment extends Fragment {

    private OnAddInteractionListener mListener;
    private EditText nameEdit;
    private EditText artistEdit;
    private EditText yearEdit;
    private Button addButton;
    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        nameEdit = rootView.findViewById(R.id.nameEdit);
        artistEdit = rootView.findViewById(R.id.artistEdit);
        yearEdit = rootView.findViewById(R.id.yearEdit);
        addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                Track newTrack = new Track(nameEdit.getText().toString(), artistEdit.getText().toString(), yearEdit.getText().toString(), "drawable 1", MainActivity.trackList.size());
                mListener.addTrack(newTrack);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddInteractionListener) {
            mListener = (OnAddInteractionListener) context;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }

    public interface OnAddInteractionListener {
        void addTrack(Track newTrack);
    }
}
