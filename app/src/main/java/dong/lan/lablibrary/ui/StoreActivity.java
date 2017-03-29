package dong.lan.lablibrary.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.adapter.BinderClickListener;
import dong.lan.lablibrary.adapter.binder.AssetBinder;
import dong.lan.lablibrary.comman.UserManager;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.Config;

public class StoreActivity extends BaseBarActivity implements BinderClickListener<Asset> {


    private static final String TAG = StoreActivity.class.getSimpleName();
    @BindView(R.id.asset_list)
    RecyclerView assetList;
    @BindView(R.id.bar_right)
    ImageButton search;
    @BindView(R.id.search_no)
    EditText searchNo;
    @BindView(R.id.search_name)
    EditText searchName;
    @BindView(R.id.search_parent)
    LinearLayout searchParent;
    private BmobRealTimeData data;
    private boolean needRefresh = false;

    @OnClick(R.id.bar_right)
    void showSearch() {
        searchParent.setVisibility(searchParent.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.search)
    void search(View view) {
        String no = searchNo.getText().toString();
        String name = searchName.getText().toString();
        searchParent.setVisibility(View.GONE);
        if (TextUtils.isEmpty(no) && TextUtils.isEmpty(name)) {
            if (binder != null)
                binder.showMainData(true);
            return;
        }
        alert("搜索中...");
        BmobQuery<Asset> query = new BmobQuery<>();
        query.addWhereEqualTo("user", UserManager.instance().curUser());
        query.addWhereContains("no", no);
        query.addWhereContains("name", name);
        query.order("-updateTime");
        query.findObjects(new FindListener<Asset>() {
            @Override
            public void done(List<Asset> list, BmobException e) {
                dismiss();
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        toast("无资产数据");
                    } else {
                        if (binder == null) {
                            binder = new AssetBinder();
                            binder.setBinderClickListener(StoreActivity.this);
                            binder.setCacheData(list);
                            assetList.setAdapter(binder.build());
                        } else {
                            binder.setCacheData(list);
                        }
                    }
                } else {
                    toast("加载数据失败:" + e.getMessage());
                }

            }
        });
    }

    @BindView(R.id.asset_refresh)
    SwipeRefreshLayout refreshLayout;

    private AssetBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        bindView("所有资产");
        initView();
    }

    private void initView() {
        search.setImageResource(R.drawable.search);
        assetList.setLayoutManager(new GridLayoutManager(this, 1));
        loadData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        data = new BmobRealTimeData();
        data.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e == null || data.isConnected()) {
                    data.subTableUpdate("Asset");
                }else {
                    needRefresh = true;
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                Log.d(TAG, "onDataChange: " + jsonObject);
                loadData();
            }
        });

    }

    private void loadData() {
        alert("开始加载数据");
        BmobQuery<Asset> query = new BmobQuery<>();
        query.addWhereEqualTo("user", UserManager.instance().curUser());
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updateTime");
        query.findObjects(new FindListener<Asset>() {
            @Override
            public void done(List<Asset> list, BmobException e) {
                dismiss();
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        toast("无资产数据");
                    } else {
                        if (binder == null) {
                            binder = new AssetBinder();
                            binder.setBinderClickListener(StoreActivity.this);
                            binder.init(list);
                            assetList.setAdapter(binder.build());
                        } else {
                            binder.resetData(list);
                            assetList.getAdapter().notifyDataSetChanged();
                        }
                    }
                } else {
                    toast("加载数据失败:" + e.getMessage());
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (needRefresh && Config.DATA_CHANGE) {
            new AlertDialog.Builder(this)
                    .setMessage("数据已发送改变请刷新")
                    .setPositiveButton("刷新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Config.DATA_CHANGE = false;
                            loadData();
                        }
                    }).show();
        }
    }

    @Override
    public void onClick(final Asset data, int position, int action) {
        if (action == 0) { //单击

            new AlertDialog.Builder(this)
                    .setMessage("请选择资产操作")
                    .setPositiveButton("入库", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(StoreActivity.this, InputActivity.class);
                            intent.putExtra(Config.ASSET_NO, data.getNo());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("出库", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(StoreActivity.this, OutputActivity.class);
                            intent.putExtra(Config.ASSET_NO, data.getNo());
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("查看记录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(StoreActivity.this, RecordActivity.class);
                            intent.putExtra(Config.ASSET, data);
                            startActivity(intent);
                        }
                    })
                    .show();
        } else if (action == 1) { //长按
            Intent intent = new Intent(this, AssetQRCodeActivity.class);
            intent.putExtra(Config.ASSET_NO, data.getNo());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (data != null && data.isConnected())
            data.unsubTableUpdate("Asset");
    }
}
