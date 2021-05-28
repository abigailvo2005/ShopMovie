package com.example.shopmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    ArrayList<String> names;
    ArrayList<String> prices;
    ArrayList<String> imageURLs;
    Context context;
    LayoutInflater inflater;
    ShareButton shareBtn;

    public MovieAdapter(ArrayList<String> names, ArrayList<String> prices, ArrayList<String> images, Context context) {
        this.names = names;
        this.prices = prices;
        this.imageURLs = images;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
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

        //inflate view for each item in Grid System
        View view = View.inflate(context, R.layout.item_movie_layout, null);

        //declare elements
        TextView title = view.findViewById(R.id.movieName);
        TextView price = view.findViewById(R.id.moviePrice);
        ImageView img = view.findViewById(R.id.movieImg);

        //display movie's information (title, price, image)
        title.setText(names.get(position));
        price.setText(prices.get(position));
        Picasso.get().load(imageURLs.get(position)).into(img);

        //Let share button share all of those information to Facebook
        shareBtn = view.findViewById(R.id.fb_share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder() //create a ShareLinkContent obj --> share contents to Facebook
                        .setQuote(names.get(position) + " is my favorite movie! It's only " + prices.get(position))
                        .setContentUrl(Uri.parse(imageURLs.get(position)))
                        .build();

                ShareDialog.show((Activity) context, shareLinkContent);
            }
        });
//        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder() //create a ShareLinkContent obj --> share contents to Facebook
//                .setQuote(names.get(position) + " is my favorite movie! It's only " + prices.get(position))
//                .setContentUrl(Uri.parse(imageURLs.get(position)))
//                .build();
//
//        ShareDialog.show((Activity) context, shareLinkContent);

        return view;
    }
}
