package com.example.livedatademo.ui.state;

import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.domain.MomentRequest;
import com.example.livedatademo.domain.Request;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListViewModel extends ViewModel implements Request.IMomentRequest {

    public final MutableLiveData<List<Moment>> list = new MutableLiveData<>();

    public final MutableLiveData<Boolean> autoScrollToTopWhenInsert = new MutableLiveData<>(true);

    private final MomentRequest momentRequest = new MomentRequest();

    @Override
    public LiveData<List<Moment>> getListMutableLiveData() {
        return momentRequest.getListMutableLiveData();
    }

    @Override
    public void requestList() {
        momentRequest.requestList();
    }

    {
        autoScrollToTopWhenInsert.setValue(true);
    }
}