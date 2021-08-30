package com.example.livedatademo.base.viewpager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivityExt {


    /**
     * Fragment 增加子Fragment
     *
     * @param childFragment
     */
    public static void replaceChildFragment(Fragment parentFragment, Fragment childFragment, int resId, boolean addToBackStack) {
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(resId, childFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
//        LogExtKt.log(parentFragment.getClass().getName(), "replaceChildFragment");
    }


    /**
     * Activity 增加Fragment
     *
     * @param baseActivity
     * @param fragment
     * @param resId
     * @param addToBackStack
     */
    public static void addContentFragment(AppCompatActivity baseActivity, Fragment fragment, int resId, boolean addToBackStack) {
        FragmentTransaction transaction = baseActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(resId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
//        LogExtKt.log(baseActivity.getClass().getName(), "replaceChildFragment");
    }


    /**
     * Activity 增加Fragment
     *
     * @param baseActivity
     * @param fragment
     * @param resId
     */
    public static void addContentFragment(AppCompatActivity baseActivity, Fragment fragment, int resId) {
        FragmentTransaction transaction = baseActivity.getSupportFragmentManager().beginTransaction();
        if (null == transaction) {
            return;
        }
        transaction.replace(resId, fragment);
        transaction.commitAllowingStateLoss();
//        LogExtKt.log(baseActivity.getClass().getName(), "addContentFragment");
    }



//    /**
//     * 根据Tag获取Fragment
//     *
//     * @param tag
//     * @return
//     */
//    private Fragment obtainFragment(String tag) {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        return manager.findFragmentByTag(tag);
//    }
//
//
//    private void showFragment(final Fragment toShowFragment) {
//        if (lastFragment == toShowFragment) {
//            TKStoreLog.d(TAG, "To show fragment is already showing, ignore replace the page.");
//            return;
//        }
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        // Hide current fragment if showing.
//        if (lastFragment != null) {
//            // Showing fragment exists, hide it.
//            transaction.hide(lastFragment);
//        }
//
//        // To show new fragment.
//        if (toShowFragment.isAdded()) {
//            // This fragment is been added, do not add again.
//            transaction.show(toShowFragment);
//        } else {
//            // This fragment has not been added, add it.
//            transaction.add(R.id.main_container, toShowFragment);
//        }
//
//        // Commit the transaction.
//        transaction.commit();
//        lastFragment = toShowFragment;
//    }
//
//
//    //intelligence.drive MainActivity
//    private void showFragment(LinkBaseFragment fragment) {
//        LogUtil.d(TAG, "showFragment: ");
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        // 解决换肤之后对象重新生成的问题，将Manager里管理的已添加的fragment全部隐藏
//        for (Fragment frag : fragments) {
//            if (frag.isAdded()) {
//                transaction.hide(frag);
//            }
//        }
//        if (!fragment.isAdded()) {
//            transaction.add(R.id.fish_frame, fragment, fragment.getClass().getName()).show(fragment).commit();
//        } else {
//            transaction.show(fragment).commit();
//        }
//    }
//
//
//    @NonNull
//    public abstract FragmentTransaction add(@NonNull Fragment var1, @Nullable String var2);
//
//    @NonNull
//    public abstract FragmentTransaction add(@IdRes int var1, @NonNull Fragment var2);
//
//    @NonNull
//    public abstract FragmentTransaction add(@IdRes int var1, @NonNull Fragment var2, @Nullable String var3);
//
//    @NonNull
//    public abstract FragmentTransaction replace(@IdRes int var1, @NonNull Fragment var2);
//
//    @NonNull
//    public abstract FragmentTransaction replace(@IdRes int var1, @NonNull Fragment var2, @Nullable String var3);
//
//
//    //fragemnt 生命周期与view周期不一致   getViewLifecycleOwner()最好 隐藏回调
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) { //不可见
//            TKStoreLog.d(TAG, "onHiddenChanged.");
//            vm.exitEditing();
//        } //可见
//    }
}
