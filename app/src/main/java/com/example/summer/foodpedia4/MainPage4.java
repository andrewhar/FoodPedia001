package com.example.summer.foodpedia4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by summer on 2017/12/9.
 */


public class MainPage4 extends Fragment {

    public MainPage4(){ super();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.mainpage4, container, false);

        return rootView;

    }



}