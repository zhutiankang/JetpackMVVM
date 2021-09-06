package com.example.livedatademo.unpeek;

import android.text.TextUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * V7 版源码相比于 V6 版的改进之处在于：
 * 通过在 "代理类/包装类" 中自行维护一个版本号，在 UnPeekLiveData 中维护一个当前版本号，
 * 分别来在 setValue 和 Observe 的时机来改变和对齐版本号，
 * 如此使得无需另外管理一个 Observer map，从而进一步规避了内存管理的问题，
 * 同时也是继 V6 版源码以来，最简的源码设计，方便阅读理解和后续修改。
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
public class ProtectedUnPeekLiveData7_1<T> extends LiveData<T> {

  private final static int START_VERSION = -1;

  private final AtomicInteger currentVersion = new AtomicInteger(START_VERSION);

  protected boolean isAllowNullValue;

  /**
   * TODO 当 liveData 用作 event 用途时，可使用该方法来观察 "生命周期敏感" 的非粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的，
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   * 一次性事件，在页面创建之前数据扔掉，防止数据倒灌  不setvalue休想接到数据
   * LiveData 的生存期长于任何一个 Fragment（假设通信双方是 Fragment）：当二级 Fragment 出栈时，LiveData 实例仍存在。
   *
   * 另一方面，LiveData 本身是被设计为粘性事件的，也即，一旦 LiveData 中持有数据，那么在观察者订阅该 LiveData 时 observe，会被推送最后一次数据。
   * 那么接下来，用户在列表页中 选择另一个 item 点击，并再次跳到详情页时，
   * 就会因为观察了 “已实例化过、并携带有旧数据” 的 MutableLiveData，而收到它的 “不符合预期” 的推送。
   *
   * 这样的设定本身也符合 LiveData 的常用场景 —— 比如 在页面重建时，自动推送最后一次数据，而不必重新去向后台请求。
   * 只不过，这个特性在 “页面间通信” 场景下，就是致命的灾难
   * @param owner activity 传入 this，fragment 建议传入 getViewLifecycleOwner
   * @param observer
   */
  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    super.observe(owner, createObserverWrapper(observer, currentVersion.get()));
  }

  /**
   * TODO 当 liveData 用作 event 用途时，可使用该方法来观察 "生命周期不敏感" 的非粘性消息
   *
   * @param observer
   */
  @Override
  public void observeForever(@NonNull Observer<? super T> observer) {
    super.observeForever(createObserverForeverWrapper(observer, currentVersion.get()));
  }

  /**
   * TODO 当 liveData 用作 state 用途时，可使用该方法来观察 "生命周期敏感" 的粘性消息
   *
   * state 是可变且私用的，event 是只读且公用的，
   * state 的倒灌是应景的，event 倒灌是不符预期的，
   *
   * 自动接收 自动分发 会产生数据倒灌，使用之前的旧数据
   * 当数据在非活跃时更新，observer不会接收到。变为活跃时 将自动接收前面最新的数据。
   * @param owner activity 传入 this，fragment 建议传入 getViewLifecycleOwner
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
    super.observe(owner, createObserverWrapper(observer, START_VERSION));
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
    super.observeForever(createObserverForeverWrapper(observer, START_VERSION));
  }

  /**
   * TODO tip：只需重写 setValue
   * postValue 最终还是会经过这里
   *
   * @param value value
   */
  @Override
  protected void setValue(T value) {
    currentVersion.getAndIncrement();
    super.setValue(value);
  }

  /**
   * TODO tip：
   * 1.添加一个包装类，自己维护一个版本号判断，用于无需 map 的帮助也能逐一判断消费情况
   * 2.重写 equals 方法和 hashCode，在用于手动 removeObserver 时，忽略版本号的变化引起的变化
   */
  class ObserverWrapper implements Observer<T> {
    private final Observer<? super T> mObserver;
    private int mVersion = START_VERSION;
    private boolean mIsForever;

    public ObserverWrapper(@NonNull Observer<? super T> observer, int version, boolean isForever) {
      this(observer, version);
      this.mIsForever = isForever;
    }

    public ObserverWrapper(@NonNull Observer<? super T> observer, int version) {
      this.mObserver = observer;
      this.mVersion = version;
    }

    @Override
    public void onChanged(T t) {
      if (currentVersion.get() > mVersion && (t != null || isAllowNullValue)) {
        mObserver.onChanged(t);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ObserverWrapper that = (ObserverWrapper) o;
      return Objects.equals(mObserver, that.mObserver);
    }

    @Override
    public int hashCode() {
      return Objects.hash(mObserver);
    }

    @NonNull
    @Override
    public String toString() {
      return mIsForever ? "IS_FOREVER" : "";
    }
  }

  @Override
  public void removeObserver(@NonNull Observer<? super T> observer) {
    if (TextUtils.isEmpty(observer.toString())) {
      super.removeObserver(observer);
    } else {
      super.removeObserver(createObserverWrapper(observer, -1));
    }
  }

  private ObserverWrapper createObserverForeverWrapper(@NonNull Observer<? super T> observer, int version) {
    return new ObserverWrapper(observer, version, true);
  }

  private ObserverWrapper createObserverWrapper(@NonNull Observer<? super T> observer, int version) {
    return new ObserverWrapper(observer, version);
  }

  /**
   * TODO tip：
   * 手动将消息从内存中清空，
   * 以免无用消息随着 SharedViewModel 的长时间驻留而导致内存溢出的发生。
   */
  public void clear() {
    super.setValue(null);
  }

}
