package com.jagdish.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jagdish.popularmovies.MovieDetailActivity;
import com.jagdish.popularmovies.R;
import com.jagdish.popularmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private static final String TAG = MoviesAdapter.class.getName();
    Context mContext;
    List<MovieDetail> movieDetailList;

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.movie_thumbnail);
        }
    }

    public MoviesAdapter(Context context, List<MovieDetail> movieDetailList) {
        this.mContext = context;
        this.movieDetailList = movieDetailList;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, final int position) {

        String posterPath = movieDetailList.get(position).getPosterPath();
        Picasso.with(mContext).load(posterPath).placeholder(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_loading)).error(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(moviesAdapterViewHolder.mPosterImageView);
        moviesAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("movieDetail", movieDetailList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == movieDetailList) return 0;
        return movieDetailList.size();
    }

}
