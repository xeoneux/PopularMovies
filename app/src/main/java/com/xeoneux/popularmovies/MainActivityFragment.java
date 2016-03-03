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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    public static View rootView;
    public static GridView gridView;
    public static String[] imageUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute("popularity");

        return rootView;
    }

    public class FetchMoviesTask {

        AsyncHttpClient client = new AsyncHttpClient();

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        public void execute(String sortType) {

            String URL = buildURL(sortType);

            client.get(URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String JSONResponseString = new String(responseBody);
                    try {
                        imageUrls = ParseJSON(JSONResponseString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    gridView = (GridView) rootView.findViewById(R.id.grid_view);
                    gridView.setAdapter(new ImageAdapter(getContext(), imageUrls));
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

        private String[] ParseJSON(String JSONResponseString) throws JSONException {

            JSONObject JSONData = new JSONObject(JSONResponseString);
            JSONArray results = JSONData.getJSONArray("results");
            String[] posters = new String[results.length()];
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = new JSONObject(results.getString(i));
                String posterPath = movie.getString("poster_path").substring(1);
                Uri.Builder builder = new Uri.Builder()
                        .scheme("https")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("original")
                        .appendPath(posterPath)
                        .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DATABASE_API_KEY);
                posters[i] = builder.build().toString();
            }

            return posters;
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