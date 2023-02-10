package com.example.livedatademo.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseActivity;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getActivityViewModel(MainViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_main, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mState = getActivityViewModel(MainViewModel.class);
//        mEvent = getApplicationScopeViewModel(SharedViewModel.class);

//        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.setClick(new ClickProxy());

        mEvent.getMoment().observe(this, moment -> {
            Toast.makeText(this, moment.getContent(), Toast.LENGTH_SHORT).show();
        });

        mEvent.getTestDelayMsg().observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                showLongToast(s + "MainActivity");
            }
        });
    }


    public class ClickProxy {

        public void toSecondActivity() {
            startActivity(new Intent(MainActivity.this, EditorActivity.class));
//            SensorAnalyticsApi.getInstance().track("track",null);
//            SensorAnalyticsApi.getInstance().trackAppClick("trackAppClick","222");
//            SensorAnalyticsApi.getInstance().trackAppOtherEvent("trackAppOtherEvent",null);
//            SensorAnalyticsApi.getInstance().login("login");
//            SensorAnalyticsApi.getInstance().appViewScreenTimeStart("appViewScreenTimeStart");
//            SensorAnalyticsApi.getInstance().appViewScreenTimeEnd("appViewScreenTimeEnd");
//            SensorAnalyticsApi.getInstance().appExposureTimeStart("appExposureTimeStart");
//            SensorAnalyticsApi.getInstance().appExposureTimeEnd("appExposureTimeEnd");
        }
    }

}