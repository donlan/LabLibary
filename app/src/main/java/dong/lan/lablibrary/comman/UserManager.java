package dong.lan.lablibrary.comman;

import dong.lan.lablibrary.model.LUser;
import dong.lan.lablibrary.utils.MyGson;
import dong.lan.lablibrary.utils.SPHelper;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午8:17.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class UserManager {

    private UserManager() {
        user = MyGson.gson().toTarget(SPHelper.instance().getString("user"),LUser.class);
    }

    private LUser user;

    private static UserManager manager;

    public static UserManager instance(){
        if(manager == null)
            manager = new UserManager();
        return manager;
    }

    public void initUser(LUser user){
        this.user = user;
    }

    public LUser curUser(){
        return user;
    }

    public boolean isLogin(){
        return user != null;
    }


}
