package com.aruna.mvvmexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.aruna.mvvmexample.adapters.RecyclerAdapter;
import com.aruna.mvvmexample.models.NicePlace;
import com.aruna.mvvmexample.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.RecyclerAdapterEvent {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private int pageNumber = 1;
    private boolean loading = false;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayoutManager;
    private int start;

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private MainActivityViewModel mMainActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mMainActivityViewModel.init();

        mMainActivityViewModel.getNicePlaces().observe(this, new Observer<List<NicePlace>>() {
            @Override
            public void onChanged(@Nullable List<NicePlace> nicePlaces) {
                if (start == lastVisibleItem) {
                    mAdapter.notifyDataSetChanged();
                }
                loading = false;
            }
        });

        mMainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                    mRecyclerView.smoothScrollToPosition(mMainActivityViewModel.getNicePlaces().getValue().size()-1);
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivityViewModel.addNewValue(
                        new NicePlace(
                                "https://i.imgur.com/ZcLLrkY.jpg",
                                "Washington"
                        )
                );
            }
        });

        initRecyclerView();

        setUpLoadMoreListener();
    }

    private void initRecyclerView(){
        mAdapter = new RecyclerAdapter(this, mMainActivityViewModel.getNicePlaces().getValue(), MainActivity.this);
//        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void stayPosition(int pos) {

    }

    private void setUpLoadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                Log.e("stayPosition " , " totalItemCount " + totalItemCount + " lastVisibleItem " + lastVisibleItem + " loading " + loading);
//                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    paginator.onNext(pageNumber);
                    loading = true;
                    start = totalItemCount;

                    mMainActivityViewModel.addNewValue(
                            new NicePlace(
                                    "https://i.imgur.com/ZcLLrkY.jpg",
                                    "Washington"
                            )
                    );
                }
            }
        });
    }

}
