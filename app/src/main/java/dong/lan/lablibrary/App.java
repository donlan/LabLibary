package dong.lan.lablibrary;

import android.app.Application;
import android.content.res.Configuration;

import cn.bmob.v3.Bmob;
import dong.lan.lablibrary.utils.SPHelper;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午2:59.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "85c5b6798e78235fe3a5e7c92a9bb713");
        SPHelper.instance().init(this,"lab");
    }

}


