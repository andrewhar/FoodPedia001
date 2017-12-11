package com.example.summer.foodpedia4;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by summer on 5/11/2017.
 */

public class MainPage1 extends Fragment {

    private String Stringsearchkeyword;
    private EditText Editextsearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //create basic layout
        View rootView = inflater.inflate(R.layout.mainpage1, container, false);

        Editextsearch =  rootView.findViewById(R.id.edittextsearch);
        Drawable image = getActivity().getResources().getDrawable(R.drawable.search);
        image.setBounds(0,0, 45, 45);
        SpannableString sb = new SpannableString("00  搜尋美食(餐廳,食品,地點)");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Editextsearch.setHint(sb);

        //set button to confirm searching
        Editextsearch.addTextChangedListener(new searchtextwatcher());
        Button buttonsearch = rootView.findViewById(R.id.buttonsearch);
        buttonsearch.setOnClickListener(new simplesearchclicklistener());

        //create viewpager for item1
        ViewPager insideviewPager =  rootView.findViewById(R.id.insideviewpager);
        AdvertAdapter adapter = new AdvertAdapter(getActivity(), getChildFragmentManager());
        insideviewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(insideviewPager, true);

        //create viewpager for item2
        ViewPager insideviewPager2 =  rootView.findViewById(R.id.insideviewpager2);
        page1item2adapter adapter2 = new   page1item2adapter(getActivity(), getChildFragmentManager());
        insideviewPager2.setAdapter(adapter2);

        return rootView;
    }

    public class simplesearchclicklistener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("keyword", Stringsearchkeyword);
            intent.putExtra("searchmethod", "ordinarysearch");
            startActivity(intent);
        }
    }



    public class searchtextwatcher implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Stringsearchkeyword = Editextsearch.getText().toString();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Stringsearchkeyword = Editextsearch.getText().toString();
        }
    }

}
