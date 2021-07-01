/*
 * Copyright 2018-2020 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.livedatademo.event;

import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.unpeek.ProtectedUnPeekLiveData;
import com.example.livedatademo.unpeek.UnPeekLiveData;

import androidx.lifecycle.ViewModel;

/**
 * Create by KunMinX at 2020/5/30
 */
public class SharedViewModel extends ViewModel {

  private final UnPeekLiveData<Moment> mMoment = new UnPeekLiveData<>();

  private final UnPeekLiveData<String> mTestDelayMsg = new UnPeekLiveData<>();

  private final UnPeekLiveData<String> mTestNull = new UnPeekLiveData.Builder<String>().setAllowNullValue(true).create();

  public ProtectedUnPeekLiveData<Moment> getMoment() {
    return mMoment;
  }

  public ProtectedUnPeekLiveData<String> getTestDelayMsg() {
    return mTestDelayMsg;
  }

  public void requestMoment(Moment moment) {
    mMoment.setValue(moment);
  }

  public void requestTestDelayMsg(String s) {
    mTestDelayMsg.setValue(s);
  }



  //TODO 特殊例子
  public UnPeekLiveData<Moment> test =
          new UnPeekLiveData.Builder<Moment>()
                  .setAllowNullValue(false)
                  .create();

}
