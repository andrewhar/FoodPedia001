package com.example.summer.foodpedia4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by summer on 11/11/2017.
 */

public class Page1Advert2 extends Fragment {
    private AdView mAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Need to define the child fragment layout

        View rootView = inflater.inflate(R.layout.page1advert2, container, false);
        mAdView = rootView.findViewById(R.id.adView);
        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544/2793859312");
        AdRequest request = new AdRequest.Builder().build();
        mAdView.loadAd(request);

        return rootView;
    }
}