package com.example.livedatademo.unpeek;


import android.util.Log;

import com.example.livedatademo.utils.AndPoolExecutors;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * LiveData Event
 *
 * @author zhutianakng
 */
public final class LiveDataManager {

    private static final String TAG = LiveDataManager.class.getSimpleName();

    public static final LiveDataManager INSTANCE = new LiveDataManager();

    private final Map<String, UnPeekLiveData<?>> liveDataMap = new HashMap<>();

    private LiveDataManager() {
    }

    public void initializeLiveData() {
        // 允许value为空通知 默认为null不通知
        liveDataMap.put(Alias.EVENT_EXAM, new UnPeekLiveData.Builder<>().setAllowNullValue(true).create());
        liveDataMap.put(Alias.EVENT_HISTORY, new UnPeekLiveData<>());
    }

    public void clearAllLiveData() {
        for (Map.Entry<String, UnPeekLiveData<?>> entry : liveDataMap.entrySet()) {
            entry.getValue().clear();
        }
        liveDataMap.clear();
    }

    public <ValueType> void observe(final @Alias String alias, final LifecycleOwner owner,
                                    final Observer<ValueType> observer) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        liveData.observe(owner, observer);
    }

    public <ValueType> void observeForever(final @Alias String alias,
                                           final Observer<ValueType> observer) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        liveData.observeForever(observer);
    }

    public <ValueType> void observeSticky(final @Alias String alias, final LifecycleOwner owner,
                                          final Observer<ValueType> observer) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        liveData.observeSticky(owner, observer);
    }

    public <ValueType> void observeStickyForever(final @Alias String alias,
                                                 final Observer<ValueType> observer) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        liveData.observeStickyForever(observer);
    }

    public <ValueType> void remove(final @Alias String alias, final Observer<ValueType> observer) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        liveData.removeObserver(observer);
    }

    public <ValueType> void setValue(final @Alias String alias, final ValueType value) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }

        AndPoolExecutors.getInstance().mainThread().execute(() -> {
            try {
                liveData.setValue(value);
            } catch (ClassCastException exception) {
                Log.e(TAG, "SetValue: " + alias + " got an error, " + exception.getMessage());
            }
        });
    }

    public <ValueType> void postValue(final @Alias String alias, final ValueType value) {
        final UnPeekLiveData<ValueType> liveData = (UnPeekLiveData<ValueType>) liveDataMap.get(alias);
        if (liveData == null) {
            Log.e(TAG, "Live data not registered, " + alias);
            return;
        }
        try {
            liveData.postValue(value);
        } catch (ClassCastException exception) {
            Log.e(TAG, "PostValue: " + alias + " got an error, " + exception.getMessage());
        }
    }
}