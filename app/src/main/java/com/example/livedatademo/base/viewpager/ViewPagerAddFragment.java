package com.example.livedatademo.base.viewpager;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;


/**
 * author : zpp
 * time   : 2021/05/26
 * desc   :使用说明
 */
public class ViewPagerAddFragment {

    private ArrayList<Fragment> fragments;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ViewPagerAddFragment.class);
        activity.startActivity(intent);

    }

    private void initTable() {

        fragments = new ArrayList<>();

//        fragments.add(new BasicIntroduceFragment());
//        fragments.add(new GestureGuideFragment());
//        fragments.add(new VoiceGuideFragment());
//
//        binding.viewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager()));
        // 默认值1 设置缓存加载 不可太大OOM
//        binding.viewPager.setOffscreenPageLimit(fragments.size());
//        binding.viewPager.setCurrentItem(0);
//
//        GuideLeftAdapter guideLeftAdapter = new GuideLeftAdapter(this, vm.getTittleList());
//        guideLeftAdapter.setHasStableIds(true);
//        binding.recyclerViewGuide.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerViewGuide.setAdapter(guideLeftAdapter);
//        binding.recyclerViewGuide.getItemAnimator().setChangeDuration(0);
//
//        guideLeftAdapter.setOnItemClickListener((guideLeftInfo, position) -> {
//
//            binding.viewPager.setCurrentItem(position, false);
//
//        });
//        fragments.add(fragment2)
//        //设置上下滑动
//        vp2.orientation = ViewPager2.ORIENTATION_VERTICAL
//        vp2.adapter = MyAdapter(this, fragments)
    }


    private class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private class ContentPagerAdapter2 extends FragmentStatePagerAdapter {

        public ContentPagerAdapter2(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


//    class MyAdapter(fragmentActivity:FragmentActivity, private val fragments: ArrayList<Fragment>) :
//    FragmentStateAdapter(fragmentActivity) {
//        override fun createFragment(position: Int) = fragments[position]
//
//        override fun getItemCount() = fragments.size

//    <androidx.viewpager2.widget.ViewPager2
//    android:id="@+id/viewpager2"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="horizontal" />
//


//    }

//    public class TabAdapter extends FragmentStateAdapter {
//
//        private List<Integer> colors;
//
//        TabAdapter(@NonNull FragmentActivity fragmentActivity) {
//            super(fragmentActivity);
//            if (colors == null) {
//                colors = new ArrayList<>();
//            }
//        }
//
//        void addColor(int color) {
//            if (colors != null) {
//                colors.add(color);
//            }
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return ShowFragment.newInstance(colors, position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return colors.size();
//        }
//    }

//    new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> tab.setText(titles.get(position))).attach();
//
//    // 滑动监听 androidx中，TabLayout没有setupWithViewPager（ViewPager2 viewPager2）方法，
//    而是用TabLayoutMediator将TabLayout和ViewPager2结合。
//mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            super.onPageSelected(position);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            super.onPageScrollStateChanged(state);
//        }
//    });

    public class RgAdapter extends FragmentStateAdapter {

        private List<Class> fragments;

        public RgAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            if (fragments == null) {
                fragments = new ArrayList<>();
            }
        }

        public void addFragment(Fragment fragment) {
            if (fragments != null) {
                fragments.add(fragment.getClass());
            }
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            try {
                return (Fragment) fragments.get(position).newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

//     vpRg.setAdapter(adapter);
//        adapter.addFragment(new HomeFragment());
//        adapter.addFragment(new MessageFragment());
//        adapter.addFragment(new MyFragment());
//        vpRg.setCurrentItem(0);
//
//
//        vpRg.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//        @Override
//        public void onPageSelected(int position) {
//            super.onPageSelected(position);
//            switch (position) {
//                case 0:
//                    ((RadioButton) findViewById(R.id.rb_home)).setChecked(true);
//                    break;
//                case 1:
//                    ((RadioButton) findViewById(R.id.rb_msg)).setChecked(true);
//                    break;
//                case 2:
//                    ((RadioButton) findViewById(R.id.rg_my)).setChecked(true);
//                    break;
//            }
//        }
//    });
//
//}
}
