package com.example.summer.foodpedia4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by summer on 11/11/2017.
 */

public class Page1Advert1 extends Fragment {
    private AdView mAdView;
    private AdRequest mrequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Need to define the child fragment layout
        mrequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE232")
                .build();

        View rootView = inflater.inflate(R.layout.page1advert1, container, false);
        mAdView = rootView.findViewById(R.id.adView);
        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544/2793859312");
        mrequest = new AdRequest.Builder().build();
        mAdView.loadAd(mrequest);


        return rootView;
    }
}