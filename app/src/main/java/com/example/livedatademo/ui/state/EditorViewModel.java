package com.example.livedatademo.ui.state;

import com.example.livedatademo.data.bean.Moment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class EditorViewModel extends ViewModel {

    public final ObservableField<String> content = new ObservableField<>("");
    public final ObservableField<String> location = new ObservableField<>("发送 Toast");
    public Moment moment;

    {
        location.set("添加定位");
        content.set("");
    }
}