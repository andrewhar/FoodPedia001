package com.example.summer.foodpedia4;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by summer on 12/11/2017.
 */

public class SimpleSearchMain extends AppCompatActivity {

    private Index mindex;
    private String Stringsearchkeyword;
    private String Searchmethod;
    private String Stringindex;
    private TextView textviewtitle;
    private boolean check;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplesearchmain);

        Stringsearchkeyword = getIntent().getStringExtra("keyword");
        Searchmethod = getIntent().getStringExtra("searchmethod");
        textviewtitle = findViewById(R.id.searchmaintitle);

        if (Searchmethod.equals("ordinarysearch") == true) {
            textviewtitle.setText("關鍵字:  " + Stringsearchkeyword);
            Stringindex = "restaurantlist";
        }
        if (Searchmethod.equals("fastfoodsearch") == true) {
            textviewtitle.setText("快餐之選");
            Stringsearchkeyword = "快餐";
            Stringindex = "restaurantlist";
        }
        if (Searchmethod.equals("cheapsearch") == true) {
            textviewtitle.setText("扺食之選");
            Stringsearchkeyword = "";
            Stringindex = "sortedbycheap";
        }
        if (Searchmethod.equals("goodmarksearch") == true) {
            textviewtitle.setText("高分推介");
            Stringsearchkeyword = "";
            Stringindex = "sortedbygoodmark";
        }
        if (Searchmethod.equals("expensivesearch") == true) {
            textviewtitle.setText("高檔餐廳");
            Stringsearchkeyword = "";
            Stringindex = "sortedbyexpensive";
        }
        if (Searchmethod.equals("buffetsearch") == true) {
            textviewtitle.setText("任飲任食");
            Stringsearchkeyword = "任食";
            Stringindex = "restaurantlist";
        }
        if (Searchmethod.equals("vegetablesearch") == true) {
            textviewtitle.setText("素食之選");
            Stringsearchkeyword = "素";
            Stringindex = "restaurantlist";
        }

        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLoader = ImageLoader.getInstance();
                ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(SimpleSearchMain.this)
                        .memoryCacheSize(2 * 1024 * 1024)
                        .memoryCacheSizePercentage(13)
                        .build();
                imageLoader.init(configuration);
            }
        }).start();

        Client client = new Client("6NV5425YB8", "f1d75ee52056e6f9a5db4e1ae3f236c8");
        mindex = client.getIndex(Stringindex);

        Query query = new Query(Stringsearchkeyword)
                .setAttributesToRetrieve("objectID", "name", "image_path", "type", "price", "score", "date", "location")
                .setHitsPerPage(50);
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

                    SearchAdapter adapter = new SearchAdapter(getBaseContext(), restaurants);
                    ListView listView = findViewById(R.id.search_list);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    Log.e("Care", "Error in fetching data", e);
                }
            }
        });
    }


    public class SearchAdapter extends ArrayAdapter<Restaurant> {

        public SearchAdapter(Context context, ArrayList<Restaurant> restaurants) {
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

            LinearLayout linearLayout = listItemView.findViewById(R.id.restfull);
            String objectID = currentrestaurant.getobjectID();
            linearLayout.setOnClickListener(new restaurantonclicklistener(objectID));

            TextView locationview = listItemView.findViewById(R.id.restlocation);
            locationview.setText(currentrestaurant.getlocation());

            return listItemView;
        }

        public class restaurantonclicklistener implements View.OnClickListener {
            String objectid;

            public restaurantonclicklistener(String id) {
                this.objectid = id;
            }

            @Override
            public void onClick(View view) {
                Log.v("CARE", objectid);
                Intent intent = new Intent(SimpleSearchMain.this, RestaurantDetailInfo.class);
                intent.putExtra("objectID", objectid);
                startActivity(intent);
            }
        }


    }

}
