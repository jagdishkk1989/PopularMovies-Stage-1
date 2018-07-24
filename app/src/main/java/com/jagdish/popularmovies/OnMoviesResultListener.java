package com.jagdish.popularmovies;

import com.jagdish.popularmovies.data.MovieDetail;

import java.util.List;

public interface OnMoviesResultListener {
        void onMoviesResult(List<MovieDetail> movieDetailList);
}