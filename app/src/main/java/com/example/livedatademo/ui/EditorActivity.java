package com.example.livedatademo.ui;

import android.view.MenuItem;

import com.example.livedatademo.BR;
import com.example.livedatademo.R;
import com.example.livedatademo.base.BaseActivity;
import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.event.SharedViewModel;
import com.example.livedatademo.strictbinding.DataBindingConfig;
import com.example.livedatademo.ui.state.EditorViewModel;

import java.util.UUID;

import androidx.appcompat.widget.Toolbar;

public class EditorActivity extends BaseActivity {

    private EditorViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getActivityViewModel(EditorViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_editor, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mState = getActivityViewModel(EditorViewModel.class);
//        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
//
//        ActivityEditorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_editor);
//        binding.setLifecycleOwner(this);
//        binding.setVm(mState);
//        binding.setClick(new ClickProxy());
//    }


    public class ClickProxy implements Toolbar.OnMenuItemClickListener {

        public void locate() {
            mEvent.requestTestDelayMsg("延迟显示了");
        }

        public void back() {
            finish();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_save) {
                toggleSoftInput();
                Moment moment = new Moment();
                moment.setUuid(UUID.randomUUID().toString());
                moment.setUserName("KunMinX");
                moment.setLocation(mState.location.get());
                moment.setContent(mState.content.get());
                mEvent.requestMoment(moment);
                finish();
            }
            return true;
        }
    }
}