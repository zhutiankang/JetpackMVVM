package com.example.livedatademo.ui.state;

import com.example.livedatademo.data.bean.Moment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    public final ObservableField<String> content = new ObservableField<>("");
    public final ObservableField<String> edit = new ObservableField<>("编辑");
    public Moment moment;

}