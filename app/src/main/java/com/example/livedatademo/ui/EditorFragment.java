package com.example.livedatademo.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseFragment;
import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.EditorViewModel;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class EditorFragment extends BaseFragment {


    private EditorViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel(EditorViewModel.class);
        mEvent = getActivityViewModel(SharedViewModel.class);
        if (mState.moment == null && getArguments() != null) {
            mState.moment = getArguments().getParcelable(Moment.MOMENT);
        }
        if (mState.moment == null) {
            mState.moment = new Moment();
        }
    }

//    @Override
//    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mState = getFragmentViewModel(EditorViewModel.class);
//        mEvent = getActivityViewModel(SharedViewModel.class);
//        if (mState.moment == null && getArguments() != null) {
//            mState.moment = getArguments().getParcelable(Moment.MOMENT);
//        }
//        if (mState.moment == null) {
//            mState.moment = new Moment();
//        }
//    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.editor_fragment, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.editor_fragment, container, false);
//        EditorFragmentBinding binding = EditorFragmentBinding.bind(view);
//        binding.setLifecycleOwner(this);
//        binding.setVm(mState);
//        binding.setClick(new ClickProxy());
//        return view;
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mState.content.set(mState.moment.getContent());
    }

    public class ClickProxy implements Toolbar.OnMenuItemClickListener {

        public void locate() {
            mEvent.requestTestDelayMsg("延迟显示了");
        }

        public void back() {
            nav().navigateUp();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_save) {
                toggleSoftInput();
                if (mState.moment.getUuid() == null) {
                    mState.moment.setUuid(UUID.randomUUID().toString());
                    mState.moment.setUserName("KunMinX");
                    mState.moment.setLocation(mState.location.get());
                }
                mState.moment.setContent(mState.content.get());
                mEvent.requestMoment(mState.moment);
                nav().navigateUp();
            }
            return true;
        }
    }

}