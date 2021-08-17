package com.example.livedatademo.unpeek;

/**
 * 目前提供 "是否允许发送 null" 的选项。
 *
 * @author zhutiankang
 */
public class UnPeekLiveData<T> extends ProtectedUnPeekLiveData<T> {

  @Override
  public void setValue(T value) {
    super.setValue(value);
  }

  @Override
  public void postValue(T value) {
    super.postValue(value);
  }

  public static class Builder<T> {

    /**
     * 是否允许传入 null value
     * 允许value为空通知 默认为null不通知
     */
    private boolean isAllowNullValue;

    public Builder<T> setAllowNullValue(boolean allowNullValue) {
      this.isAllowNullValue = allowNullValue;
      return this;
    }

    public UnPeekLiveData<T> create() {
      UnPeekLiveData<T> liveData = new UnPeekLiveData<>();
      liveData.isAllowNullValue = this.isAllowNullValue;
      return liveData;
    }
  }
}
