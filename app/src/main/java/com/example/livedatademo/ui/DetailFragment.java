package com.example.livedatademo.ui;

import android.os.Bundle;
import android.view.View;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseFragment;
import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.DetailViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailFragment extends BaseFragment {

    private DetailViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel(DetailViewModel.class);
        mEvent = getActivityViewModel(SharedViewModel.class);
        if (mState.moment == null && getArguments() != null) {
            mState.moment = getArguments().getParcelable(Moment.MOMENT);
        }
    }

//    @Override
//    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mState = getFragmentViewModel(DetailViewModel.class);
//        mEvent = getActivityViewModel(SharedViewModel.class);
//        if (mState.moment == null && getArguments() != null) {
//            mState.moment = getArguments().getParcelable(Moment.MOMENT);
//        }
//    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.detail_fragment, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.detail_fragment, container, false);
//        DetailFragmentBinding binding = DetailFragmentBinding.bind(view);
//        binding.setLifecycleOwner(this);
//        binding.setVm(mState);
//        binding.setClick(new ClickProxy());
//        return view;
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mState.content.set(mState.moment.getContent());

        mEvent.getMoment().observe(getViewLifecycleOwner(), moment -> {
            mState.moment = moment;
            mState.content.set(moment.getContent());
        });
    }



    public class ClickProxy {

        public void edit() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Moment.MOMENT, mState.moment);
            nav().navigate(R.id.action_detailFragment_to_editorFragment, bundle);
        }

        public void back() {
            nav().navigateUp();
        }

    }
}