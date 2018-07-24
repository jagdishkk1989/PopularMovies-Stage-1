package com.jagdish.popularmovies;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jagdish.popularmovies.adapter.GridSpacingItemDecoration;
import com.jagdish.popularmovies.adapter.MoviesAdapter;
import com.jagdish.popularmovies.data.MovieDetail;
import com.jagdish.popularmovies.utility.AppConstants;
import com.jagdish.popularmovies.utility.ConnectionDetector;
import com.jagdish.popularmovies.utility.FetchMoviesAsyncTask;
import com.jagdish.popularmovies.utility.SessionManager;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMoviesResultListener {

    private static final String TAG = MainActivity.class.getName();
    private ConnectionDetector mConnectionDetector;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        setTitle();

        mConnectionDetector = new ConnectionDetector(getApplicationContext());
        if (mConnectionDetector.isConnectedToInternet()) {
            bindData();
        } else {
            showNoInternetDialog();
        }
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 3; // 3 columns
        int spacing = 10; // 10px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }

    private void setTitle() {
        if (getSupportActionBar() != null) {
            if (SessionManager.getIntegerSharedPrefs(SessionManager.SORT_ORDER_BY) == AppConstants.SORT_BY_POPULAR_MOVIES) {
                getSupportActionBar().setTitle(R.string.lbl_popular_movies);
            } else {
                getSupportActionBar().setTitle(R.string.lbl_top_rated_movies);
            }
        }
    }

    private void showNoInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.no_internet_connection));
        builder.setMessage(getResources().getString(R.string.message_no_internet));

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showOrderByDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOrderByDialog() {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.dialog_sort_order, null);
        final RadioGroup rdoGroupOrderBy = (RadioGroup) layout.findViewById(R.id.rdoGroupOrderBy);
        final RadioButton rdoBtnPopularMovies = (RadioButton) layout.findViewById(R.id.rdoBtnPopularMovies);
        final RadioButton rdoBtnTopRatedMovies = (RadioButton) layout.findViewById(R.id.rdoBtnTopRatedMovies);

        int prefsValue = SessionManager.getIntegerSharedPrefs(SessionManager.SORT_ORDER_BY);
        if (prefsValue == AppConstants.SORT_BY_POPULAR_MOVIES) {
            rdoBtnPopularMovies.setChecked(true);
        } else if (prefsValue == AppConstants.SORT_BY_TOP_RATED_MOVIES) {
            rdoBtnTopRatedMovies.setChecked(true);
        }

        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.sort_order_by));
        builder.setView(layout);
        builder.setPositiveButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int filterBy = 0;
                int checkedId = rdoGroupOrderBy.getCheckedRadioButtonId();
                if (checkedId == rdoBtnPopularMovies.getId()) {
                    filterBy = AppConstants.SORT_BY_POPULAR_MOVIES;
                } else if (checkedId == rdoBtnTopRatedMovies.getId()) {
                    filterBy = AppConstants.SORT_BY_TOP_RATED_MOVIES;
                }
                dialogInterface.dismiss();

                if (mConnectionDetector.isConnectedToInternet()) {
                    SessionManager.setIntegerSharedPrefs(SessionManager.SORT_ORDER_BY, filterBy);
                    setTitle();
                    bindData();
                } else {
                    showNoInternetDialog();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void bindData() {
        int filterBy = SessionManager.getIntegerSharedPrefs(SessionManager.SORT_ORDER_BY);
        new FetchMoviesAsyncTask(MainActivity.this, mProgressBar, filterBy).execute();
    }

    @Override
    public void onMoviesResult(List<MovieDetail> movieDetailList) {
        if (movieDetailList != null && movieDetailList.size() > 0) {
            mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movieDetailList));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_movies_found), Toast.LENGTH_SHORT).show();
        }
    }
}
