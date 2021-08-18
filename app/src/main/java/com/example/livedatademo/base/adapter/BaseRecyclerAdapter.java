package com.example.livedatademo.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseRecyclerAdapter<RecyclerVM extends ViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * ViewModel.
     */
    protected RecyclerVM viewModel;

    /**
     * Constructor with an argument: ViewModel.
     *
     * @param viewModel ViewMode which extends {@link ViewModel}.
     */
    public BaseRecyclerAdapter(RecyclerVM viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = onGenerateRootView(parent, viewType);
        final ViewDataBinding binding = DataBindingUtil.bind(view);
        onViewCreated(binding);
        BaseViewHolder holder = new BaseViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mItemClickListener.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mItemLongClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mItemLongClickListener.onItemLongClick( position);
                return true;
            }
            return false;
        });
        return holder;
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            onSetData(binding, position);
            binding.executePendingBindings();
        }
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent an item, and
     * the returned value of {@link View} used to create instance of the {@link RecyclerView.ViewHolder}.
     *
     * 单布局
     * return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);
     *
     * 多布局
     * final int layoutId;
     *         switch (viewType) {
     *             case LAYOUT_TYPE_BANNER:
     *                 layoutId = R.layout.item_app_store_banner;
     *                 break;
     *
     *             case LAYOUT_TYPE_TITLE:
     *                 layoutId = R.layout.item_app_store_title;
     *                 break;
     *
     *             case LAYOUT_TYPE_BLANK_APP:
     *                 layoutId = R.layout.item_app_store_blank_app;
     *                 break;
     *
     *             case LAYOUT_TYPE_APP:
     *             default:
     *                 layoutId = R.layout.item_app_store_app;
     *                 break;
     *         }
     * return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
     *
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType Type of the view.
     * @return The argument used to create instance of the {@link RecyclerView.ViewHolder}.
     */
    protected abstract View onGenerateRootView(ViewGroup parent, int viewType);

    /**
     * 一般不用
     * Called when item's root view created, and got the {@link ViewDataBinding} instance.
     *
     * @param binding DataBinding.
     */
    protected void onViewCreated(ViewDataBinding binding) {
        // Do something if necessary in child.
    }

    /**
     * Called when RecyclerView needs to show data on each item.
     *  final AppInfo appInfo = viewModel.getLocalApp(position);
     *         if (appInfo == null) {
     *             return;
     *         }//数据
     *         binding.setVariable(BR.localAppInfo, appInfo);
     *         //操作
     *         binding.setVariable(BR.viewModel, viewModel);
     *         binding.setVariable(BR.position, position);
     *         binding.getRoot().setOnClickListener(v -> {});
     *
     *         公共binding  精确binding 可以binding.tvName.setText
     *
     *         // Application type's data.
     *         if (binding instanceof ItemMyAppAppBinding) {
     *             final ItemMyAppAppBinding appBinding = (ItemMyAppAppBinding) binding;}
     *
     *             if (binding instanceof ItemAppUpdateBottomBinding) {
     *             contentBinding = ((ItemAppUpdateBottomBinding) binding).updateLayoutItem;
     *         } else if (binding instanceof ItemAppUpdateNormalBinding) {
     *
     *             final int layoutType = getItemViewType(position);
     *         switch (layoutType) {
     *             case LAYOUT_TYPE_BANNER:
     *                 setBannerData((ItemAppStoreBannerBinding) binding);
     *                 break;
     *
     *             case LAYOUT_TYPE_TITLE:
     *                 setTitleData();
     *                 break;
     *
     *             case LAYOUT_TYPE_APP:
     *                 setAppData((ItemAppStoreAppBinding) binding, position);
     *                 break;
     *
     *             case LAYOUT_TYPE_BLANK_APP:
     *                 break;
     *
     *             default:
     *                 TKStoreLog.e(TAG, "Unknown layout type: " + layoutType);
     *                 break;
     *         }
     * @param binding  DataBinding.
     * @param position The index of current item.
     */
    protected abstract void onSetData(ViewDataBinding binding, int position);

    /**
     * Create the RecyclerView's layout.
     * @param context Context.
     * @return RecyclerView's layout.
     */
    protected abstract RecyclerView.LayoutManager onCreateLayoutManager(Context context);


    private final static class BaseViewHolder extends RecyclerView.ViewHolder {
        /**
         * Package-private constructor.
         *
         * @param itemView The view of each item.
         */
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

//    @Override
//    public int getItemCount() {
//        final List<PackageEntity> packageEntities = viewModel.getPackageEntityList();
//        return packageEntities == null ? 0 : packageEntities.size();
//    }


//    @Override 多布局
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return LAYOUT_TYPE_BANNER;
//        } else if (position == 1) {
//            return LAYOUT_TYPE_TITLE;
//        } else if (appSize != 0) {
//            return LAYOUT_TYPE_APP;
//        } else {
//            return LAYOUT_TYPE_BLANK_APP;
//        }
//    }

//    @Override
//    protected RecyclerView.LayoutManager onCreateLayoutManager(Context context) {
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        return layoutManager;

//          return new GridLayoutManager(context, 3);

//         GridLayoutManager layoutManager = new GridLayoutManager(context, TITLE_SIZE_SPAN);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//        @Override
//        public int getSpanSize(int position) {
//            if (viewModel.getAppInfos() == null) {
//                return TITLE_SIZE_SPAN;
//            }
//            return viewModel.getAppInfo(position).isTitle() ? TITLE_SIZE_SPAN : ITEM_SIZE_SPAN;
//        }
//    });
//        return layoutManager;
//    }


    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemLongClickListener onItemLongClickListener) {
        mItemLongClickListener = onItemLongClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}