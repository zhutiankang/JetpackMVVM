package com.example.livedatademo.ui;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseFragment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.DetailViewModel;


public class ChildCFragment extends BaseFragment {

    private DetailViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel(DetailViewModel.class);
        mEvent = getActivityViewModel(SharedViewModel.class);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_child_c, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_child_c, container, false);
//        FragmentChildCBinding binding = FragmentChildCBinding.bind(view);
//        binding.setLifecycleOwner(this);
//        binding.setClick(new ClickProxy());
//        return view;
//    }

    public class ClickProxy {

        public void jump() {

        }

        public void back() {
            nav().navigateUp();
        }

    }
}