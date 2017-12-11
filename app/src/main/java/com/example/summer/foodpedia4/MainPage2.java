package com.example.summer.foodpedia4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.logging.Handler;

import static android.content.ContentValues.TAG;

/**
 * Created by summer on 5/11/2017.
 */

public class MainPage2 extends Fragment {
    private Index mindex;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Configure Universal Image Loader.
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLoader = ImageLoader.getInstance();
                ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity())
                        .memoryCacheSize(2 * 1024 * 1024)
                        .memoryCacheSizePercentage(13)
                        .build();
                imageLoader.init(configuration);
            }
        }).start();

        super.onCreate(savedInstanceState);
        //create basic layout
        final View rootView = inflater.inflate(R.layout.restaurantlist, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Query query = new Query(" ")
                        .setAttributesToRetrieve("name", "image_path", "price", "score", "date", "location")
                        .setHitsPerPage(10);

                mindex.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        // [...]
                        try {
                            final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

                            JSONArray featureArray = content.getJSONArray("hits");

                            for (int i = 0; i < featureArray.length(); i++) {
                                JSONObject currenthit = featureArray.getJSONObject(i);
                                String ID = currenthit.getString("objectID");
                                String name = currenthit.getString("name");
                                String imagepath = currenthit.getString("image_path");
                                String price = currenthit.getString("price");
                                String score = currenthit.getString("score");
                                Integer date = currenthit.getInt("date");
                                String location = currenthit.getString("location");
                                restaurants.add(new Restaurant(ID, name, null, imagepath, price, score, date, location));
                            }

                            RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurants);
                            ListView listView = rootView.findViewById(R.id.list);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("Care", "Error in fetching data", e);
                        }
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Client client = new Client("6NV5425YB8", "f1d75ee52056e6f9a5db4e1ae3f236c8");
        mindex = client.getIndex("sortedbydate");

        Query query = new Query(" ")
                .setAttributesToRetrieve("name", "image_path", "price", "score", "date", "location")
                .setHitsPerPage(10);


        mindex.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                try {
                    final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

                    JSONArray featureArray = content.getJSONArray("hits");

                    for (int i = 0; i < featureArray.length(); i++) {
                        JSONObject currenthit = featureArray.getJSONObject(i);
                        String ID = currenthit.getString("objectID");
                        String name = currenthit.getString("name");
                        String imagepath = currenthit.getString("image_path");
                        String price = currenthit.getString("price");
                        String score = currenthit.getString("score");
                        Integer date = currenthit.getInt("date");
                        String location = currenthit.getString("location");
                        restaurants.add(new Restaurant(ID, name, null, imagepath, price, score, date, location));
                    }

                    RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurants);
                    ListView listView = rootView.findViewById(R.id.list);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    Log.e("Care", "Error in fetching data", e);
                }
            }
        });

        return rootView;
    }



    public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        private int mRes;

        public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
            super(context, 0, restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false);
            }

            Restaurant currentrestaurant = getItem(position);

            TextView nameview = listItemView.findViewById(R.id.restname);
            nameview.setText(currentrestaurant.getname());

            TextView scoreview = listItemView.findViewById(R.id.restscore);
            scoreview.setText(currentrestaurant.getscore());

            TextView priceview = listItemView.findViewById(R.id.restprice);
            priceview.setText("$ "+currentrestaurant.getprice());

            ImageView imageview = listItemView.findViewById(R.id.restimage);
            imageLoader.displayImage(currentrestaurant.getimage_path(), imageview, displayImageOptions);

            TextView locationview = listItemView.findViewById(R.id.restlocation);
            locationview.setText(currentrestaurant.getlocation());

            LinearLayout linearLayout = listItemView.findViewById(R.id.restfull);
            String objectID = currentrestaurant.getobjectID();
            linearLayout.setOnClickListener(new restaurantonclicklistener(objectID));


            return listItemView;
        }
    }



    public class restaurantonclicklistener implements View.OnClickListener{
        String objectid;

        public restaurantonclicklistener(String id){
            this.objectid = id;
        }

        @Override
        public void onClick(View view){
            Log.v("CARE", objectid);
            Intent intent = new Intent(getActivity(),RestaurantDetailInfo.class);
            intent.putExtra("objectID",objectid  );
            startActivity(intent);
        }
    }


}
