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

package com.example.livedatademo.ui.adapter;


import com.example.livedatademo.data.bean.Moment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

/**
 * DiffUtil的主是用与RecyclerView的局部更新，从而提高页面刷新效率
 *
 * 当areItemsTheSame返回为false时，不管areContentsTheSame是否为true，adapter中的条目都会更新
 *
 * //返回值表示新数据传入时这两个位置的数据是否时同一个条目
 * public abstract boolean areItemsTheSame(int oldItemPosition, int newItemPosition);
 * //返回值表示新老位置的数据内容是否相同，这个方法在areItemsTheSame（）返回true时生效
 *
 */
public class DiffUtilCallbacks {

  public DiffUtil.ItemCallback<Moment> getMomentItemCallback() {
    return new DiffUtil.ItemCallback<Moment>() {

      @Override
      public boolean areItemsTheSame(@NonNull Moment oldItem, @NonNull Moment newItem) {
        return oldItem.getUuid().equals(newItem.getUuid());
      }

      @Override
      public boolean areContentsTheSame(@NonNull Moment oldItem, @NonNull Moment newItem) {
        return oldItem.getContent().equals(newItem.getContent());
      }
    };
  }
}
