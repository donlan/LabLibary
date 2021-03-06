package dong.lan.lablibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.comman.UserManager;
import dong.lan.lablibrary.model.Asset;
import dong.lan.lablibrary.model.Record;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.Config;

public class InputActivity extends BaseBarActivity {
    @BindView(R.id.input_name)
    EditText name;
    @BindView(R.id.input_no)
    EditText no;
    @BindView(R.id.input_count)
    EditText assetCount;
    @BindView(R.id.input_new_count)
    EditText count;
    @BindView(R.id.input_new_remark)
    EditText remark;

    @OnClick(R.id.input_ok)
    void ensureInput() {
        if (asset == null) { //新入库
            toast("检测库存中...");
            BmobQuery<Asset> query = new BmobQuery<>();
            query.addWhereEqualTo("user",UserManager.instance().curUser());
            query.addWhereEqualTo("no",no.getText().toString());
            query.findObjects(new FindListener<Asset>() {
                @Override
                public void done(List<Asset> list, BmobException e) {
                    if(e !=null){
                        e.printStackTrace();
                        toast("检查库存失败："+e.getMessage());
                    }else{
                        if(list==null || list.isEmpty()){
                            newInput();
                        }else{
                            toast("已存在此编号的库存，可更新该库存");
                            initAsset(list.get(0));
                            toast("此时入库只更新库存数量");
                        }
                    }
                }
            });
        } else { //更新入库
            toast("更新库存...");
            updateInput();
        }
    }


    private void newInput(){
        asset = new Asset(UserManager.instance().curUser(),
                no.getText().toString(),
                name.getText().toString(),
                Integer.valueOf(count.getText().toString()),
                remark.getText().toString(),
                System.currentTimeMillis(),
                System.currentTimeMillis());
        asset.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    toast("入库成功");
                    asset.setObjectId(s);
                    Record record = new Record(UserManager.instance().curUser(),
                            Record.in,
                            asset,
                            Integer.valueOf(count.getText().toString()),
                            System.currentTimeMillis(),
                            remark.getText().toString()
                    );
                    record.save();
                    Config.DATA_CHANGE = true;
                } else {
                    toast("入库失败:" + e);
                }
            }
        });
    }

    private void updateInput(){
        final int need = Integer.valueOf(count.getText().toString());
        if (need > 0) {
            asset.setCount(asset.getCount() + need);
            asset.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        toast("入库成功");
                        Record record = new Record(UserManager.instance().curUser(),
                                Record.in,
                                asset,
                                need,
                                System.currentTimeMillis(),
                                remark.getText().toString()
                        );
                        record.save();
                        Config.DATA_CHANGE = true;
                    } else {
                        toast("更新入库失败:" + e.getMessage());
                    }
                }
            });
        }
    }

    @OnClick(R.id.bar_right)
    void scanInput() {
        startActivityForResult(new Intent(this, CaptureActivity.class), Config.REQUEST_CODE_ZXING);
    }

    private Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        bindView("入库");
        setBarRightIcon(R.drawable.scan);
        Asset no = (Asset) getIntent().getSerializableExtra(Config.ASSET);
        if (no!=null)
            queryAsset(no.getObjectId());
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


    private void initAsset(Asset asset){
        InputActivity.this.asset = asset;
        name.setText(InputActivity.this.asset.getName());
        no.setText(InputActivity.this.asset.getNo());
        assetCount.setText("" + asset.getCount());
        name.setFocusable(false);
        no.setFocusable(false);
        assetCount.setFocusable(false);
    }

    private void queryAsset(String  id) {
        alert("查询资产:" + id);
        BmobQuery<Asset> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<Asset>() {
            @Override
            public void done(Asset asset, BmobException e) {
                dismiss();
                if (e == null) {
                    if (asset == null) {
                        toast("无资产数据");
                    } else {
                        initAsset(asset);
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
            toast("无效二维码数据");
        } else {
            alert("获取资产数据中...");
            final String noStr = resStr.substring((Config.BASE_API + "no=").length());;
            queryAsset(noStr);
        }
    }
}
