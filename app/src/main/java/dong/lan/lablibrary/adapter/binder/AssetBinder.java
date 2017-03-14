package dong.lan.lablibrary.adapter.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.adapter.AbstractBinder;
import dong.lan.lablibrary.adapter.BaseHolder;
import dong.lan.lablibrary.adapter.BinderClickListener;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午8:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class AssetBinder extends AbstractBinder<Asset> {
    @Override
    public BaseHolder<Asset> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<Asset> clickListener) {
        this.clickListener = clickListener;
    }

    public void resetData(List<Asset> assets) {
        if (data == null)
            data = new ArrayList<>();
        isMainData = true;
        if (!data.isEmpty())
            data.clear();
        data.addAll(assets);
        adapter.notifyDataSetChanged();
    }

    public void setCacheData(List<Asset> assets) {
        if (data == null)
            data = new ArrayList<>();
        if (!cache.isEmpty())
            cache.clear();
        cache.addAll(assets);
        isMainData = false;
        adapter.notifyDataSetChanged();
    }

    public void showMainData(boolean main) {
        if (main != isMainData)
            isMainData = main;
        adapter.notifyDataSetChanged();
    }

    class ViewHolder extends BaseHolder<Asset> {

        @BindView(R.id.item_asset_count)
        TextView count;
        @BindView(R.id.item_asset_info)
        TextView info;
        @BindView(R.id.item_asset_name)
        TextView name;
        @BindView(R.id.item_asset_no)
        TextView no;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (clickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getLayoutPosition();
                        clickListener.onClick(valueAt(pos), pos, 0);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = getLayoutPosition();
                        clickListener.onClick(valueAt(pos), pos, 1);
                        return true;
                    }
                });
            }
        }

        @Override
        public void bindData(Asset asset) {
            no.setText("no:" + asset.getNo());
            count.setText("库存:" + asset.getCount());
            name.setText(asset.getName());
            info.setText("入库时间:" + TimeUtil.getTime(asset.getCreateTime(), "yyyy.MM.dd")
                    + " | 更改时间:" + TimeUtil.getTime(asset.getUpdateTime(), "yyyy.MM.dd"));
        }
    }
}
