package com.jagdish.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagdish.popularmovies.data.MovieDetail;
import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {

    private ImageView posterImgView;
    private TextView titleValue;
    private TextView releaseDateValue;
    private TextView voteAverageValue;
    private TextView overviewValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
        if (getIntent().hasExtra("movieDetail")) {
            MovieDetail movieDetail = getIntent().getParcelableExtra("movieDetail");
            if (movieDetail != null) {
                setDetail(movieDetail);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to previous activity
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        posterImgView = (ImageView) findViewById(R.id.posterImgView);
        titleValue = (TextView) findViewById(R.id.titleValue);
        releaseDateValue = (TextView) findViewById(R.id.releaseDateValue);
        voteAverageValue = (TextView) findViewById(R.id.voteAverageValue);
        overviewValue = (TextView) findViewById(R.id.overviewValue);
    }

    private void setDetail(MovieDetail movieDetail) {

        Picasso.with(MovieDetailActivity.this).load(movieDetail.getPosterPath()).placeholder(getResources().getDrawable(R.drawable.ic_thumbnails_loading)).error(getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(posterImgView);

        titleValue.setText(movieDetail.getTitle());
        releaseDateValue.setText(movieDetail.getReleaseDate());
        voteAverageValue.setText(String.valueOf(movieDetail.getVoteAverage()));
        overviewValue.setText(movieDetail.getOverview());

    }
}
