package com.aruna.mvvmexample.repositories.remote;

import com.aruna.mvvmexample.models.NicePlace;
import com.aruna.mvvmexample.repositories.remote.models.VideoList;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by aruna on 20/02/18.
 */

public interface ApiService {

    // Create note
    @FormUrlEncoded
    @POST("android/video.php")
    Single<ArrayList<NicePlace>> callVideo(@Field("id") String id);

}
