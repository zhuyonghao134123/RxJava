package cn.zyh.rxjavademot.module.elementary_1.adapter;

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
import cn.zyh.rxjavademot.module.elementary_1.model.ElementImageModel;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/10.
 */
public class ElementAdapter extends RecyclerView.Adapter {


    private List<ElementImageModel> imaglists;


    //item数量
    public int getItemCount() {
        return imaglists == null ? 0 : imaglists.size();
    }

    //onCreateViewHolder中负责为Item创建视图
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.grid_item, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(view);
    }

    //onBindViewHolder负责将数据绑定到Item的视图上。
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        ElementImageModel elementImageModel = imaglists.get(position);
        String imgUrl = elementImageModel.image_url;
        String describe = elementImageModel.description;
        //加载图片
        Glide.with(holder.itemView.getContext()).load(imgUrl).into(mViewHolder.imageIv);
        //加载描述
        mViewHolder.tvDescription.setText(describe);
    }

    //设置图片
    public void setImages(List<ElementImageModel> imglists) {
        this.imaglists = imglists;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageIv)
        ImageView imageIv;
        @Bind(R.id.tvDescription)
        TextView tvDescription;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
