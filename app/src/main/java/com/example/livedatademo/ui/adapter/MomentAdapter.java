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

import android.content.Context;

import com.example.livedatademo.R;
import com.example.livedatademo.base.adapter.SimpleBindingAdapter;
import com.example.livedatademo.data.bean.Moment;
import com.example.livedatademo.databinding.AdapterMomentBinding;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by KunMinX at 2020/5/31
 */
public class MomentAdapter extends SimpleBindingAdapter<Moment, AdapterMomentBinding> {

    public MomentAdapter(Context context) {
        super(context, R.layout.adapter_moment, new DiffUtilCallbacks().getMomentItemCallback());
    }

    @Override
    protected void onBindItem(AdapterMomentBinding binding, Moment item, RecyclerView.ViewHolder holder) {
        // 绑定布局参数
        binding.setMoment(item);
    }
}
