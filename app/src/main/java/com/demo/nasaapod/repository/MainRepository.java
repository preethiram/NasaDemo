package com.demo.nasaapod.repository;

import com.demo.nasaapod.api.APODApi;
import com.demo.nasaapod.model.APODModel;
import com.demo.nasaapod.util.NetworkingUtil;
import com.demo.nasaapod.util.Util;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class MainRepository{

    private APODApi apiAPODInterface = NetworkingUtil.getClient().create(APODApi.class);

    public Observable<Response<List<APODModel>>> fetchAPODData(int count ){
        return apiAPODInterface.fetchAPODData(Util.NASA_APOD_API_KET, count);
    }
}
