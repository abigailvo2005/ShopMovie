package com.example.shopmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {
    String[] names;
    String[] prices;
    int[] images;
    Context context;
    LayoutInflater inflater;

    public MovieAdapter(String[] names, String[] prices, int[] images, Context context) {
        this.names = names;
        this.prices = prices;
        this.images = images;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = View.inflate(context, R.layout.item_movie_layout, null);

        TextView title = view.findViewById(R.id.movieName);
        TextView price = view.findViewById(R.id.moviePrice);
        ImageView img = view.findViewById(R.id.movieImg);

        title.setText(names[position]);
        price.setText(prices[position]);
        img.setImageResource(images[position]);


        return view;
    }
}
