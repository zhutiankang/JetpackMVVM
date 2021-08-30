package com.example.livedatademo.base.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * /**
 *  * author :
 *  * time   : 2021/06/01
 *  * desc   : 可以使用viewpager搭载fragment 替换showFragment
 *  */

public class ViewPagerSlide extends ViewPager {
    //是否可以进行滑动 false禁止左右滑动 true可以左右滑动
    private boolean isSlide = false;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }
    public ViewPagerSlide(Context context) {
        super(context);
    }

    public ViewPagerSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
//        this.isSlide && super.onInterceptTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //true被消耗,处理，不会往外传递
        //false[默认返回的是false]没有被消耗，不处理，那么move  up这些后续事件就不会再交给view的OnTouchEvent方法啦
        return this.isSlide && super.onTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if (isScroll) {
//            return super.onInterceptTouchEvent(event);
//        } else {
//            return false;
//        }
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (isScroll) {
//            return super.onTouchEvent(event);
//        } else {
//            return true;
//        }
//    }

}
