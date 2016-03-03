package com.xeoneux.popularmovies;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

}