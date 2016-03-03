package com.xeoneux.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    public static String[] imageUrls = {
            "http://lorempixel.com/492/492/abstract",
            "http://lorempixel.com/492/492/animals",
            "http://lorempixel.com/492/492/business",
            "http://lorempixel.com/492/492/cats",
            "http://lorempixel.com/492/492/city",
            "http://lorempixel.com/492/492/food",
            "http://lorempixel.com/492/492/nightlife",
            "http://lorempixel.com/492/492/fashion",
            "http://lorempixel.com/492/492/people",
            "http://lorempixel.com/492/492/nature",
            "http://lorempixel.com/492/492/sports",
            "http://lorempixel.com/492/492/technics",
            "http://lorempixel.com/492/492/transport"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(getContext(), imageUrls));
        return rootView;
    }

    @Override
    public void onStart() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute("popularity");
        super.onStart();
    }

    public class FetchMoviesTask {

        AsyncHttpClient client = new AsyncHttpClient();

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        public void execute(String sortType) {

            String URL = buildURL(sortType);

            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d(LOG_TAG, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e(LOG_TAG, error.getMessage());
                }
            });

        }

        private String buildURL(String sortType) {

            String sortParam;

            switch (sortType) {
                case "popularity":
                    sortParam = "popularity.desc";
                    break;
                case "rating":
                    sortParam = "vote_average.desc";
                    break;
                default:
                    sortParam = "popularity.desc";
            }

            String APIVersion = "3";
            String APIKey = BuildConfig.THE_MOVIE_DATABASE_API_KEY;

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath(APIVersion)
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", APIKey)
                    .appendQueryParameter("sort_by", sortParam);

            return builder.build().toString();

        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context context;
        private String[] imageUrls;

        public ImageAdapter(Context context, String[] imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
        }

        public int getCount() {
            return imageUrls.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(492, 492));
            } else {
                imageView = (ImageView) convertView;
            }

            Glide
                    .with(context)
                    .load(imageUrls[position])
                    .centerCrop()
                    .into(imageView);

            return imageView;
        }


    }

}