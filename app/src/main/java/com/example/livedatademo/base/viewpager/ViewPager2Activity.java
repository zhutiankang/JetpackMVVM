package com.example.livedatademo.base.viewpager;

import android.os.Bundle;

import com.example.livedatademo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


/**
 * author : zhutiankang
 * time   : 2021/05/26
 * desc   : ViewPager2 recyclerview实现
 * FragmentStateAdapter 替代 FragmentStatePagerAdapter
 *
 *    RecyclerView.Adapter 替代 PagerAdapter
 *
 *     registerOnPageChangeCallback 替代 addPageChangeListener
 *       notifyDataSetChanged();
 *     notifyItemChanged(int position)
 */
public class ViewPager2Activity extends AppCompatActivity {


    private TabLayout tab;
    private ViewPager2 vp2;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titls = new ArrayList<>();

    private Fragment fragment1;
    private Fragment fragment2;
    //设置自定义的适配器必需继承FragmentStateAdapter
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager2);
        tab = (TabLayout) findViewById(R.id.tab);
        vp2 = (ViewPager2) findViewById(R.id.vp2);
        fragment1 = new Fragment();
        fragment2 = new Fragment();
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
//        vp2.setRotation(-90);
        //设置上下滑动
        vp2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        //设置标题
        titls.add("首页");
        titls.add("第二页");

        //定义适配器
        myAdapter = new MyAdapter(this, fragments, titls);
        vp2.setAdapter(myAdapter);

        //通过TabLayoutMediator 方法参数类型 tablayout   viewpager  new new TabLayoutMediator.TabConfigurationStrategy()
        //tab赋值.settext(集合.get（position）)
        new TabLayoutMediator(tab, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titls.get(position));
            }
        }).attach();


    }

    public class MyAdapter extends FragmentStateAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titls;


        public MyAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragments, ArrayList<String> titls) {
            super(fragmentActivity);
            this.fragments = fragments;
            this.titls = titls;
        }

        public MyAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }


    }
}
