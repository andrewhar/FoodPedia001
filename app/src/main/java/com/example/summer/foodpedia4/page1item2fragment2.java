package com.example.summer.foodpedia4;

/**
 * Created by summer on 12/11/2017.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by summer on 11/11/2017.
 */

public class page1item2fragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Need to define the child fragment layout

        View rootView = inflater.inflate(R.layout.page1table2, container, false);

        return rootView;
    }
}