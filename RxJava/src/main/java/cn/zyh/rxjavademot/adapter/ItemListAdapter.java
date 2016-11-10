package cn.zyh.rxjavademot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/12.
 */
public class ItemListAdapter extends RecyclerView.Adapter {


    private List<ItemModel> items;

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        if(mViewHolder instanceof ViewHolder){
            //获得每个条目
            ItemModel item = items.get(position);
            mViewHolder.tvDescription.setText(item.description);
            Glide.with(mViewHolder.imageIv.getContext()).load(item.imageUrl).into(mViewHolder.imageIv);
        }
    }

    //设置数据
    public void setItem(List<ItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.imageIv)
        ImageView imageIv;
        @Bind(R.id.tvDescription)
        TextView tvDescription;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
