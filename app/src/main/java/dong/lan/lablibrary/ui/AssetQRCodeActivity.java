package dong.lan.lablibrary.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import dong.lan.lablibrary.R;
import dong.lan.lablibrary.model.Asset;
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
        Asset no = (Asset) getIntent().getSerializableExtra(Config.ASSET);
        if (no == null) {
            toast("无效编号");
        } else {
            Bitmap qrcodeBmp = EncodingUtils.createQRCode(Config.BASE_API + "no=" + no.getObjectId(), 300, 300, null);
            qrcodeImg.setImageBitmap(qrcodeBmp);
            assetNoTv.setText("编号:" + no.getNo());
        }
    }
}
