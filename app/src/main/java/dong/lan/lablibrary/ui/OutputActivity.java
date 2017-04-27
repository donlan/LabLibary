package dong.lan.lablibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.comman.UserManager;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.model.Record;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.Config;

public class OutputActivity extends BaseBarActivity {

    @BindView(R.id.output_name)
    TextView name;
    @BindView(R.id.output_no)
    TextView no;
    @BindView(R.id.output_total_count)
    TextView assetCount;
    @BindView(R.id.output_count)
    TextView count;
    @BindView(R.id.output_remark)
    EditText remark;
    @BindView(R.id.bar_right)
    ImageButton scan;

    @OnClick(R.id.bar_right)
    void scan() {
        startActivityForResult(new Intent(this, CaptureActivity.class), Config.REQUEST_CODE_ZXING);
    }

    private Asset asset;

    @OnClick(R.id.output_ok)
    void ensureOutput() {
        if (asset == null) {
            toast("无效资产数据");
            return;
        }
        final int need = Integer.valueOf(count.getText().toString());
        if (need < asset.getCount()) {
            alert("出库中...");
            asset.setCount(asset.getCount() - need);
            asset.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    dismiss();
                    if (e == null) {
                        Record record = new Record(UserManager.instance().curUser(),
                                Record.out,
                                asset,
                                need,
                                System.currentTimeMillis(),
                                remark.getText().toString()
                        );
                        record.save();
                        Config.DATA_CHANGE = true;
                        toast("出库成功");
                    } else {
                        toast("更新入库失败:" + e.getMessage());
                    }
                }
            });
        } else {
            toast("出库数量大于库存量(库存:" + asset.getCount() + ")");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        bindView("出库");
        scan.setImageResource(R.drawable.scan);
        Asset no = (Asset) getIntent().getSerializableExtra(Config.ASSET);
        if (no!=null)
            queryAsset(no.getObjectId());
        else
            startActivityForResult(new Intent(this, CaptureActivity.class), Config.REQUEST_CODE_ZXING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Config.REQUEST_CODE_ZXING) {
            Bundle res = data.getExtras();
            String resStr = res.getString("result");
            handleScanResult(resStr);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void queryAsset(String id) {
        alert("查询资产:" + id);
        BmobQuery<Asset> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Asset>() {
            @Override
            public void done(Asset asset, BmobException e) {
                dismiss();
                if (e == null) {
                    if (asset == null ) {
                        toast("无资产数据");
                    } else {
                        OutputActivity.this.asset = asset;
                        name.setText(asset.getName());
                        no.setText(asset.getNo());
                        assetCount.setText("" + asset.getCount());
                    }
                } else {
                    toast("加载数据失败:" + e.getMessage());
                }
            }
        });
    }

    private void handleScanResult(String resStr) {
        Log.d("TAG", "handleScanResult: "+resStr);
        if (TextUtils.isEmpty(resStr) || !resStr.contains(Config.BASE_API)) {
            toast("无效的二维码参数");
            finish();
        } else {
            alert("获取资产数据中...");
            final String noStr = resStr.substring((Config.BASE_API + "no=").length());
            Log.d("TAG", "handleScanResult: "+noStr);
            BmobQuery<Asset> query = new BmobQuery<>();
            query.getObject(noStr, new QueryListener<Asset>() {
                @Override
                public void done(Asset asset, BmobException e) {
                    dismiss();
                    if (e == null) {
                        if (asset == null ) {
                            toast("无资产数据");
                        } else {
                            OutputActivity.this.asset = asset;
                            name.setText(asset.getName());
                            no.setText(asset.getNo());
                            assetCount.setText("" + asset.getCount());
                        }
                    } else {
                        toast("加载数据失败:" + e.getMessage());
                    }
                }
            });
        }
    }

}
