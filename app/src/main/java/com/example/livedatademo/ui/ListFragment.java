package com.example.livedatademo.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseFragment;
import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.adapter.MomentAdapter;
import com.example.livedatademo.ui.state.ListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListFragment extends BaseFragment {

    private ListViewModel mState;
    private SharedViewModel mEvent;


    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel(ListViewModel.class);
        mEvent = getActivityViewModel(SharedViewModel.class);
    }

//    @Override
//    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mState = getFragmentViewModel(ListViewModel.class);
//        mEvent = getActivityViewModel(SharedViewModel.class);
//    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        MomentAdapter adapter = new MomentAdapter(mActivity.getApplicationContext());
        adapter.setOnItemClickListener((item, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Moment.MOMENT, item);
            nav().navigate(R.id.action_listFragment_to_detailFragment, bundle);
        });
        return new DataBindingConfig(R.layout.list_fragment, BR.vm,mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter,adapter);
    }


//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.list_fragment, container, false);
//        ListFragmentBinding binding = ListFragmentBinding.bind(view);
//        binding.setLifecycleOwner(this);
//        binding.setVm(mState);
//        binding.setClick(new ClickProxy());
//
//        MomentAdapter adapter = new MomentAdapter(mActivity.getApplicationContext());
//        adapter.setOnItemClickListener((item, position) -> {
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(Moment.MOMENT, item);
//            nav().navigate(R.id.action_listFragment_to_detailFragment, bundle);
//        });
//        binding.setAdapter(adapter);
//
//        return view;
//    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mState.getListMutableLiveData().observe(getViewLifecycleOwner(), moments -> mState.list.setValue(moments));

        mEvent.getMoment().observe(getViewLifecycleOwner(), moment -> {
            List<Moment> list = mState.list.getValue();
            assert list != null;
            boolean modify = false;
            for (Moment moment1 : list) {
                if (moment1.getUuid().equals(moment.getUuid())) {
                    moment1.setContent(moment.getContent());
                    modify = true;
                    break;
                }
            }
            if (!modify) {
                list.add(0, moment);
            }
            mState.list.setValue(list);
        });

        mEvent.getTestDelayMsg().observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                showLongToast(s + "listFragment");
            }
        });


        if (mState.list.getValue() == null || mState.list.getValue().size() == 0) {
            mState.requestList();
        }

    }

    public class ClickProxy {
        public void fabClick() {
            nav().navigate(R.id.action_listFragment_to_editorFragment);
        }
    }
}