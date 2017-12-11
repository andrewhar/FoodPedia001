package com.example.summer.foodpedia4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by summer on 23/11/2017.
 */

public class RestaurantDetailInfo extends AppCompatActivity {
    private Index mindex;
    private  DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;
    private String maddress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantdetailinfo);

        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLoader = ImageLoader.getInstance();
                ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(RestaurantDetailInfo.this)
                        .memoryCacheSize(2 * 1024 * 1024)
                        .memoryCacheSizePercentage(13)
                        .build();
                imageLoader.init(configuration);
            }
        }).start();

        String objectID = getIntent().getStringExtra("objectID");
        Log.v("objectID", objectID + "!");

        Client client = new Client("6NV5425YB8", "f1d75ee52056e6f9a5db4e1ae3f236c8");
        mindex = client.getIndex("searchID");


        Query query = new Query(objectID)
                .setAttributesToRetrieve("name", "location", "type", "image_path", "price", "score", "date")
                .setHitsPerPage(1);

        mindex.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                try {
                    final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

                    JSONArray featureArray = content.getJSONArray("hits");

                    JSONObject currenthit = featureArray.getJSONObject(0);
                    String ID = currenthit.getString("objectID");
                    String name = currenthit.getString("name");
                    maddress = currenthit.getString("location");
                    String type = currenthit.getString("type");
                    String imagepath = currenthit.getString("image_path");
                    String price = currenthit.getString("price");
                    String score = currenthit.getString("score");
                    Integer date = currenthit.getInt("date");
                    restaurants.add(new Restaurant(ID, name, type, imagepath, price, score, date, maddress));


                    TextView nameview = findViewById(R.id.detailname);
                    nameview.setText(name);

                    ImageView imageview = findViewById(R.id.detailimage);
                    imageLoader.displayImage(imagepath, imageview, displayImageOptions);

                    TextView typeview = findViewById(R.id.detailtype);
                    typeview.setText(type);

                    TextView scoreview = findViewById(R.id.detailscore);
                    scoreview.setText(score);

                    TextView priceview = findViewById(R.id.detailprice);
                    priceview.setText("$ "+ price);

                    TextView locationview = findViewById(R.id.detaillocation);
                    locationview.setText(maddress);

                    Button button = findViewById(R.id.buttongooglemapsearch);
                    button.setOnClickListener(new googlemaponclicklistener());


                } catch (JSONException e) {
                    Log.e("Care", "Error in fetching data", e);
                }
            }
        });



    }

    public class googlemaponclicklistener implements View.OnClickListener{


        @Override
        public void onClick(View view){
            Log.v("CARE", maddress);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=+"+ maddress +"＋香港"));
            startActivity(intent);
        }
    }

}
