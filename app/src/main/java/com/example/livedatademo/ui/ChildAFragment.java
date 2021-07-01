package com.example.livedatademo.ui;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseFragment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.DetailViewModel;


public class ChildAFragment extends BaseFragment {

    private DetailViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel(DetailViewModel.class);
        mEvent = getActivityViewModel(SharedViewModel.class);
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mState = getFragmentViewModel(DetailViewModel.class);
//        mEvent = getActivityViewModel(SharedViewModel.class);
//    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_child_a, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_child_a, container, false);
//        FragmentChildABinding binding = FragmentChildABinding.bind(view);
//        binding.setLifecycleOwner(this);
//        binding.setClick(new ClickProxy());
//        return view;
//    }


    public class ClickProxy {

        public void jumpToB() {
            nav().navigate(R.id.action_childAFragment_to_childBFragment);
        }

        public void jumpToC() {
            nav().navigate(R.id.action_childAFragment_to_childCFragment);
        }

        public void back() {
            nav().navigateUp();
        }

    }

}