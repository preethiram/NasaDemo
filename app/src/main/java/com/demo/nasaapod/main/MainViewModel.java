package com.demo.nasaapod.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.demo.nasaapod.model.APODModel;
import com.demo.nasaapod.model.APODUiData;
import com.demo.nasaapod.repository.MainRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final MainRepository mainRepository;

    public MainViewModel(MainRepository repository) {
        mainRepository = repository;
    }


    private MutableLiveData<APODUiData> mApodModelLiveData = new MutableLiveData<>();

    public LiveData<APODUiData> getApodModelLiveData() {
        return mApodModelLiveData;
    }

    public void fetchAPODData(int count){
        final APODUiData data = new APODUiData();
        mainRepository.fetchAPODData(count)
            .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<List<APODModel>>>() {
                    @Override
                    public void onNext(Response<List<APODModel>> listResponse) {

                        if(listResponse.isSuccessful()){
                            data.setApodModels(listResponse.body());
                        }else {
                            data.setErrorMsg(listResponse.message());
                        }
                        mApodModelLiveData.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        data.setErrorMsg("Something went wrong : \n "+e.getLocalizedMessage());
                        mApodModelLiveData.postValue(data);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //
    public static class MainViewModelFactory implements ViewModelProvider.Factory {

        private final MainRepository mainRepository;

        public MainViewModelFactory(MainRepository repository) {
            this.mainRepository = repository;
        }

        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MainViewModel.class)) {
                return (T) new MainViewModel(mainRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
