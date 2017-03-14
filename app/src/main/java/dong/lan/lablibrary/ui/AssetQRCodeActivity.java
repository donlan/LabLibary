package dong.lan.lablibrary.ui;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.ui.base.BaseBarActivity;
import dong.lan.lablibrary.utils.Config;

public class AssetQRCodeActivity extends BaseBarActivity {

    @BindView(R.id.qrcode_img)
    ImageView qrcodeImg;
    @BindView(R.id.asset_no_tv)
    TextView assetNoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_qrcode);
        bindView("二维码");

        initView();
    }

    private void initView() {
        String no = getIntent().getStringExtra(Config.ASSET_NO);
        if (TextUtils.isEmpty(no)) {
            toast("无效编号");
        } else {
            Bitmap qrcodeBmp = EncodingUtils.createQRCode(Config.BASE_API + "no=" + no, 300, 300, null);
            qrcodeImg.setImageBitmap(qrcodeBmp);
            assetNoTv.setText("编号:" + no);
        }
    }
}
