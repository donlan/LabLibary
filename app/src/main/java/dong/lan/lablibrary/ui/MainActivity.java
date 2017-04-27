package dong.lan.lablibrary.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.SPHelper;
import dong.lan.permission.CallBack;
import dong.lan.permission.Permission;

public class MainActivity extends BaseBarActivity {


    @OnClick(R.id.lab_in)
    void labIn() {
        startActivity(new Intent(this,InputActivity.class));
    }

    @OnClick(R.id.lab_out)
    void labOut() {
        startActivity(new Intent(this,OutputActivity.class));

    }

    @OnClick(R.id.lab_store)
    void labStore() {
        startActivity(new Intent(this,StoreActivity.class));
    }

    @OnClick(R.id.lab_record)
    void labRecord() {
        startActivity(new Intent(this,RecordActivity.class));
    }

    @OnClick(R.id.bar_left) void back(){
        finish();
    }

    @BindView(R.id.bar_right)
    ImageButton logout;
    @OnClick(R.id.bar_right)
    void logout(){
        SPHelper.instance().putString("user","");
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView("资产管理");
        logout.setImageResource(R.drawable.logout);
        List<String> pers = new ArrayList<>();
        pers.add(Manifest.permission.CAMERA);
        Permission.instance().check(new CallBack<List<String>>() {
            @Override
            public void onResult(List<String> result) {

            }
        },this,pers);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permission.instance().handleRequestResult(this,requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
