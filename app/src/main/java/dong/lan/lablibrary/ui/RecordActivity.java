package dong.lan.lablibrary.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.adapter.BinderClickListener;
import dong.lan.lablibrary.adapter.binder.RecordBinder;
import dong.lan.lablibrary.comman.UserManager;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.model.Record;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.Config;

public class RecordActivity extends BaseBarActivity implements BinderClickListener<Record> {

    @BindView(R.id.record_list)
    RecyclerView recordList;
    @BindView(R.id.record_refresh)
    SwipeRefreshLayout refreshLayout;
    private Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        bindView("资产记录");
        initView();
    }

    private void initView() {
        asset = (Asset) getIntent().getSerializableExtra(Config.ASSET);
        recordList.setLayoutManager(new GridLayoutManager(this, 1));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();

    }

    private void loadData() {
        alert("加载数据...");
        BmobQuery<Record> query = new BmobQuery<>();
        if (asset != null)
            query.addWhereEqualTo("asset", asset);

        query.addWhereEqualTo("user", UserManager.instance().curUser());
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.include("user,asset");
        query.order("-createTime");
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> list, BmobException e) {
                dismiss();
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        toast("无资产数据");
                    } else {
                        RecordBinder binder = new RecordBinder();
                        binder.setBinderClickListener(RecordActivity.this);
                        binder.init(list);
                        recordList.setAdapter(binder.build());
                    }
                } else {
                    toast("加载数据失败:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(Record data, int position, int action) {
        if (action == 0) { //单击

        }
    }
}
