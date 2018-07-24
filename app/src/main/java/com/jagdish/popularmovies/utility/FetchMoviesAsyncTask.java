package com.jagdish.popularmovies.utility;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jagdish.popularmovies.MainActivity;
import com.jagdish.popularmovies.data.MovieDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FetchMoviesAsyncTask extends AsyncTask<Void, Void, List<MovieDetail>> {

    private static final String TAG = FetchMoviesAsyncTask.class.getName();

    private MainActivity mainActivity;
    private ProgressBar mProgressBar;
    private int orderType;

    public FetchMoviesAsyncTask(MainActivity mainActivity, ProgressBar progressBar, int orderType) {
        this.mainActivity = mainActivity;
        this.mProgressBar = progressBar;
        this.orderType = orderType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<MovieDetail> doInBackground(Void... voids) {
        List<MovieDetail> movieDetailList = new ArrayList<>();
        URL url = buildUrl(orderType);

        String response = getResponseFromUrl(url);
        if (response != null)
            movieDetailList = JSONParser.parseMovieListResponse(response);

        return movieDetailList;
    }

    @Override
    protected void onPostExecute(List<MovieDetail> movieDetailList) {
        super.onPostExecute(movieDetailList);
        mProgressBar.setVisibility(View.GONE);
        mainActivity.onMoviesResult(movieDetailList);
    }

    public URL buildUrl(int orderType) {

        Uri builtUri = null;
        if (orderType == AppConstants.SORT_BY_POPULAR_MOVIES) {
            builtUri = Uri.parse(AppConstants.BASE_URL + AppConstants.ACTION_POPULAR_MOVIES).buildUpon()
                    .appendQueryParameter(AppConstants.API_KEY_PARAM, AppConstants.API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(AppConstants.BASE_URL + AppConstants.ACTION_TOP_RATED_MOVIES).buildUpon()
                    .appendQueryParameter(AppConstants.API_KEY_PARAM, AppConstants.API_KEY)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromUrl(URL url) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String response = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            response = buffer.toString();

        } catch (IOException e) {
            Log.e(TAG, "Exception", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
