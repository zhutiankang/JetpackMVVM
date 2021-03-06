package com.example.livedatademo.unpeek;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * 引入 Observer 代理类的设计，
 * 这使得在旋屏重建时，无需通过反射方式跟踪和复用基类 Map 中的 Observer，
 * 转而通过 removeObserver 的方式来自动移除和在页面重建后重建新的 Observer，
 * <p>
 * 因而复杂度由原先的分散于基类数据结构，到集中在 proxy 对象这一处，
 * 进一步方便了源码逻辑的阅读和后续的修改。
 * <p>
 * <p>
 * TODO 唯一可信源设计
 * 在 V6 中继续沿用从 V3 版延续下来的基于 "唯一可信源" 理念的设计，
 * 来确保 "事件" 的发送权牢牢握在可信的逻辑中枢单元手里，从而确保所有订阅者收到的信息都是可靠且一致的，
 * TODO 以及支持消息从内存清空
 * 在 V6 中继续沿用从 V3 版延续下来的 "消息清空" 设计，
 * 我们支持通过 clear 方法手动将消息从内存中清空，
 * 以免无用消息随着 SharedViewModel 的长时间驻留而导致内存溢出的发生。
 *
 * @author zhutiankang
 */
public class ProtectedUnPeekLiveDataOld<T> extends LiveData<T> {

  private final static String TAG = "V6Test";

  protected boolean isAllowNullValue;

  private final ConcurrentHashMap<Observer<? super T>, Boolean> observerStateMap = new ConcurrentHashMap();

  private final ConcurrentHashMap<Observer<? super T>, Observer<? super T>> observerProxyMap = new ConcurrentHashMap();

  /**
   * TODO 当 liveData 用作 event 用途时，可使用该方法来观察 "生命周期敏感" 的非粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的，
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   * 一次性事件，在页面创建之前数据扔掉，防止数据倒灌
   *
   * @param owner
   * @param observer
   */
  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    Observer<? super T> observer1 = getObserverProxy(observer);
    if (observer1 != null) {
      super.observe(owner, observer1);
    }
  }

  /**
   * TODO 当 liveData 用作 event 用途时，可使用该方法来观察 "生命周期不敏感" 的非粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的，
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   *
   *
   * @param observer
   */
  @Override
  public void observeForever(@NonNull Observer<? super T> observer) {
    Observer<? super T> observer1 = getObserverProxy(observer);
    if (observer1 != null) {
      super.observeForever(observer1);
    }
  }

  private Observer<? super T> getObserverProxy(Observer<? super T> observer) {
    if (observerStateMap.containsKey(observer)) {
      Log.d(TAG, "observe repeatedly, observer has been attached to owner");
      return null;
    } else {
      observerStateMap.put(observer, false);
      ObserverProxy proxy = new ObserverProxy(observer);
      observerProxyMap.put(observer, proxy);
      return proxy;
    }
  }

  private class ObserverProxy implements Observer<T> {

    private final Observer<? super T> target;

    public ObserverProxy(Observer<? super T> target) {
      this.target = target;
    }

    public Observer<? super T> getTarget() {
      return target;
    }

    @Override
    public void onChanged(T t) {
      if (observerStateMap.get(target) != null && observerStateMap.get(target)) {
        observerStateMap.put(target, false);
        if (t != null || isAllowNullValue) {
          target.onChanged(t);
        }
      }
    }
  }

  /**
   * TODO 当 liveData 用作 state 用途时，可使用该方法来观察 "生命周期敏感" 的粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的，
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   *
   * 自动接收 自动分发 会产生数据倒灌，使用之前的旧数据
   * 当数据在非活跃时更新，observer不会接收到。变为活跃时 将自动接收前面最新的数据。
   * @param owner
   * @param observer
   */
  /**
   * 添加观察者. 事件在主线程分发. 如果LiveData已经有数据，将直接分发给observer。
   * 观察者只在LifecycleOwner活跃时接受事件，如果变为DESTROYED状态，observer自动移除。
   * 当数据在非活跃时更新，observer不会接收到。变为活跃时 将自动接收前面最新的数据。
   * LifecycleOwner非DESTROYED状态时，LiveData持有observer和 owner的强引用，DESTROYED状态时自动移除引用。
   * @param owner    控制observer的LifecycleOwner
   * @param observer 接收事件的observer
   */
  public void observeSticky(LifecycleOwner owner, Observer<T> observer) {
    super.observe(owner, observer);
  }

  /**
   * TODO 当 liveData 用作 state 用途时，可使用该方法来观察 "生命周期不敏感" 的粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   *
   *
   * @param observer
   */
  public void observeStickyForever(Observer<T> observer) {
    super.observeForever(observer);
  }

  @Override
  protected void setValue(T value) {
    if (value != null || isAllowNullValue) {
      for (Map.Entry<Observer<? super T>, Boolean> entry : observerStateMap.entrySet()) {
        entry.setValue(true);
      }
      super.setValue(value);
    }
  }

  @Override
  public void removeObserver(@NonNull Observer<? super T> observer) {
    Observer<? super T> proxy;
    Observer<? super T> target;
    if (observer instanceof ProtectedUnPeekLiveDataOld.ObserverProxy) {
      proxy = observer;
      target = ((ObserverProxy) observer).getTarget();
    } else {
      proxy = observerProxyMap.get(observer);
      target = (proxy != null) ? observer : null;
    }
    if (proxy != null && target != null) {
      observerProxyMap.remove(target);
      observerStateMap.remove(target);
      super.removeObserver(proxy);
    }
  }

  /**
   * 手动将消息从内存中清空，
   * 以免无用消息随着 SharedViewModel 的长时间驻留而导致内存溢出的发生。
   */
  public void clear() {
    super.setValue(null);
  }

}
