package com.aruna.mvvmexample.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.aruna.mvvmexample.MainActivity;
import com.aruna.mvvmexample.models.NicePlace;
import com.aruna.mvvmexample.repositories.remote.ApiClient;
import com.aruna.mvvmexample.repositories.remote.ApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NicePlaceRepository {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context context;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private static NicePlaceRepository instance;
    private ArrayList<NicePlace> dataSet = new ArrayList<>();

    public static NicePlaceRepository getInstance(){
        if(instance == null){
            instance = new NicePlaceRepository();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        apiService = ApiClient.getApiClient(context).create(ApiService.class);
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<List<NicePlace>> getNicePlaces(){
//        setNicePlaces();
        videoList(1);
        MutableLiveData<List<NicePlace>> data = new MutableLiveData<>();
        dataSet.add(
                new NicePlace("",
                        "","")
        );
        data.setValue(dataSet);
        return data;
    }

    private void videoList(int id) {
        disposable.add(
                apiService.callVideo(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ArrayList<NicePlace>>() {

                            @Override
                            public void onSuccess(ArrayList<NicePlace> places) {
                                Log.d(TAG, "Note updated! " + places.get(0).getTitle());
                                for (int i = 0; i < places.size(); i++) {
                                    dataSet.add(places.get(i));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());

                                dataSet.add(
                                        new NicePlace("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg",
                                                "Havasu Falls","https://www.radiantmediaplayer.com/media/bbb-360p.mp4")
                                );
                            }
                        }));
    }
}
