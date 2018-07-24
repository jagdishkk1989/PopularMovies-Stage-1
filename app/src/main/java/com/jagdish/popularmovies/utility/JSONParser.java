package com.jagdish.popularmovies.utility;

import android.net.Uri;

import com.jagdish.popularmovies.data.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class JSONParser {

    public static List<MovieDetail> parseMovieListResponse(String response) {
        List<MovieDetail> movieDetailList = new ArrayList<>();

        try {

            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("results")) {

                JSONArray resultsArray = jsonResponse.getJSONArray("results");

                int length = resultsArray.length();

                for (int i = 0; i < length; i++) {

                    JSONObject movieObj = resultsArray.getJSONObject(i);

                    MovieDetail movieDetail = new MovieDetail();

                    if (movieObj.has("id")) {
                        movieDetail.setId(movieObj.getLong("id"));
                    }
                    if (movieObj.has("vote_average")) {
                        movieDetail.setVoteAverage(movieObj.getDouble("vote_average"));
                    }
                    if (movieObj.has("title")) {
                        movieDetail.setTitle(movieObj.getString("title"));
                    }
                    if (movieObj.has("poster_path")) {
                        movieDetail.setPosterPath(AppConstants.POSTER_MOVIES_BASE_URL + AppConstants.POSTER_SIZE + movieObj.getString("poster_path"));
                    }
                    if (movieObj.has("overview")) {
                        movieDetail.setOverview(movieObj.getString("overview"));
                    }
                    if (movieObj.has("release_date")) {
                        movieDetail.setReleaseDate(movieObj.getString("release_date"));
                    }

                    movieDetailList.add(movieDetail);
                }
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return movieDetailList;
    }

    public static Uri generatePosterUri(String posterPath) {
        Uri builtUri = Uri.parse(AppConstants.POSTER_MOVIES_BASE_URL).buildUpon()
                .appendEncodedPath(AppConstants.POSTER_SIZE).appendEncodedPath(posterPath)
                .build();
        return builtUri;
    }

}
