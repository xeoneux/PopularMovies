package com.xeoneux.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String poster = intent.getStringExtra("poster");
            String synopsis = intent.getStringExtra("synopsis");
            String userRating = intent.getStringExtra("userRating");
            String releaseDate = intent.getStringExtra("releaseDate");

            ((TextView) rootView.findViewById(R.id.text_view_title)).setText(title);
            ImageView imageView = ((ImageView) rootView.findViewById(R.id.image_view_poster));
            Glide
                    .with(getContext())
                    .load(poster)
                    .into(imageView);
            ((TextView) rootView.findViewById(R.id.text_view_synopsis)).setText(synopsis);
            ((TextView) rootView.findViewById(R.id.text_view_user_rating)).append(" " + userRating);
            ((TextView) rootView.findViewById(R.id.text_view_release_date)).append(" " + releaseDate);
        }
        return rootView;
    }
}
