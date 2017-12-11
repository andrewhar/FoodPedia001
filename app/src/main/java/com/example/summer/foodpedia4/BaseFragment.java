package com.example.summer.foodpedia4;

import android.app.Fragment;
import android.net.Uri;

/**
 * Created by summer on 18/11/2017.
 */

public class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";

    public BaseFragment(){
        //
    }


    public interface OnFragmentInteractionListener {
    public void onFragmentInteraction(Uri uri);
    public void onFragmentInteraction(String id);
    public void onFragmentInteraction(int actionId);
}
}
