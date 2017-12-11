package com.example.summer.foodpedia4;

/**
 * Created by summer on 12/11/2017.
 */


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by summer on 11/11/2017.
 */

public class page1item2fragment1 extends Fragment {

    TextView iconview;
    Drawable image;
    SpannableString sb;
    ImageSpan imageSpan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Need to define the child fragment layout

        View rootView = inflater.inflate(R.layout.page1table1, container, false);

        LinearLayout cheapchoice = rootView.findViewById(R.id.cheapchoice);
        cheapchoice.setOnClickListener(new cheapclick());

        LinearLayout expensive = rootView.findViewById(R.id.expensivesearch);
        expensive.setOnClickListener(new expensiveclick());

        LinearLayout goodmark = rootView.findViewById(R.id.goodmarksearch);
        goodmark.setOnClickListener(new goodmarkclick());

        LinearLayout buffet = rootView.findViewById(R.id.buffetsearch);
        buffet.setOnClickListener(new buffetclick());

        LinearLayout vegetable = rootView.findViewById(R.id.vegetablesearch);
        vegetable.setOnClickListener(new vegetableclick());

        LinearLayout fastfood = rootView.findViewById(R.id.fastfoodsearch);
        fastfood.setOnClickListener(new fastfoodclick());

        return rootView;
    }

    public class cheapclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "cheapsearch");
            startActivity(intent);
        }
    }

    public class expensiveclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "expensivesearch");
            startActivity(intent);
        }
    }

    public class goodmarkclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "goodmarksearch");
            startActivity(intent);
        }
    }

    public class buffetclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "buffetsearch");
            startActivity(intent);
        }
    }

    public class vegetableclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "vegetablesearch");
            startActivity(intent);
        }
    }

    public class fastfoodclick implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),SimpleSearchMain.class);
            intent.putExtra("searchmethod", "fastfoodsearch");
            startActivity(intent);
        }
    }

}