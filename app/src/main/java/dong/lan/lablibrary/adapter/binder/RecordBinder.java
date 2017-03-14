package dong.lan.lablibrary.adapter.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.adapter.AbstractBinder;
import dong.lan.lablibrary.adapter.BaseHolder;
import dong.lan.lablibrary.adapter.BinderClickListener;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.model.Record;
import dong.lan.lablibrary.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午8:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class RecordBinder extends AbstractBinder<Record> {
    @Override
    public BaseHolder<Record> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<Record> clickListener) {
        this.clickListener = clickListener;
    }


    class ViewHolder extends BaseHolder<Record> {

        @BindView(R.id.item_record_count)
        TextView count;
        @BindView(R.id.item_record_info)
        TextView info;
        @BindView(R.id.item_record_name)
        TextView name;
        @BindView(R.id.item_record_no)
        TextView no;
        @BindView(R.id.item_record_icon)
        ImageView icon;

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
            }
        }

        @Override
        public void bindData(Record record) {
            String num ;
            if(record.getStatus() == Record.in){
                icon.setImageResource(R.drawable.in_icon);
                num ="入库:";
            }else{
                icon.setImageResource(R.drawable.out_icon);
                num ="出库:";
            }
            no.setText("no:" + record.getAsset().getNo());
            count.setText(num + record.getCount());
            name.setText(record.getAsset().getName());
            info.setText("创建时间:" + TimeUtil.getTime(record.getCreateTime(), "yyyy.MM.dd"));
        }
    }
}
